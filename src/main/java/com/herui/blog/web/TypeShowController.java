package com.herui.blog.web;

import com.herui.blog.pojo.Blog;
import com.herui.blog.pojo.Type;
import com.herui.blog.service.BlogService;
import com.herui.blog.service.TypeService;
import com.herui.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class TypeShowController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/types/{id}")
    public String typeShow(@PageableDefault(size = 2,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable, @PathVariable Long id, ModelMap modelMap){
        List<Type> types = typeService.listTypeTop(10000);
        if (id==-1){
            id = types.get(0).getId();
        }
        BlogQuery query = new BlogQuery();
        query.setTypeId(id);
        modelMap.put("activeTypeId",id);
        modelMap.put("page",blogService.listBlog(pageable,query));
        modelMap.put("types",types);
        return "types";
    }
}