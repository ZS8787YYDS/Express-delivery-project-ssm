package com.zisheng.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zisheng.Pojo.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * 持久层
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
