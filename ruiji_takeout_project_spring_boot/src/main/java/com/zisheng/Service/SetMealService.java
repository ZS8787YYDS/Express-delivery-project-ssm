package com.zisheng.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zisheng.Dto.SetMealDto;
import com.zisheng.Pojo.SetMealPagingResult;
import com.zisheng.Pojo.Setmeal;

import java.util.List;

public interface SetMealService extends IService<Setmeal> {
    SetMealPagingResult setMealPaging(Integer page, Integer pageSize,String name);

    void insertSetmeal(SetMealDto setMealDto);

    SetMealDto searchSetmeal(Long id);

    void updateSetmeal(SetMealDto setMealDto);

    void startSell(Integer status,List<Long> ids);

    void deleteSetmeals(List<Long> ids);
}
