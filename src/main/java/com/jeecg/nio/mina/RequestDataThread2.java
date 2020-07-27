package com.jeecg.nio.mina;

import com.jeecg.device.entity.BaseDeviceEntity;
import com.jeecg.nio.mqtt.service.MqttSendSevice;
import com.jeecg.scene.entity.SceneDeviceDepolyEntity;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.HexConvertUtil;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Component
public class RequestDataThread2 implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(MinaDataDealThread.class);

	@Autowired
	private SystemService systemService;
	@Autowired
	private MqttSendSevice mqttSendSevice;

	private IoSession session;
	private String address;

	// 请求命令数据类型
	private String cmdDataType = "string"; // bytes:二进制数组;

	public RequestDataThread2() {
		super();
	}

	public RequestDataThread2(IoSession session) {
		this.session = session;
		this.address = session.getRemoteAddress().toString();
		logger.info("RequestDataThread***");
	}

	private String getAddress() {
		// 验证地址
		if (!this.address.matches("/\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}:\\d+")) {
			mqttSendSevice.send(this.getClass(), "info", "[mqtt]IP地址解析有误：1、默认场景是否正确；2、客户端IP地址是否正确；", "$forallcn/iotcp/logger");
			throw new RuntimeException("IP地址解析有误：1、默认场景是否正确；2、客户端IP地址是否正确");
		}
		// 正则表达式截取IP地址
		return StringUtil.getMatcher("\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}", this.address);
	}

	private SceneDeviceDepolyEntity getDeployDevice(String IP) {
		// 获取部署设备，根据默认场景的IP地址
		String sql = "select sdd.id id,sdd.scene_by sceneBy,sdd.device_by deviceBy,sdd.device_parent_by deviceParentBy,sdd.device_code deviceCode," +
				"sdd.device_address deviceAddress,sdd.is_send_cmd isSendCmd,sdd.cmd_data_type cmdDataType,sdd.request_cmd requestCmd " +
				"from jform_scene_device_depoly sdd left join jform_scene s on sdd.scene_by=s.id " +
				"where 1=1 and s.is_default_view=1 and sdd.device_address=?";
		Map<String, Object> sddMap = systemService.findOneForJdbc(sql, IP);
		if (sddMap == null && sddMap.size() != 1) {
			mqttSendSevice.send(this.getClass(), "info", "[mqtt]获取部署设备有误：1、默认场景是否正确；2、客户端IP地址是否正确", "$forallcn/iotcp/logger");
			throw new RuntimeException("获取部署设备有误：1、默认场景是否正确；2、客户端IP地址是否正确");
		}
		mqttSendSevice.send(this.getClass(), "info", "[mqtt]部署设备id: " + sddMap.get("id").toString(), "$forallcn/iotcp/logger");
		SceneDeviceDepolyEntity sdd = new SceneDeviceDepolyEntity();
		try {
			MyBeanUtils.copyMap2Bean(sdd, sddMap);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return sdd;
	}

	@Override
	public void run() {
		/* 判断ApplicationContext */
		this.systemService = this.contextGetBeen(SystemService.class);
		this.mqttSendSevice = this.contextGetBeen(MqttSendSevice.class);
		// 1.解析IP地址
		String IP = getAddress();
		byte[] b = HexConvertUtil.hexStringToBytes("01 03 00 00 00 24 45 D1");
		session.write(IoBuffer.wrap(b));
		// 2.获取部署设备，根据默认场景的IP地址
		SceneDeviceDepolyEntity sdd = getDeployDevice(IP);
		// 3.根据部署设备中的设备id，获取基础设备
		BaseDeviceEntity bDevice = systemService.get(BaseDeviceEntity.class, sdd.getDeviceBy());
		mqttSendSevice.send(this.getClass(), "info", "[mqtt]设备：" + bDevice.getId() + " 设备名称：" + bDevice.getName() + " 设备类型-" + bDevice.getType(), "$forallcn/iotcp/logger");
		// 判断设备类型，非物联设备不做处理, 直接返回
		if ("nsdevice".equals(bDevice.getType())) return;
		// 判断设备是否需要主动请求
		String workModel = bDevice.getWorkModel();
		// 如果不是轮询请求即被动
		if (!"passive".equals(workModel)) {
			mqttSendSevice.send(this.getClass(), "info", "[mqtt]设备：" + bDevice.getName() + "不需要请求命令", "$forallcn/iotcp/logger");
			return;
		}
		mqttSendSevice.send(this.getClass(), "info", "[mqtt][mqtt]设备：" + bDevice.getName() + "需要请求命令", "$forallcn/iotcp/logger");
		/* 开始请求数据，需要判断请求数据 在哪个设备中存储，如果在网关中则一次发送，如在子设备终端中循环发送 */
		boolean running = true;
		while ((!session.isClosing()) && running) {
			// 请求时间(ms)
			Integer requestTime = Integer.valueOf(bDevice.getRequestTime());
			if ("gateway".equals(bDevice.getType()) && "N".equals(sdd.getIsSendCmd())) { // 网关设备，需要获取子设备
				List<SceneDeviceDepolyEntity> childList = systemService.findHql("from SceneDeviceDepolyEntity where deviceParentBy=?", sdd.getId());
				for (SceneDeviceDepolyEntity csdd : childList) {
					mqttSendSevice.send(this.getClass(), "info", "[mqtt]设备：" + bDevice.getName() + "请求命令：" + csdd.getRequestCmd(), "$forallcn/iotcp/logger");
					String cmd = csdd.getRequestCmd();
					// 子设备-基础设备
					BaseDeviceEntity cbDevice = systemService.get(BaseDeviceEntity.class, csdd.getDeviceBy());
					mqttSendSevice.send(this.getClass(), "info", "[mqtt]" + cbDevice.getName() + "的请求命令: " + cmdDataType, "$forallcn/iotcp/logger");
					if (StringUtil.isBlank(cmdDataType)) {
						running = false;
						return;
					}
					this.writeData(session, csdd.getCmdDataType(), sdd.getRequestCmd());
					try {
						// 具有父子关系的子设备，把子设备的轮询时间作为网关遍历的延时时间
						Thread.sleep(cbDevice.getRequestTime());
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}
			} else if ("terminal".equals(bDevice.getType()) || ("gateway".equals(bDevice.getType()) && "Y".equals(sdd.getIsSendCmd()))) {
				this.writeData(session, sdd.getCmdDataType(), sdd.getRequestCmd());
			} else {
				mqttSendSevice.send(this.getClass(), "info", "[mqtt]无操作，网关或设备发送请求命令逻辑错误！", "$forallcn/iotcp/logger");
			}
			try {
				mqttSendSevice.send(this.getClass(), "info", "[mqtt]*******************延时："+requestTime + "毫秒 **********************", "$forallcn/iotcp/logger");
				Thread.sleep(requestTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		mqttSendSevice.send(this.getClass(), "info", "[mqtt]客户端 " + this.address + " 中断！1、检查串口转换器的连接超时时间不小于设备轮询时间；2、设备轮询时间不小于平台通讯心跳时间30分钟", "$forallcn/iotcp/logger");
		this.session = null;
	}

	/**
	 * 加载SystemService，堵塞
	 *
	 * @return
	 */
	private <T> T contextGetBeen(Class<T> requiredType) {
		do {
			try {
				Thread.sleep(1000 * 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (ApplicationContextUtil.getContext() == null);
		return ApplicationContextUtil.getContext().getBean(requiredType);
	}

	/**
	 * 根据不同数据类型，发送数据
	 *
	 * @param session
	 * @param cmdDataType
	 * @param cmd
	 */
	private void writeData(IoSession session, String cmdDataType, String cmd) {
		switch (cmdDataType) {
			case "string":  // 字符串，原样输出
				session.write(cmd);
				break;
			case "byte":  // 二进制
				byte[] b = HexConvertUtil.hexStringToBytes(cmd);
				System.out.println(Arrays.toString(b));
				session.write(IoBuffer.wrap(b));
				break;
			case "bit":
				break;
			default:
				break;
		}
	}
}
