package com.zisheng.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zisheng.Pojo.Category;
import com.zisheng.Pojo.CategoryPagingResult;

import java.util.List;

public interface CategoryService extends IService<Category> {

    void insertCat(Category category);

    CategoryPagingResult PagingCat(Integer page, Integer pageSize);

    void updateCat(Category category);

    void deleteCat(Long ids);

    List<Category> searchCat(Integer type);
}
