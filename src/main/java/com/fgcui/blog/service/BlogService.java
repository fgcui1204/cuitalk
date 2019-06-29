package com.fgcui.blog.service;

import com.fgcui.blog.bean.BlogQuery;
import com.fgcui.blog.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;


public interface BlogService {
    Blog getBlog(Long id);


    Blog getAndConvertBlog(Long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlog(Long tagId, Pageable pageable);

    List<Blog> listRecommendTopBlog(Integer size);

    Page<Blog> search(Pageable pageable, String query);

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id, Blog blog);

    void deleteBlog(Long id);

    Map<String, List<Blog>> archiveBlogs();

    Long countBlog();
}
