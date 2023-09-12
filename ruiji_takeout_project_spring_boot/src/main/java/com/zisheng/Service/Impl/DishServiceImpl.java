package com.zisheng.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zisheng.Dto.DishDto;
import com.zisheng.Mapper.DishFlavorMapper;
import com.zisheng.Mapper.DishMapper;
import com.zisheng.Pojo.DishFlavor;
import com.zisheng.Pojo.DishPagingResult;
import com.zisheng.Pojo.Dish;
import com.zisheng.Service.DishService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    private static final Logger log = LoggerFactory.getLogger(DishServiceImpl.class);
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
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

    /**
     * 插入功能
     * @param dishDto
     */
    @Transactional// 将该方法交给String进行事务管理
    @Override
    public void insertDish(DishDto dishDto) {
        Dish dish = new Dish();
        // 将dishDto对象的属性值复制到dish对象当中，注意：只会复制类型和名称都相同的属性
        BeanUtils.copyProperties(dishDto,dish);
        // 将菜品信心插入到数据库表中
        dishMapper.insert(dish);
        // 获取该菜品的口味信息
        List<DishFlavor> flavors = dishDto.getFlavors();
        // 将该菜品的口味信息插入到数据库表中
        for(DishFlavor dishFlavor: flavors)
        {
            // 插入之前设置一下该口味是属于哪个菜品
            dishFlavor.setDishId(dish.getId());
            dishFlavorMapper.insert(dishFlavor);
        }
    }

    /**
     * 修改功能
     * @param dishDto
     */
    @Override
    // 将该方法交给Spring进行事务管理，方法执行之前开始事务，方法成功执行结束之后提交事务，一旦出现异常，回滚事务
    // 保证数据库中数据的一致性
    @Transactional
    public void updateDishData(DishDto dishDto) {
         // 先根据菜品ID将数据库表中该菜品口味的数据进行删除
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dishDto.getId() != null,DishFlavor::getDishId,dishDto.getId());
        dishFlavorMapper.delete(queryWrapper);
        // 如果该菜品的口味存在的话
        if(dishDto.getFlavors() != null)
        {
            // 遍历修改后的菜品口味对象，将该菜品的口味插入到数据库中
            for (DishFlavor flavor : dishDto.getFlavors())
            {
                // 设置当前口味是属于哪个菜品
                flavor.setDishId(dishDto.getId());
                // 将该菜的口味插入到数据库中
                dishFlavorMapper.insert(flavor);
            }
        }
        Dish dish = new Dish();
        // BeanUtils工具类的copyProperties方法，将一个对象的属性复制到另一个对象当中，注意：是根据属性的类型和名称进行匹配的
        BeanUtils.copyProperties(dishDto,dish);
        dishMapper.updateById(dish);
    }

    /**
     * 查询功能
     * @param id
     * @return
     */
    @Override
    public DishDto searchDishes(Long id) {
        QueryWrapper<Dish> queryWrapper  = new QueryWrapper<>();
        queryWrapper.eq(id != null,"id",id);
        Dish dish = dishMapper.selectOne(queryWrapper);
        DishDto dishDto = new DishDto();
        QueryWrapper<DishFlavor> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq(id != null,"dish_id",id);
        List<DishFlavor> flavors = dishFlavorMapper.selectList(queryWrapper1);
        dishDto.setFlavors(flavors);
        // 对象拷贝器，将一个对象中的属性拷贝到另一个对象当中
        BeanUtils.copyProperties(dish,dishDto);
        return dishDto;
    }

    /**
     * 查询功能
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> searchDishes_02(Long categoryId) {
        // 创建条件对象
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 设置查询条件
        lambdaQueryWrapper.eq(categoryId != null,Dish::getCategoryId,categoryId);
        lambdaQueryWrapper.eq(Dish::getStatus,1);
        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishes = dishMapper.selectList(lambdaQueryWrapper);
        return dishes;
    }
}
