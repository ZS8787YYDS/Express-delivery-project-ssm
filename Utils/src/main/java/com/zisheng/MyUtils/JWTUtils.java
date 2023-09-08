package com.zisheng.MyUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JWTUtils {
    // 设置签名算法
    private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    @Value("${signKey}")//通过注解的形式引入配置文件中的key为signKey的值，赋值给该变量
    private String signKey;
    private Long endTime =   3600 * 1000 * 24L;
    //生成JWT令牌
    public String generateJWT(Map<String,Object> map)
    {
        return Jwts.builder()// 获得JWTBuilder对象
                .signWith(signatureAlgorithm,signKey) //设置签名算法和密钥
                .setClaims(map)// 设置有效载荷
                .setExpiration(new Date(System.currentTimeMillis() + endTime))// 设置截止日期
                .compact();
//        return "啥也不是";
    }
    /*解析JWT令牌*/
    public Claims parseJWT(String JWTStr)
    {
        Claims claims = Jwts.parser()// 获得parser解析器对象
                .setSigningKey(signKey)// 设置密钥
                .parseClaimsJws(JWTStr) // 解析JWT字符串
                .getBody();// 获取解析的结果
        return claims;
    }
}
