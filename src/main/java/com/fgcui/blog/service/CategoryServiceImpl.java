package com.fgcui.blog.service;

import com.fgcui.blog.exception.NotFoundException;
import com.fgcui.blog.po.Category;
import com.fgcui.blog.repository.CategoryRepository;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategory(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Category> listCategory(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Category updateCategory(Long id, Category category) {
        Category result = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("不存在该类型"));

        BeanUtils.copyProperties(category, result);

        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.delete(categoryRepository.getOne(id));
    }
}
