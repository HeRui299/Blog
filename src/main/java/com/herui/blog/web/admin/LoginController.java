package com.herui.blog.web.admin;
import com.herui.blog.constant.LoginConstant;
import com.herui.blog.pojo.User;
import com.herui.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;
@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping // 这里不给值，默认是 admin 就访问这个方法
    public String loginPage(){
        return "admin/login";
    }

    @PostMapping("/login") // post请求 RequestParam 封装进来
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes){
        User user = userService.checkUser(username, password);
        if (user!=null){
            user.setPassword(null); // 为了安全，把密码设置为空不传到前台
            session.setAttribute(LoginConstant.SESSION_USER,user);
            return "admin/index";
        }else{
            redirectAttributes.addFlashAttribute("message","用户名或者密码不正确");
            return "redirect:/admin";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/admin";
    }
}