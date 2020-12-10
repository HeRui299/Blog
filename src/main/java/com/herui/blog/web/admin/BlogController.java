package com.herui.blog.web.admin;

import com.herui.blog.constant.LoginConstant;
import com.herui.blog.pojo.Blog;
import com.herui.blog.pojo.User;
import com.herui.blog.service.BlogService;
import com.herui.blog.service.TagService;
import com.herui.blog.service.TypeService;
import com.herui.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 2,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog, ModelMap modelMap){
        modelMap.put("types",typeService.listType());
        modelMap.put("page",blogService.listBlog(pageable,blog));
        return "admin/blogs";
    }

    @GetMapping("/blogs/input")
    public String blogsInput(ModelMap modelMap){
        setTypeAndTag(modelMap);
        modelMap.put("blog",new Blog());
        return "admin/blogs-input";
    }

    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, ModelMap modelMap){
        setTypeAndTag(modelMap);
        Blog blog = blogService.getBlog(id);
        blog.init();
        modelMap.put("blog",blog);
        return "admin/blogs-input";
    }

    @GetMapping("/blogs/delete")
    public String deleteBlog(@RequestParam("id") Long id,RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/blogs";
    }

    private void setTypeAndTag(ModelMap modelMap){
        modelMap.put("types",typeService.listType());
        modelMap.put("tags",tagService.listTag());
    }

    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes redirectAttributes, HttpSession session){
        blog.setUser((User) session.getAttribute(LoginConstant.SESSION_USER));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getNames()));
        Blog a; //
        if ( blog.getId()==null){
            a = blogService.saveBlog(blog);
        }else{
            a = blogService.updateBlog(blog.getId(),blog);
        }
        if (a==null){
            blogService.saveBlog(blog);
        }else{
            redirectAttributes.addFlashAttribute("message","操作成功");
        }
        return "redirect:/admin/blogs";
    }



    @PostMapping("/blogs/search")
    public String searchBlog(@PageableDefault(size = 2,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                             BlogQuery blog,ModelMap modelMap){
        modelMap.put("page",blogService.listBlog(pageable,blog));
        return "admin/blogs :: blogList";
    }
}