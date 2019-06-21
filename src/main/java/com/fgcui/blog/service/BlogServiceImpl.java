package com.fgcui.blog.service;

import com.fgcui.blog.bean.BlogQuery;
import com.fgcui.blog.exception.NotFoundException;
import com.fgcui.blog.po.Blog;
import com.fgcui.blog.repository.BlogRepository;
import com.fgcui.blog.util.MarkdownUtils;
import com.fgcui.blog.util.MyBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getOne(id);
    }

    @Override
    public Blog getAndConvertBlog(Long id) {
        Blog blog = blogRepository.getOne(id);

        Blog result = new Blog();

        BeanUtils.copyProperties(blog, result);
        String content = result.getContent();
        String formattedContent = MarkdownUtils.markdownToHtmlExtensions(content);
        result.setContent(formattedContent);
        return result;
    }


    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicates.add(criteriaBuilder.like(root.get("title"), "%"+blog.getTitle()+"%"));
                }
                if (blog.getCategoryId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("category").get("id"), blog.getCategoryId()));
                }
                if (blog.isRecommend()) {
                    predicates.add(criteriaBuilder.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public List<Blog> listRecommendTopBlog(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable = new PageRequest(0, size, sort);
        return blogRepository.findTopRecommendBlog(pageable);
    }

    @Override
    public Page<Blog> search(Pageable pageable, String query) {

        return blogRepository.findByQuery(query, pageable);
    }


    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {

        if (blog.getId() == null) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        } else {
            blog.setUpdateTime(new Date());
        }

        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog result = blogRepository.findById(id).orElseThrow(() -> new NotFoundException("博客不存在"));
        BeanUtils.copyProperties(blog, result, MyBeanUtils.getNullPropertyNames(result));
        result.setUpdateTime(new Date());
        return blogRepository.save(result);
    }

    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
