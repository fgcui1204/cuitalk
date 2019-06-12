package com.fgcui.blog.service;

import com.fgcui.blog.exception.NotFoundException;
import com.fgcui.blog.po.Tag;
import com.fgcui.blog.repository.TagRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public void delete(Long id) {
        tagRepository.delete(tagRepository.getOne(id));
    }

    @Override
    public Page<Tag> listTags(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Override
    public Tag getTag(Long id) {
        return tagRepository.findById(id).orElse(null);
    }

    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag result = tagRepository.findById(id).orElseThrow(() -> new NotFoundException("不存在该标签"));

        BeanUtils.copyProperties(tag, result);

        return tagRepository.save(tag);
    }
}
