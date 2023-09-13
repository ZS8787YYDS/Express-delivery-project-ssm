package com.zisheng.MyUtils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;

/**
 * 短信发送工具类
 */
public class SMSUtils {

	/**
	 * 发送短信
	 * @param signName 签名
	 * @param templateCode 模板
	 * @param phoneNumbers 手机号
	 * @param param 参数
	 */
	public static void sendMessage(String signName, String templateCode,String phoneNumbers,String param){
		// 创建DefaultProfile对象，设置地域节点，密钥id，密钥密码
		DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "", "");
		// 创建IAcsClient对象，封装DefaultProfile对象
		IAcsClient client = new DefaultAcsClient(profile);
		// 创建SendRequest对象，
		SendSmsRequest request = new SendSmsRequest();
		// 设置地域节点id
		request.setSysRegionId("cn-hangzhou");
		// 设置手机号
		request.setPhoneNumbers(phoneNumbers);
		// 设置签名，代表发送方的唯一标识
		request.setSignName(signName);
		// 设置模板，代表模板的内容
		request.setTemplateCode(templateCode);
		// 设置映射关系，code的值为param对应的值
		request.setTemplateParam("{\"code\":\""+param+"\"}");
		try {
			// 调用IAclClient方法发送短信，返回SendSmsResponse对象
			SendSmsResponse response = client.getAcsResponse(request);
			System.out.println("短信发送成功");
		}catch (ClientException e) {
			e.printStackTrace();
		}
	}

}
