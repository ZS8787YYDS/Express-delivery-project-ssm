package com.zisheng.Controller;

import com.zisheng.Pojo.Category;
import com.zisheng.Pojo.CategoryPagingResult;
import com.zisheng.Pojo.Result;
import com.zisheng.Service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 控制层
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);

    /**
     * 插入菜品
     * @param category
     * @return
     */
    @PostMapping
    public Result insertCat(@RequestBody Category category)
    {
        log.info("接收到的数据为：{}",category);
        categoryService.insertCat(category);
        return Result.success();
    }
    /**
     * 菜品分页功能
     */
    @GetMapping("/page")
    public Result PagingCat(Integer page,Integer pageSize)
    {
        log.info("page: {},pageSize: {}",page,pageSize);
        CategoryPagingResult categoryPagingResult = categoryService.PagingCat(page,pageSize);
        return Result.success(categoryPagingResult);
    }

    /**
     * 更新菜品功能
     * @param category
     * @return
     */
    @PutMapping
    public Result updateCat(@RequestBody Category category)
    {
        log.info("接收到的数据为: {}",category);
        categoryService.updateCat(category);
        return Result.success();
    }
    /**
     * 删除菜品功能
     */
    @DeleteMapping
    public Result deleteCat(Long ids)
    {
        log.info("接收到的数据: {}",ids);
        categoryService.deleteCat(ids);
        return Result.success();

    }

    /**
     * 分类查询功能
     * @param type
     * @return
     */
    @GetMapping("/list")
    public Result searchCat(Integer type)
    {
        log.info("接收到的数据为：{}",type);
        List<Category> categories = categoryService.searchCat(type);
        return Result.success(categories);
    }
}
