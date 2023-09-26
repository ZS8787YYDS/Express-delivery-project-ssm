package com.zisheng.Controller;

import com.zisheng.Dto.DishDto;
import com.zisheng.Pojo.Dish;
import com.zisheng.Pojo.DishPagingResult;
import com.zisheng.Pojo.Result;
import com.zisheng.Service.DishService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Resource
    private RedisTemplate<String,Object> redisTemplate;// 注入redisTemplate对象，可以调用这个对象的方法去获取操作具体数据类型的对象
    private static final Logger log = LoggerFactory.getLogger(DishController.class);// 创建日志记录对象
    @Autowired
    private DishService dishService;

    /**
     * 分页功能
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result DishPaging(Integer page,Integer pageSize,String name)
    {
        log.info("接收到的数据为：page: {}, pageSize: {},name: {}",page,pageSize,name);
        DishPagingResult dishPagingResult =  dishService.DishPaging(page,pageSize,name);
        return  Result.success(dishPagingResult);
    }
//    /**
//     * 停售功能
//     */
//    @PostMapping("/status/{status}")
//    public Result pauseSell(Long ids,@PathVariable Integer status)
//    {
//        log.info("接收到的数据为：ids: {},status: {}",ids,status);
//        dishService.pauseSell(ids,status);
//        return Result.success();
//    }

//    /**
//     * 删除功能
//     * @param ids
//     * @return
//     */
//    @DeleteMapping
//    public Result deleteDish(Long ids)
//    {
//        log.info("接收到的数据为：ids: {}",ids);
//        dishService.deleteDish(ids);
//        return Result.success();
//    }

    /**
     * 停售与起售功能
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public Result startSell(@PathVariable Integer status, @RequestParam List<Long> ids)
    {
        log.info("接收到的数据为：id:{}",status);
        log.info("dishes:" + ids.toString());
        dishService.startSell(status,ids);
        // 第一种方式，清理菜品所有分类的缓存
        // 获取所有以category_开头的所有键，存放在set集合中
        Set<String> keys = redisTemplate.keys("category_*");
        // 清理redis中所有在集合当中的键
        if(!ObjectUtils.isEmpty(keys)) redisTemplate.delete(keys);
//        // 第二种方式：清理指定菜品分类的缓存
//        String key = "category_" + dishDto.getCategoryId() + "_1";
//        redisTemplate.delete(key);
        return Result.success();
    }

    /**
     * 批量删除功能
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result DeleteBatch(@RequestParam List<Long> ids)
    {
        dishService.deleteBatch(ids);
        // 第一种方式，清理菜品所有分类的缓存
        Set<String> keys = redisTemplate.keys("category_*");
        if(!ObjectUtils.isEmpty(keys)) redisTemplate.delete(keys);
//        // 第二种方式：清理指定菜品分类的缓存
//        String key = "category_" + dishDto.getCategoryId() + "_1";
//        redisTemplate.delete(key);
        return Result.success();
    }

    /**
     * 插入数据功能
     * @param dishDto
     * @return
     */
    @PostMapping
    public Result insertDish(@RequestBody DishDto dishDto)
    {
        log.info("接收到的数据为：{}",dishDto);
        dishService.insertDish(dishDto);
        // 插入菜品之后，某一分类下的菜品就会发生改变，此时为了保证数据库中的数据和redis中数据的一致性
        // 要注意清理缓存
        // 第一种方式，清理菜品所有分类的缓存
        Set<String> keys = redisTemplate.keys("category_*");
        if(!ObjectUtils.isEmpty(keys)) redisTemplate.delete(keys);
//        // 第二种方式：清理指定菜品分类的缓存
//        String key = "category_" + dishDto.getCategoryId() + "_1";
//        redisTemplate.delete(key);
        return Result.success();
    }

    /**
     * 修改功能
     * @param dishDto
     * @return
     */
    @PutMapping
    public Result updateDishData(@RequestBody DishDto dishDto)
    {
        dishService.updateDishData(dishDto);
        // 第一种方式，清理菜品所有分类的缓存
        Set<String> keys = redisTemplate.keys("category_*");
        if(!ObjectUtils.isEmpty(keys)) redisTemplate.delete(keys);
//        // 第二种方式：清理指定菜品分类的缓存
//        String key = "category_" + dishDto.getCategoryId() + "_1";
//        redisTemplate.delete(key);
        return Result.success();
    }

    /**
     * 查询功能
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result searchDishes(@PathVariable Long id)
    {
        log.info("接收到的数据为：{}",id);
        DishDto dish = dishService.searchDishes(id);
        return Result.success(dish);
    }

    /**
     * 查询功能
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public Result searchDishes_02(Long categoryId)
    {
        log.info("接收到的数据为：{}",categoryId);
        List<DishDto> dishes = dishService.searchDishes_02(categoryId);
        return Result.success(dishes);

    }
}
