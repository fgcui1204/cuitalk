package com.fgcui.blog.web.admin;


import com.fgcui.blog.po.Category;
import com.fgcui.blog.service.CategoryService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String list(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                       Model model) {
        Page<Category> categories = categoryService.listCategory(pageable);
        model.addAttribute("page", categories);
        return "admin/categories";
    }

    @GetMapping("/categories/add")
    public String add(Model model) {
        model.addAttribute("category", new Category());
        return "admin/new-category";
    }

    @PostMapping("/category")
    public String save(@Valid Category category, BindingResult bindingResult, RedirectAttributes attributes) {
        if (categoryService.getCategoryByName(category.getName()) != null) {
            bindingResult.rejectValue("name", "nameError", "该分类已存在");
        }

        if (bindingResult.hasErrors()) {
            return "admin/new-category";
        }

        Category result = categoryService.save(category);
        if (result == null) {
            attributes.addFlashAttribute("message", "添加失败");
        } else {
            attributes.addFlashAttribute("message", "添加成功");
        }
        return "redirect:/admin/categories";
    }

    @GetMapping("/category/{id}/edit")
    public String edit(@PathVariable  Long id, Model model) {
        model.addAttribute("category", categoryService.getCategory(id));
        return "admin/new-category";
    }


    @PostMapping("/category/{id}")
    public String edit(@Valid Category category, BindingResult bindingResult, @PathVariable  Long id, RedirectAttributes attributes) {
        if (categoryService.getCategoryByName(category.getName()) != null) {
            bindingResult.rejectValue("name", "nameError", "该分类已存在");
        }

        if (bindingResult.hasErrors()) {
            return "admin/new-category";
        }

        Category result = categoryService.updateCategory(id, category);
        if (result == null) {
            attributes.addFlashAttribute("message", "更新失败");
        } else {
            attributes.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/admin/categories";
    }

    @GetMapping("/category/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        categoryService.delete(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/categories";
    }

}
