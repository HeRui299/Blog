package com.herui.blog.service;

import com.herui.blog.pojo.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface TagService{
    Page<Tag> listTag(Pageable pageable);

    Tag getTagByName(String name);

    Tag saveTag(Tag tag);

    Tag getTag(Long id);

    void deleteTagById(Long id);

    List<Tag> listTag();

    List<Tag> listTag(String ids);

    List<Tag> listTagTop(Integer size);

    Tag updateTag(Long id, Tag tag);
}
