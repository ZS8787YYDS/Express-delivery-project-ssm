package com.zisheng.Controller;

import com.zisheng.MyUtils.JWTUtils;
import com.zisheng.Pojo.Employee;
import com.zisheng.Pojo.Result;
import com.zisheng.Service.EmployeeService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("/login")
    public Result registerFun(@RequestBody  Employee employee)
    {
        log.info("接收到的数据为" + employee);
        Employee employee1 = employeeService.login(employee);
        if(employee1 != null) {
            log.info("11");
            Map<String,Object> map = new HashMap<>();
            map.put("id",employee1.getId());
            map.put("name",employee1.getName());
            map.put("username",employee1.getUsername());
            log.info("122321");
            String jwt = jwtUtils.generateJWT(map);
            return Result.success(jwt);
        }
        else return Result.error("登陆失败！！！");
    }

}
