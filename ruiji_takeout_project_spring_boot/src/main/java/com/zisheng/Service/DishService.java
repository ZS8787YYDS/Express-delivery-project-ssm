package com.zisheng.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zisheng.Dto.DishDto;
import com.zisheng.Pojo.DishPagingResult;
import com.zisheng.Pojo.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    DishPagingResult DishPaging(Integer page, Integer pageSize,String name);

    void pauseSell(Long ids,Integer status);

    void deleteDish(Long ids);

    void startSell(Integer status, List<Long> dishes);

    void deleteBatch(List<Long> ids);

    void insertDish(DishDto dishDto);

    void updateDishData(DishDto dishDto);

    DishDto searchDishes(Long id);

    List<Dish> searchDishes_02(Long categoryId);
}
