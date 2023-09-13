package com.zisheng.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zisheng.Mapper.ShortMessageMapper;
import com.zisheng.Pojo.User;
import com.zisheng.Service.ShortMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShortMessageServiceImpl extends ServiceImpl<ShortMessageMapper, User> implements ShortMessageService {
    private static final Logger log = LoggerFactory.getLogger(ShortMessageService.class);
    @Autowired
    private ShortMessageMapper shortMessageMapper;

    /**
     * 判断手机号是否存储
     * @param phoneNumber
     * @return
     */
    @Override
    public User exists(String phoneNumber) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(phoneNumber != null,User::getPhone,phoneNumber);
        return  shortMessageMapper.selectOne(lambdaQueryWrapper);
    }
}
