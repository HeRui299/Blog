package com.herui.blog.service.Impl;

import com.herui.blog.dao.BlogRepository;
import com.herui.blog.dao.TagRepository;
import com.herui.blog.exception.NotFoundException;
import com.herui.blog.pojo.Blog;
import com.herui.blog.pojo.Tag;
import com.herui.blog.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public Tag getTag(Long id) {
        return tagRepository.getOne(id);
    }

    @Override
    public void deleteTagById(Long id) {
        tagRepository.deleteById(id);
    }

    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> listTag(String names) { // 1,2,3
        String[] name = names.split(",");

        List<Tag> tags = new ArrayList<>();

        for (int i =0;i<name.length;i++){
            Tag byName = tagRepository.findByName(name[i]);
            if (byName != null){
                System.out.println(byName.getName());
                tags.add(byName);
            }else{
                Tag tag = new Tag();
                tag.setName(name[i]);
                tagRepository.save(tag);
                tags.add(tag);
            }
        }
        return tags;
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"blogs.size");
        Pageable pageable = PageRequest.of(0,size,sort);
        return tagRepository.findTop(pageable);
    }

    private List<String> convertToList(String ids){
        List<String> list = new ArrayList<>();
        if (! "".equals(ids) && ids != null){
            String[] idarray = ids.split(",");
            for (int i = 0;i<idarray.length;i++){
                list.add(idarray[i]);
            }
        }
        return list;
    }



    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag one = tagRepository.getOne(id);

        if (one == null){
            throw new NotFoundException("没有此标签");
        }
        BeanUtils.copyProperties(tag,one);
        return tagRepository.save(one);
    }
}
