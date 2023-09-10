package com.zisheng.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zisheng.Pojo.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * 持久层
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}
