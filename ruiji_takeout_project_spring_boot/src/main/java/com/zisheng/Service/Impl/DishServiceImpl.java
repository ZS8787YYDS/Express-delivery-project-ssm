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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    private static final Logger log = LoggerFactory.getLogger(DishServiceImpl.class);
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
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
        IPage<Dish> iPage = new Page<>(page,pageSize);
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
        // 将菜品信息插入到数据库表中
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
     * 查询功能：
     * 设计思路：先从redis中查询数据，如果查到的话直接返回
     *         如果在redis中查询不到数据的话，就去查询数据库，将查询的结果缓存在redis中
     * @param categoryId
     * @return
     */
    @Override
    public List<DishDto> searchDishes_02(Long categoryId) {
        /**
         * 先从redis中查询数据，如果查到的话直接返回。如果在redis中查询不到数据的话，就去查询数据库，将查询的结果缓存在redis中
         * 并且可以设置过期时间
         */
        // 自定义缓存的key
        String key = "category_" + categoryId + "_1";
        // 尝试从redis中查询数据
        List<DishDto> dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);
        if(!ObjectUtils.isEmpty(dishDtoList))
        {
            //在redis中查询成功，直接将结果返回即可
            return dishDtoList;
        }
        // 在缓存中查询失败，从数据库中查询数据
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 设置查询条件
        lambdaQueryWrapper.eq(categoryId != null,Dish::getCategoryId,categoryId)
                          .eq(Dish::getStatus,1);
        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishes = dishMapper.selectList(lambdaQueryWrapper);
        dishDtoList = dishes.stream().map(o -> {
            DishDto dishDto = new DishDto();
            // 调用BeanUtils工具类的copyProperties方法进行属性赋值，注意是根据类型类属性名称进行复制的
            BeanUtils.copyProperties(o, dishDto);
            // 获取菜品id，用于查询该菜品的所有口味信息
            Long dishId = o.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(dishId != null, DishFlavor::getDishId, dishId);
            List<DishFlavor> flavors = dishFlavorMapper.selectList(lambdaQueryWrapper1);
            dishDto.setFlavors(flavors);
            return dishDto;
        }).collect(Collectors.toList());
        // 将查询的结果存储在redis当中,设置有效期限为一个小时。
        // 调用redisTemplate对象的opsForValue方法获得操作String类型数据对象，调用set方法即可设置键以及对应的值。
        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);
        return dishDtoList;
    }
}
