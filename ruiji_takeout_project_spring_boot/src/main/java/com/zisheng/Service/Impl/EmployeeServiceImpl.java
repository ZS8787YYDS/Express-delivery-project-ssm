package com.zisheng.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zisheng.Mapper.EmployeeMapper;
import com.zisheng.Pojo.Employee;
import com.zisheng.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.Query;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;
    @Override
    public Employee login(Employee employee) {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(employee.getUsername() != null,
                Employee::getUsername,employee.getUsername()).eq(
                        employee.getPassword() != null,Employee::getPassword,employee.getPassword());
        return employeeMapper.selectOne(queryWrapper);
    }
}
