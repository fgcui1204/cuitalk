package com.fgcui.blog.web;

import com.fgcui.blog.po.Blog;
import com.fgcui.blog.service.BlogService;
import com.fgcui.blog.service.CategoryService;
import com.fgcui.blog.service.TagService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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


    @PostMapping("/search")
    public String search(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String query,
                         Model model) {
        model.addAttribute("page", blogService.search(pageable, "%" + query + "%"));
        model.addAttribute("query", query);
        return "search";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model) {
        Blog blog = blogService.getAndConvertBlog(id);
        model.addAttribute("blog", blog);
        return "blog";
    }
}
