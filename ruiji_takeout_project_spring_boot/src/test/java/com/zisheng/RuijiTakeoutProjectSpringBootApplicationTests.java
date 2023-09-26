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
import java.util.concurrent.TimeUnit;

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
        // 调用redisTemplate对象的opsForValue方法，获取操作String类型数据的对象
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        // 设置键为name的值为张三
        valueOperations.set("name","zhangSan");
        // 获取键为name的值
        String name = (String) valueOperations.get("name");
        log.info("name-->" + name);
        // 获取集合当中的所有键
        Set<String> keys = redisTemplate.keys("*");
        if(!ObjectUtils.isEmpty(keys)) keys.forEach(log::info);
    }
    /**
     * 设置存储在redis中的key和值（String类型），并且设置有效期
     */
    @Test
    public void test_String_data()
    {
        log.info("向redis中存储String类型键值对数据，并且设置截止时间");
        // 获取操作String类型数据的对象
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        // 设置有效期和截止时间，一旦超过了截止时间，便会自动删除该键
        valueOperations.set("name","张三",5, TimeUnit.MINUTES);
    }
}
