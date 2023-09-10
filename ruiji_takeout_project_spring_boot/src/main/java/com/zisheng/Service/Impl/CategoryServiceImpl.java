package com.zisheng.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zisheng.Mapper.CategoryMapper;
import com.zisheng.Pojo.Category;
import com.zisheng.Pojo.CategoryPagingResult;
import com.zisheng.Service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务逻辑层
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    /**
     * 插入菜品功能
     * @param category
     */
    @Override
    public void insertCat(Category category) {
        categoryMapper.insert(category);
    }

    /**
     * 分页菜品功能
     * @param page
     * @param pageSize
     * @return
     */
    // 使用PageHelper分页插件进行解决
//    @Override
//    public CategoryPagingResult PagingCat(Integer page, Integer pageSize) {
//        PageHelper.startPage(page,pageSize);
//        List<Category> categories = categoryMapper.selectList(null);
//        Page<Category> pages = (Page<Category>) categories;
//        return new CategoryPagingResult(pages.getResult(),pages.getTotal());
//    }
    // 使用MybatisPlus提供的分页拦截器进行解决
    @Override
    public CategoryPagingResult PagingCat(Integer page, Integer pageSize) {
        IPage<Category> ipage = new Page<>(page,pageSize);
        categoryMapper.selectPage(ipage,null);
        return new CategoryPagingResult(ipage.getRecords(),ipage.getTotal());
    }

    /**
     * 更新菜品功能
     * @param category
     */
    @Override
    public void updateCat(Category category) {
        categoryMapper.updateById(category);
    }

    /**
     * 删除菜品信息
     * @param ids
     */
    @Override
    public void deleteCat(Long ids) {
        categoryMapper.deleteById(ids);
    }

    @Override
    public List<Category> searchCat(Integer type) {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(type != null,"type",type);
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        return categories;
    }
}
