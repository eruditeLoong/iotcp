package com.jeecg.device.utils;

import org.jeecgframework.core.util.ApplicationContextUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeecg.device.entity.BaseDeviceEntity;
import com.jeecg.device.service.BaseDeviceServiceI;

public class BaseDeviceUtil {

	@Autowired
	private static BaseDeviceServiceI baseDeviceService;

	public static String getBaseDeviceNameById(String id) {
		baseDeviceService = ApplicationContextUtil.getContext().getBean(BaseDeviceServiceI.class);
		BaseDeviceEntity device = baseDeviceService.get(BaseDeviceEntity.class, id);
		if (device != null) {
			return device.getName();
		}
		return "";
	}
}
