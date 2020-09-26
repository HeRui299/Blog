package com.herui.blog.web.admin;

import com.herui.blog.pojo.Type;
import com.herui.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @GetMapping("/types")
    public String types(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.DESC)
                                    Pageable pageable, ModelMap modelMap){
        modelMap.put("page",typeService.listType(pageable));
        return "admin/types";
    }

    @PostMapping("/types")
    // 这个BindingResult 这个参数一定要放在 这个校验的参数类型后面，才能生效
    public String post(@Valid Type type,BindingResult result,RedirectAttributes redirectAttributes){
        Type typeByName = typeService.getTypeByName(type.getName());
        if (typeByName!=null){
            result.rejectValue("name","nameError","不能添加重复的分类"); // 向字段添加一个错误信息
        }

        if (result.hasErrors()){
            return "admin/types-input";
        }
        Type t = typeService.saveType(type);
        if (t==null){
            // 保存失败
            redirectAttributes.addFlashAttribute("message","新增失败");
        }else{
            // 保存成功
            redirectAttributes.addFlashAttribute("message","新增成功");
        }
        return "redirect:/admin/types";
    }

    @PostMapping("/types/{id}")
    public String editPost(@Valid Type type,BindingResult result,@PathVariable Long id,RedirectAttributes redirectAttributes){
        Type typeByName = typeService.getTypeByName(type.getName());
        if (typeByName!=null){
            result.rejectValue("name","nameError","请修改分类名称"); // 向字段添加一个错误信息  name 就是字段 nameError 错误类型
        }

        if (result.hasErrors()){
            return "admin/types-input";
        }
        Type t = typeService.updateType(id, type);
        if (t==null){
            // 保存失败
            redirectAttributes.addFlashAttribute("message","更新失败");
        }else{
            // 保存成功
            redirectAttributes.addFlashAttribute("message","更新成功");
        }
        return "redirect:/admin/types";
    }


    @GetMapping("/types/input")
    public String input(ModelMap modelMap){
        modelMap.put("type",new Type());
        return "admin/types-input";
    }

    @GetMapping("/types/{id}/input")
    public String eidtInput(@PathVariable Long id, ModelMap modelMap){
        modelMap.addAttribute("type",typeService.getType(id));
        return "admin/types-input";
    }



}