package com.fgcui.blog.web;

import com.fgcui.blog.service.BlogService;
import com.fgcui.blog.service.CategoryService;
import com.fgcui.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String index(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("page", blogService.listBlog(pageable));
        model.addAttribute("categories", categoryService.listTopCategory(6));
        model.addAttribute("tags", tagService.listTopTags(10));
        model.addAttribute("recommendBlogs", blogService.listRecommendTopBlog(8));
        return "index";
    }

    @GetMapping("/blog")
    public String blog() {
        return "blog";
    }
}
