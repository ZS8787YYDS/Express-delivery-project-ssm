package com.zisheng.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zisheng.Mapper.DishMapper;
import com.zisheng.Pojo.DishPagingResult;
import com.zisheng.Pojo.Dish;
import com.zisheng.Service.DishService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    private static final Logger log = LoggerFactory.getLogger(DishServiceImpl.class);
    @Autowired
    private DishMapper dishMapper;

    /**
     * 分页查询功能
     *
     * @param page
     * @param pageSize
     * @return
     */
    // 使用PageHelper分页插件
//    @Override
//    public DishPagingResult DishPaging(Integer page, Integer pageSize,String name) {
//        PageHelper.startPage(page,pageSize);
//        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.likeRight(name != null,"name",name);
//        List<Dish> dishes = dishMapper.selectList(queryWrapper);
//        com.github.pagehelper.Page<Dish> page1 = (com.github.pagehelper.Page<Dish>) dishes;
//        return new DishPagingResult(page1.getTotal(),page1.getResult());
//    }
    // 使用MybatisPlus分页插件
    @Override
    public DishPagingResult DishPaging(Integer page, Integer pageSize,String name) {
        IPage iPage = new Page(page,pageSize);
        QueryWrapper<Dish> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight(name != null,"name",name);
        dishMapper.selectPage(iPage,queryWrapper);
        return new DishPagingResult(iPage.getTotal(),iPage.getRecords());
    }

    /**
     * 停售商品
     * @param ids
     * @param status
     */
    @Override
    public void pauseSell(Long ids,Integer status) {
        Dish dish = new Dish();
        dish.setId(ids);
        dish.setStatus(status);
        dishMapper.updateById(dish);
    }

    /**
     * 删除菜品
     * @param ids
     */
    @Override
    public void deleteDish(Long ids) {
        dishMapper.deleteById(ids);
    }

    /**
     * 批量起售功能
     * @param status
     * @param ids
     */
    @Override
    public void startSell(Integer status, List<Long> ids) {
        List<Dish> dishes = dishMapper.selectBatchIds(ids);
        for(Dish dish : dishes)
        {
            dish.setStatus(status);
            dishMapper.updateById(dish);
        }
    }

    /**
     * 批量删除功能
     * @param ids
     */
    @Override
    public void deleteBatch(List<Long> ids) {
        dishMapper.deleteBatchIds(ids);
    }
}
