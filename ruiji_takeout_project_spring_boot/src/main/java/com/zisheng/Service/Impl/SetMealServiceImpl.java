package com.zisheng.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zisheng.Controller.SetMealController;
import com.zisheng.Dto.SetMealDto;
import com.zisheng.Mapper.SetMealMapper;
import com.zisheng.Mapper.SetmealDIshMapper;
import com.zisheng.Pojo.SetMealPagingResult;
import com.zisheng.Pojo.Setmeal;
import com.zisheng.Pojo.SetmealDish;
import com.zisheng.Service.SetMealService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 业务逻辑层
 */
@Service
public class SetMealServiceImpl extends ServiceImpl<SetMealMapper, Setmeal> implements SetMealService {
    private static final Logger log = LoggerFactory.getLogger(SetMealController.class);
    @Autowired
    private SetMealMapper setMealMapper;

    @Autowired
    private SetmealDIshMapper setmealDIshMapper;
    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    // 第一种方式:采用PageHelper分页插件进行解决
//    @Override
//    public SetMealPagingResult setMealPaging(Integer page, Integer pageSize,String name) {
//        PageHelper.startPage(page,pageSize);
//        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.likeRight(name != null,Setmeal::getName,name);
//        List<Setmeal> setmeals = setMealMapper.selectList(lambdaQueryWrapper);
//        Page<Setmeal> pages = (Page<Setmeal>) setmeals;
//        return new SetMealPagingResult(pages.getTotal(),pages.getResult());
//    }
    // 第二种方式:采用MybatisPlus提供的分页拦截器进行处理
    @Override
    public SetMealPagingResult setMealPaging(Integer page, Integer pageSize, String name) {
        IPage page1 = new Page(page,pageSize);
        // 创建条件对象
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.likeRight(name != null,Setmeal::getName,name);
        setMealMapper.selectPage(page1, lambdaQueryWrapper);
        return new SetMealPagingResult(page1.getTotal(),page1.getRecords());
    }

    /**
     * 插入套餐功能
     * @param setMealDto
     */
    @Override
    @Transactional
    public void insertSetmeal(SetMealDto setMealDto) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setMealDto,setmeal);
        setMealMapper.insert(setmeal);
        List<SetmealDish> setmealDishes = setMealDto.getSetmealDishes();
        List<SetmealDish> collect = setmealDishes.stream().map(o -> {
            o.setSetmealId(setmeal.getId());
            return o;
        }).collect(Collectors.toList());
        collect.stream().forEach(o -> {
            setmealDIshMapper.insert(o);
        });
    }

    /**
     * 查询套餐功能
     * @param id
     * @return
     */
    @Override
    public SetMealDto searchSetmeal(Long id) {
        Setmeal setmeal = setMealMapper.selectById(id);

        SetMealDto setMealDto = new SetMealDto();
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(id != null,SetmealDish::getSetmealId,id);
        List<SetmealDish> setmealDishes = setmealDIshMapper.selectList(lambdaQueryWrapper);
        setMealDto.setSetmealDishes(setmealDishes);
        BeanUtils.copyProperties(setmeal,setMealDto);
        return setMealDto;
    }

    /**
     * 修改套餐功能
     * @param setMealDto
     */
    @Override
    @Transactional
    public void updateSetmeal(SetMealDto setMealDto) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setMealDto,setmeal);
        setMealMapper.updateById(setmeal);
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(setMealDto.getId() != null,SetmealDish::getSetmealId,setMealDto.getId());
        setmealDIshMapper.delete(lambdaQueryWrapper);
        List<SetmealDish> setmealDishes = setMealDto.getSetmealDishes();
        if(setmealDishes != null)
        {
            setmealDishes.stream().map(o -> {
                o.setSetmealId(setMealDto.getId());
                return o;
            }).collect(Collectors.toList());
            setmealDishes.stream().forEach(o -> {
                setmealDIshMapper.insert(o);
            });
        }
    }

    /**
     * 批量起售功能
     * @param ids
     */
    @Override
    public void startSell(Integer status,List<Long> ids) {
        ids.stream().forEach(o -> {
            Setmeal setmeal = setMealMapper.selectById(o);
            setmeal.setStatus(status);
            setMealMapper.updateById(setmeal);
        });
    }

    /**
     * 批量删除功能
     * @param ids
     */
    @Override
    public void deleteSetmeals(List<Long> ids) {
        ids.stream().forEach(o -> {
            setMealMapper.deleteById(o);
        });
    }

    /**
     * 查询套餐功能
     * @param categoryId
     * @param status
     * @return
     */
    @Override
    public List<Setmeal> findSetmeals(Long categoryId, Integer status) {
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(categoryId != null,Setmeal::getCategoryId,categoryId)
                        .eq(status != null,Setmeal::getStatus,status);
         return  setMealMapper.selectList(lambdaQueryWrapper);
    }
}
