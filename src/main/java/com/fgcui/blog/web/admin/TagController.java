package com.fgcui.blog.web.admin;

import com.fgcui.blog.po.Category;
import com.fgcui.blog.po.Tag;
import com.fgcui.blog.service.TagService;
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
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public String list(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                       Model model) {
        Page<Tag> tags = tagService.listTags(pageable);
        model.addAttribute("page", tags);
        return "admin/tags";
    }

    @GetMapping("/tags/add")
    public String add(Model model) {
        model.addAttribute("tag", new Tag());
        return "admin/new-tag";
    }


    @PostMapping("/tag")
    public String save(@Valid Tag tag, BindingResult bindingResult, RedirectAttributes attributes) {
        if (tagService.getTagByName(tag.getName()) != null) {
            bindingResult.rejectValue("name", "nameError", "该标签已存在");
        }

        if (bindingResult.hasErrors()) {
            return "admin/new-tag";
        }

        Tag result = tagService.save(tag);
        if (result == null) {
            attributes.addFlashAttribute("message", "添加失败");
        } else {
            attributes.addFlashAttribute("message", "添加成功");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tag/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("tag", tagService.getTag(id));
        return "admin/new-tag";
    }

    @PostMapping("/tag/{id}")
    public String save(@Valid Tag tag, BindingResult bindingResult, @PathVariable Long id, RedirectAttributes attributes) {
        if (tagService.getTagByName(tag.getName()) != null) {
            bindingResult.rejectValue("name", "nameError", "该标签已存在");
        }

        if (bindingResult.hasErrors()) {
            return "admin/new-tag";
        }

        Tag result = tagService.updateTag(id, tag);
        if (result == null) {
            attributes.addFlashAttribute("message", "更新失败");
        } else {
            attributes.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tag/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        tagService.delete(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/tags";
    }

}
