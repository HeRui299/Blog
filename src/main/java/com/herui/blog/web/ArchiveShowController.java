package com.herui.blog.web;

import com.herui.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArchiveShowController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/archive")
    public String archive(ModelMap modelMap){
        modelMap.put("archiveMap",blogService.archiveBlog());
        modelMap.put("blogCount",blogService.countBlog());
        return "archives";
    }
}