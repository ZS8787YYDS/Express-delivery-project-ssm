package com.zisheng.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zisheng.Pojo.Employee;
import com.zisheng.Pojo.PageResult;

public interface EmployeeService extends IService<Employee> {
    /**
     * 登陆操作
     * @param employee
     * @return
     */
    Employee login(Employee employee);

    /**
     * 插入操作
     * @param employee
     * @return
     */

    int isnertEmp(Employee employee);

    /**
     * 分页操作
     * @param page
     * @param pageSize
     * @param name
     * @return
     */

    PageResult paging(Integer page, Integer pageSize,String name);

    /**
     * 查询操作
     * @param id
     * @return
     */

    Employee searchEmp(Long id);

    /**
     * 修改操作
     * @param employee
     * @param updateId
     */

    void modifyEmp(Employee employee,Long updateId);
}
