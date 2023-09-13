package com.zisheng.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zisheng.Pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShortMessageMapper extends BaseMapper<User> {
}
