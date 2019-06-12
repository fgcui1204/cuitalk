package com.fgcui.blog.service;

import com.fgcui.blog.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TagService {
    Tag save(Tag tag);

    void delete(Long id);

    Page<Tag> listTags(Pageable pageable);

    Tag getTagByName(String name);

    Tag getTag(Long id);

    Tag updateTag(Long id, Tag tag);
}
