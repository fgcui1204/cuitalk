package com.fgcui.blog.web.admin;

import com.fgcui.blog.bean.BlogQuery;
import com.fgcui.blog.po.Blog;
import com.fgcui.blog.po.User;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT="admin/blogs-input";
    private static final String LIST="admin/blogs";
    private static final String REDIRECT_LIST="redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;

    @Autowired
    private CategoryService categoryService;


    @Autowired
    private TagService tagService;

    @GetMapping("/blogs")
    public String bolgs(@PageableDefault(size = 3, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blog, Model model) {

        model.addAttribute("categories", categoryService.listCategory());

        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return LIST;
    }

    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 3, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blog, Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blogs :: blogList";
    }

    @GetMapping("/blogs/input")
    public String input(Model model) {
        model.addAttribute("blog", new Blog());
        model.addAttribute("categories", categoryService.listCategory());
        model.addAttribute("tags", tagService.listTags());
        return INPUT;
    }


    @PostMapping("/blogs")
    public String post(Blog blog, HttpSession session, RedirectAttributes attributes) {
        blog.setUser((User) session.getAttribute("user"));
        blog.setCategory(categoryService.getCategory(blog.getCategory().getId()));
        blog.setTags(tagService.listTags(blog.getTagIds()));

        Blog result = blogService.saveBlog(blog);

        if (result == null) {
            attributes.addFlashAttribute("message", "添加失败");
        } else {
            attributes.addFlashAttribute("message", "添加成功");
        }
        return REDIRECT_LIST;
    }

}
