package com.herui.blog.web;
import com.herui.blog.service.BlogService;
import com.herui.blog.service.CommentService;
import com.herui.blog.service.TagService;
import com.herui.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String index(@PageableDefault(size = 4,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,ModelMap modelMap){
        modelMap.put("page",blogService.listBlog(pageable));
        modelMap.put("types",typeService.listTypeTop(6));
        modelMap.put("tags",tagService.listTagTop(10));
        modelMap.put("recommendBlogs",blogService.listRecommendBlogTop(8));
        return "index";
    }

    @PostMapping("/search")
    public String search(@PageableDefault(size = 2,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,ModelMap modelMap,@RequestParam String query){
        modelMap.put("page",blogService.listBlog(pageable,query));
        modelMap.put("query",query);
        return "search";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id,ModelMap map){
        map.put("blog",blogService.getAndConvert(id));
        map.put("comments", commentService.listCommentByBlogId(id));
        return "blog";
    }

    @GetMapping("/footer/newblog")
    public String newblogs(ModelMap modelMap){
        modelMap.put("newblogs",blogService.listRecommendBlogTop(3));
        return "_fragments :: newblogList";
    }
}