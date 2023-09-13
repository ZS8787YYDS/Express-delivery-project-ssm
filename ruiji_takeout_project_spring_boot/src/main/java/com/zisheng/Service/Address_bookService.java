package com.zisheng.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zisheng.Pojo.AddressBook;

import java.util.List;

/**
 * 业务层接口
 */
public interface Address_bookService extends IService<AddressBook> {
    /**
     * 查询多个地址簿信息
     * @return
     */
    List<AddressBook> searcAddress();

    /**
     * 插入地址簿信息
     * @param addressBook
     */
    void insertAddress_book(AddressBook addressBook);

    /**
     * 查询单个地址簿信息
     * @param id
     * @return
     */
    AddressBook findOne(Long id);

    /**
     * 更新地址簿信息
     * @param addressBook
     */
    void updateAddress_book(AddressBook addressBook);

    /**
     * 删除地址簿信息
     * @param ids
     */
    void deleteAddress_book(Long ids);

    /**
     * 设置默认地址
     * @param addressBook
     */
    void setDefaultValue(AddressBook addressBook);
}
