package com.zisheng.Controller;

import com.zisheng.MyUtils.ValidateCodeUtils;
import com.zisheng.Pojo.Result;
import com.zisheng.Pojo.User;
import com.zisheng.Service.ShortMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class ShortMessageController {
    private static final Logger log = LoggerFactory.getLogger(ShortMessageController.class);
    @Autowired
    private ShortMessageService shortMessageService;
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
            // 将验证码保存在Session中
            httpServletRequest.getSession().setAttribute(phoneNumber,validateCode);
            // 如果说验证码比对成功的话
            if(validateCode.equals(httpServletRequest.getSession().getAttribute(phoneNumber))) {
                // 登陆成功
                // 判断手机号是否存在，如果不存在的话，进行注册一下
                User user = shortMessageService.exists(phoneNumber);
                // 不存在，注册一下，创建一个用户对象，插入到数据库表当中
                if (user == null) {
                    // 新用户，注册一下
                    User user1 = new User();
                    user1.setPhone(phoneNumber);
                    user1.setStatus(1);
                    shortMessageService.save(user1);
                    // 登陆成功之后将用户的id保存在session当中
                    httpServletRequest.getSession().setAttribute("user",user1.getId());
                    return Result.success(user1);
                }
                // 将登录成功之后的id保存在Session当中
                httpServletRequest.getSession().setAttribute("user",user.getId());
                // 将用户的信息返回给前端
                return Result.success(user);
            }
        }
        return Result.error("登陆失败！");
    }
}
