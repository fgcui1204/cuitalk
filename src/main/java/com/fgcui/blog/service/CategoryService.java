package com.fgcui.blog.service;

import com.fgcui.blog.po.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface CategoryService {
    Category save(Category category);

    Category getCategory(Long id);

    Page<Category> listCategory(Pageable pageable);

    List<Category> listCategory();

    Category updateCategory(Long id, Category category);

    Category getCategoryByName(String name);

    void delete(Long id);
}
