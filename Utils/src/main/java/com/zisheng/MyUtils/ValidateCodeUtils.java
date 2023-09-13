package com.zisheng.MyUtils;

import java.util.Random;

/**
 * 随机生成验证码工具类
 */
public class ValidateCodeUtils {
    /**
     * 随机生成验证码
     * @param length 长度为4位或者6位
     * @return
     */
    public static Integer generateValidateCode(int length){
        Integer code =null;
        if(length == 4){
            code = new Random().nextInt(9000) + 1000;//生成随机数，最大为9999
        }else if(length == 6){
            code = new Random().nextInt(900000) + 100000;//生成随机数，最大为999999
        }else{
            throw new RuntimeException("只能生成4位或6位数字验证码");
        }
        return code;
    }

    /**
     * 随机生成指定长度字符串验证码
     * @param length 长度
     * @return
     */
    public static String generateValidateCode4String(int length){
        // 创建随机数对象
        Random rdm = new Random();
        // 将生成的验证码转换成字符串
        String hash1 = Integer.toHexString(rdm.nextInt());
        // 从头开始截取,截取长度为length的字符串
        String capstr = hash1.substring(0, length);
        return capstr;
    }
}
