package com.zisheng.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zisheng.Pojo.Employee;

public interface EmployeeService extends IService<Employee> {
    Employee login(Employee employee);
}
