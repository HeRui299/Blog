package com.herui.blog.web.admin;

import com.herui.blog.pojo.Tag;
import com.herui.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TagsController {

    @Autowired
    private TagService tagsService;


    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.DESC)
                                   Pageable pageable
                                  , ModelMap modelMap){
        modelMap.put("page",tagsService.listTag(pageable));
        return "admin/tags";
    }

    @Transactional
    @PostMapping("/tags/{id}")
    public String addTag(@Valid Tag tag,BindingResult result,@PathVariable(required = false) Long id,RedirectAttributes attributes){
        Tag tag1 = tagsService.getTagByName(tag.getName());
        System.out.println("id = " + id);
        if (tag1!=null){
           result.rejectValue("name","NameError","该标签已存在，请重试！");
        }

        if (result.hasErrors()){
            return "admin/tags-input";
        }
        Tag t = null;
        if (id == 0){
            t = tagsService.saveTag(tag);
        }else{
            t = tagsService.updateTag(id,tag);
        }


        if(t!=null){
            attributes.addFlashAttribute("message","操作成功");
        }else{
            attributes.addFlashAttribute("message","操作失败");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id,ModelMap map){
        map.put("tag",tagsService.getTag(id));
        return "admin/tags-input";
    }

    @Transactional
    @GetMapping("/tags/delete")
    public String deleteTag(@RequestParam("id") Long id, ModelMap modelMap){
        tagsService.deleteTagById(id);
        modelMap.put("message","删除成功");
        return "redirect:/admin/tags";
    }


    @GetMapping("/tags/input")
    public String input(ModelMap modelMap){
        modelMap.put("tag",new Tag());
        return "admin/tags-input";
    }
}