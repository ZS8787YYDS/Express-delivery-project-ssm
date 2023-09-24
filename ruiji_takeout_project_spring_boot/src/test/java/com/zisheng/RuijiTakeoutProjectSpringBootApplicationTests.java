package com.zisheng;

import com.zisheng.MyUtils.JWTUtils;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SpringBootTest
class RuijiTakeoutProjectSpringBootApplicationTests {
    private static final Logger log = LoggerFactory.getLogger(RuijiTakeoutProjectSpringBootApplication.class);
    @Autowired
    private JWTUtils jwtUtils;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    private
    @Test
    void contextLoads() {
        System.out.println("啥也不是吊毛");
    }
    @Test
    void testJWT()
    {
        Map<String,Object> map = new HashMap<>();
        map.put("张三",12);
        map.put("李四",13);
        map.put("张五",14);
        String JWt = jwtUtils.generateJWT(map);
       log.info("生成的JWT令牌为"  + JWt);
        Claims claims = jwtUtils.parseJWT(JWt);
        log.info("解析的结果为：" + claims);
    }

    /**
     * 测试redis
     */
    @Test
    public void test_redis()
    {
        log.info("测试redis----------------");
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("name","zhangSan");
        String name = (String) valueOperations.get("name");
        log.info("name-->" + name);
        Set<String> keys = redisTemplate.keys("*");
        if(!ObjectUtils.isEmpty(keys)) keys.forEach(log::info);
    }
}
