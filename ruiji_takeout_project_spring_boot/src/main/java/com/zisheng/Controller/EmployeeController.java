package com.zisheng.Controller;

import com.zisheng.MyUtils.JWTUtils;
import com.zisheng.Pojo.Employee;
import com.zisheng.Pojo.Result;
import com.zisheng.Service.EmployeeService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);// 创建日志记录对象，便于记录日志
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private EmployeeService employeeService;

    /**
     * 登录功能
     * @param httpServletRequest
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public Result registerFun(HttpServletRequest httpServletRequest, @RequestBody  Employee employee)
    {
        log.info("接收到的数据为" + employee);
        // 获取页面传递过来的密码
        String pw = employee.getPassword();
        // 调用DigestUtils工具类的md5DigestAsHex方法将页面提交的密码进行md5加密处理
        String encryptPw = DigestUtils.md5DigestAsHex(pw.getBytes());
        log.info("加密后的密码：" + encryptPw);
        // 设置密码为加密之后的密码
        employee.setPassword(encryptPw);
        // 调用Service层进行查询数据库中的数据
        Employee employee1 = employeeService.login(employee);
        if(employee1 != null) {
            // 说明用户存在，查看用户是否处于禁用状态
            if(employee1.getStatus() == 0) return Result.error("账户已经禁用,登陆失败！");
            // 生成JWT令牌代码
//            Map<String,Object> map = new HashMap<>();// 设置自定义的一些信息
//            map.put("id",employee1.getId());
//            map.put("name",employee1.getName());
//            map.put("username",employee1.getUsername());
//            String jwt = jwtUtils.generateJWT(map);// 生成JWT令牌
            // 说明账户存在并且可用,下面将用户ID存入Session，并且返回正确的结果.通过HttpServletRequest对象进行设置
            httpServletRequest.getSession().setAttribute("employee",employee1.getId());
            // 将对象转换为JSON格式的字符串响应给前端
            return Result.success(employee1);
        }
        else return Result.error("账号或密码错误！！！");
    }
    /**
     * 退出功能
     */
    @PostMapping("/logout")
    public Result logout(HttpServletRequest httpServletRequest)
    {
        // 将保存在session中的id移出
        httpServletRequest.getSession().removeAttribute("employee");
        return Result.success();
    }
}