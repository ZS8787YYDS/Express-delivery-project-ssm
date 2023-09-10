package com.zisheng.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zisheng.Mapper.EmployeeMapper;
import com.zisheng.Pojo.Employee;
import com.zisheng.Pojo.PageResult;
import com.zisheng.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.management.Query;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 登录功能
     * @param employee
     * @return
     */
    @Override
    public Employee login(Employee employee) {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(employee.getUsername() != null,
                Employee::getUsername,employee.getUsername()).eq(
                        employee.getPassword() != null,Employee::getPassword,employee.getPassword());
        return employeeMapper.selectOne(queryWrapper);
    }

    /**
     * 插入功能
     * @param employee
     * @return
     */
    @Override
    public int isnertEmp( Employee employee) {
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        int insert = employeeMapper.insert(employee);
        return insert;
    }


    /**
     * 分页功能
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    // 分页查询方法一：采用pageHelper分页插件
//    @Override
//    public PageResult paging(Integer page, Integer pageSize,String name) {
//        PageHelper.startPage(page,pageSize);
//        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
//        queryWrapper.likeRight(name != null,"name",name);
//        List<Employee> employees = employeeMapper.selectList(queryWrapper);
//        Page<Employee> pages = (Page<Employee>) employees;
//        PageResult pageResult = new PageResult(pages.getTotal(),pages.getResult());
//        return pageResult;
//    }
    // 分页查询方法二：使用MybatisPlus进行分页查询
    @Override
    public PageResult paging(Integer page, Integer pageSize, String name) {
        IPage iPage = new Page(page,pageSize);
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight(name != null,"name",name);
        employeeMapper.selectPage(iPage,queryWrapper);
        PageResult pageResult = new PageResult(iPage.getTotal(),iPage.getRecords());
        return pageResult;
    }

    /**
     * 查询功能
     * @param id
     */
    @Override
    public Employee searchEmp(Long id) {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        Employee employee = employeeMapper.selectOne(queryWrapper);
        return employee;
    }

    /**
     * 修改员工
     * @param employee
     */
    @Override
    public void modifyEmp(Employee employee,Long updateId) {
        employee.setUpdateUser(updateId);
        employee.setUpdateTime(LocalDateTime.now());
        employeeMapper.updateById(employee);
    }
}
