package com.zisheng.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zisheng.Pojo.User;

public interface ShortMessageService extends IService<User> {
    User exists(String phoneNumber);
}
