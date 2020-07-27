package org.jeecgframework.jwt.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.UserUtils;
import org.jeecgframework.jwt.model.loginTokenEntity;
import org.jeecgframework.jwt.service.TokenManager;
import org.jeecgframework.jwt.util.ResponseMessage;
import org.jeecgframework.jwt.util.Result;
import org.jeecgframework.jwt.util.menu.ResponseMessageCodeEnum;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.pojo.base.TSUserOrg;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 获取和删除token的请求地址， 在Restful设计中其实就对应着登录和退出登录的资源映射
 *
 * @author scott
 * @date 2015/7/30.
 */
@Api(value = "token", description = "鉴权token接口", tags = "tokenAPI")
@Controller
@RequestMapping("/tokens")
public class TokenController {
    private static final Logger logger = Logger.getLogger(TokenController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private SystemService systemService;

    @ApiOperation(value = "获取TOKEN", notes = "根据用户名和密码获取token信息", httpMethod = "POST", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage<?> login(@RequestParam String username, @RequestParam String password) {
        logger.info("获取TOKEN[{}]" + username);
        // 验证
        if (StringUtils.isEmpty(username)) {
            return Result.error(ResponseMessageCodeEnum.ERROR, "用户账号不能为空!", "用户账号不能为空!");
            // return new ResponseEntity("用户账号不能为空!", HttpStatus.NOT_FOUND);
        }
        // 验证
        if (StringUtils.isEmpty(username)) {
            return Result.error(ResponseMessageCodeEnum.ERROR, "用户账号不能为空!", "用户账号不能为空!");
            // return new ResponseEntity("用户密码不能为空!", HttpStatus.NOT_FOUND);
        }
        Assert.notNull(username, "username can not be empty");
        Assert.notNull(password, "password can not be empty");

        TSUser user = userService.checkUserExits(username, password);
        if (user == null) {
            // 提示用户名或密码错误
            logger.info("获取TOKEN,户账号密码错误[{}]" + username);
            return Result.error(ResponseMessageCodeEnum.ERROR, "用户账号密码错误!", "用户账号密码错误!");
            // return new ResponseEntity("用户账号密码错误!", HttpStatus.NOT_FOUND);
        }
        // 生成一个token，保存用户登录状态
        String token = tokenManager.createToken(user);
        loginTokenEntity lt = new loginTokenEntity();
        lt.setUsername(username);
        lt.setRealname(UserUtils.getUserRealnameByUsername(username));
        List<TSUserOrg> orgs = user.getUserOrgList();
        lt.setDepartcode(orgs == null ? "" : orgs.get(0).getTsDepart().getOrgCode());
        lt.setDepartname(orgs == null ? "" : orgs.get(0).getTsDepart().getDepartname());
        lt.setToken(token);
        systemService.addAppLog("获取token成功", Globals.Log_Leavel_INFO, Globals.Log_Type_LOGIN, user);
        return Result.success(ResponseMessageCodeEnum.SUCCESS, "获取token成功!", lt);
    }

    @ApiOperation(value = "销毁TOKEN")
    @RequestMapping(value = "/{username}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseMessage<?> logout(
            @ApiParam(name = "username", value = "用户账号", required = true) @PathVariable("username") String username) {
        System.out.println("销毁TOKEN");
        logger.info("deleteToken[{}]" + username);
        // 验证
        if (StringUtils.isEmpty(username)) {
            return Result.error("用户账号，不能为空!");
        }
        try {
            tokenManager.deleteToken(username);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("销毁TOKEN失败");
        }
        systemService.addAppLog("销毁TOKEN成功", Globals.Log_Leavel_INFO, Globals.Log_Type_LOGIN, UserUtils.getUserByUsername(username));
        return Result.success();
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @throws Exception
     *
     */
    private String get(String url) throws Exception {
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            String yanCode = "8888";
            String str = "";
            String smsStr = "【泰安消防】您的短信验证码是" + yanCode + "，请在5分钟内按页面提示提交验证码";
            try {
                str = "http://sms.20080531.com/sendsms.php?cellnumber=15953328385&content="
                        + URLEncoder.encode(smsStr, "utf-8") + "&smsuserid=26&sing=af4f3eb7dfbbcc05e29c4fd72d425782";
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
            }
            String url = new String(str);
            System.out.println(new TokenController().get(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
