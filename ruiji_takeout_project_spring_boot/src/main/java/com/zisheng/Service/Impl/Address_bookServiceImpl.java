package com.zisheng.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zisheng.Mapper.Address_bookMapper;
import com.zisheng.MyUtils.ThreadUtils;
import com.zisheng.Pojo.AddressBook;
import com.zisheng.Service.Address_bookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务逻辑层
 */
@Service
public class Address_bookServiceImpl extends ServiceImpl<Address_bookMapper, AddressBook> implements Address_bookService {
    @Autowired
    private Address_bookMapper addressBookMapper;

    /**
     * 查询地址簿功能
     * @return
     */
    @Override
    public List<AddressBook> searcAddress() {
        LambdaQueryWrapper<AddressBook> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AddressBook::getUserId,ThreadUtils.getThreadLocal());
        return addressBookMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 新增地址簿功能
     * @param addressBook
     */
    @Override
    public void insertAddress_book(AddressBook addressBook) {
        addressBook.setUserId(ThreadUtils.getThreadLocal());
        addressBookMapper.insert(addressBook);
    }

    /**
     * 查询单个地址簿信息
     * @param id
     * @return
     */
    @Override
    public AddressBook findOne(Long id) {
        AddressBook addressBook = addressBookMapper.selectById(id);
        return addressBook;
    }

    /**
     * 修改地址簿功能
     * @param addressBook
     */
    @Override
    public void updateAddress_book(AddressBook addressBook) {
        addressBookMapper.updateById(addressBook);
    }

    /**
     * 删除地址簿信息
     * @param ids
     */
    @Override
    public void deleteAddress_book(Long ids) {
        addressBookMapper.deleteById(ids);
    }

    /**
     * 设置默认地址
     * @param addressBook
     */
    @Override
    public void setDefaultValue(AddressBook addressBook) {
        List<AddressBook> addressBooks = addressBookMapper.selectList(null);
        addressBooks.stream().forEach( o -> {
            o.setIsDefault(0);
            addressBookMapper.updateById(o);
        });
        addressBook.setIsDefault(1);
        QueryWrapper<AddressBook> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(addressBook.getId() != null,AddressBook::getId,addressBook.getId());
        addressBookMapper.update(addressBook,queryWrapper);
    }

    /**
     * 获取默认地址
     * @return
     */
    @Override
    public AddressBook getDefault() {
        LambdaQueryWrapper<AddressBook> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AddressBook::getIsDefault,1).eq(AddressBook::getUserId,ThreadUtils.getThreadLocal());
        return  addressBookMapper.selectOne(lambdaQueryWrapper);
    }
}
