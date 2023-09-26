package com.zisheng.Controller;

import com.zisheng.MyUtils.ValidateCodeUtils;
import com.zisheng.Pojo.Result;
import com.zisheng.Pojo.User;
import com.zisheng.Service.ShortMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController// 将对象转换成JSON格式的字符串响应给前端浏览器
@RequestMapping("/user") // 设置公共路径
public class ShortMessageController {
    private static final Logger log = LoggerFactory.getLogger(ShortMessageController.class);
    @Resource
    private ShortMessageService shortMessageService;
    @Resource
    private  RedisTemplate<String,Object> redisTemplate;
    /**
     * 登录功能
     * 由于接收的数据是键值对数据，因此可以采用一个Map集合来接收，加上RequestBody注解也是可以正常封装的
     * @param map
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody Map<String,String> map, HttpServletRequest httpServletRequest)
    {
        log.info("接收到的数据为：{}",map.toString());
        // 手机号
        String phoneNumber = map.get("phone");
        // 验证码，这里我直接自定义了验证码
        String validateCode  = "8787";
        // 如果手机号不为空的话
        if(phoneNumber != null)
        {
            // 生成一个4位的验证码
            Integer code = ValidateCodeUtils.generateValidateCode(4);
            log.info("生成的验证码为：{}",code.toString());
//            // 将验证码保存在Session中
//            httpServletRequest.getSession().setAttribute(phoneNumber,validateCode);
            // 将生成的验证码缓存在redis当中,设置有效期为五分钟
            redisTemplate.opsForValue().set(phoneNumber,"8787",5, TimeUnit.MINUTES);
            // 如果说验证码比对成功的话
            if(validateCode != null && validateCode.equals(redisTemplate.opsForValue().get(phoneNumber))) {
                // 登陆成功
                // 判断手机号是否存在，如果不存在的话，进行注册一下
                User user = shortMessageService.exists(phoneNumber);
                // 不存在，注册一下，创建一个用户对象
                if (ObjectUtils.isEmpty(user)) {
                    // 新用户，注册一下
                    user = new User();
                    user.setPhone(phoneNumber);
                    user.setStatus(1);
                    // 将该用户的信息保存的数据库表中
                    shortMessageService.save(user);
//                    // 登陆成功之后将用户的id保存在session当中
//                    httpServletRequest.getSession().setAttribute("user",user1.getId());
//                    return Result.success(user1);
                }
                // 登录成功之后将redis中缓存的验证码删除
                redisTemplate.delete(phoneNumber);
                // 将登录成功之后的id保存在Session当中
                httpServletRequest.getSession().setAttribute("user",user.getId());
                // 将用户的信息返回给前端
                return Result.success(user);
            }
        }
        return Result.error("登陆失败！");
    }

    /**
     * 退出功能
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/loginout")
    public Result loginOut(HttpServletRequest httpServletRequest)
    {
        httpServletRequest.getSession().removeAttribute("user");
        return Result.success("退出成功！！！");
    }

}
