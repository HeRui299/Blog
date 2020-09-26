package com.herui.blog.web;

import com.herui.blog.pojo.Tag;
import com.herui.blog.service.BlogService;
import com.herui.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;
import java.util.List;

@Controller
public class TagShowController {

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/tags/{id}")
    public String typeShow(@PageableDefault(size = 2,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable, @PathVariable Long id, ModelMap modelMap){
        List<Tag> tags = tagService.listTagTop(1000);
        if (id==-1){
            id = tags.get(0).getId();
        }
        modelMap.put("tags",tags);
        modelMap.put("page",blogService.listBlog(pageable,id));
        modelMap.put("activeTagId",id);
       return "tags";
    }
}