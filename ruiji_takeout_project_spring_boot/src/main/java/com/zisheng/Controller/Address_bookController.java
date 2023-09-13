package com.zisheng.Controller;

import com.zisheng.Pojo.AddressBook;
import com.zisheng.Pojo.Result;
import com.zisheng.Service.Address_bookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/addressBook")
public class Address_bookController {
    @Autowired
    private Address_bookService addressBookService;

    /**
     * 查询多个地址簿功能
     * @return
     */
    @GetMapping("/list")
    public Result searchAddress()
    {
        List<AddressBook> addressBooks = addressBookService.searcAddress();
        return Result.success(addressBooks);
    }

    /**
     * 插入地址簿功能
     * @param addressBook
     * @return
     */
    @PostMapping
    public Result insertAddress_book(@RequestBody AddressBook addressBook)
    {
        log.info("接收到的信息：{}",addressBook.toString());
        addressBookService.insertAddress_book(addressBook);
        return Result.success();
    }

    /**
     * 查询单个地址簿信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Long id)
    {
        log.info("接收到的信息为: {}",id);
        AddressBook addressBook =  addressBookService.findOne(id);
        return Result.success(addressBook);
    }

    /**
     * 修改地址簿信息
     * @param addressBook
     * @return
     */
    @PutMapping
    public Result updateAddress_book(@RequestBody AddressBook addressBook)
    {
        log.info("接收到的数据为：{}",addressBook);
        addressBookService.updateAddress_book(addressBook);
        return Result.success();
    }

    /**
     * 删除地址簿信息
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result deleteAddress_book( Long ids)
    {
        log.info("接收到的信息为：{}",ids.toString());
        addressBookService.deleteAddress_book(ids);
        return Result.success();
    }

    /**
     * 设置默认地址值
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    public Result setDefaultValue(@RequestBody AddressBook addressBook)
    {
        log.info("接受到的数据为：{}",addressBook);
        addressBookService.setDefaultValue(addressBook);
        return Result.success();
    }
}
