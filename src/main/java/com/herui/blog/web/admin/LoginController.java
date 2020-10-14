package com.herui.blog.web.admin;
import com.google.code.kaptcha.Producer;
import com.herui.blog.constant.LoginConstant;
import com.herui.blog.pojo.User;
import com.herui.blog.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping // 这里不给值，默认是 admin 就访问这个方法
    public String loginPage(){
        return "admin/login";
    }

    @PostMapping("/login") // post请求 RequestParam 封装进来
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        @RequestParam String kaptcha,
                        HttpSession session,
                        RedirectAttributes redirectAttributes){
        User user = userService.checkUser(username, password);
        String code = session.getAttribute("kaptcha").toString(); // 后台生成的code
        if (user == null){
            redirectAttributes.addFlashAttribute("message","用户名或密码错误");
            return "redirect:/admin";
        }else if (! kaptcha.equalsIgnoreCase(code)){
            redirectAttributes.addFlashAttribute("message","验证码填写有误");
            return "redirect:/admin";
        }
        user.setPassword(null); // 为了安全，把密码设置为空不传到前台
        session.setAttribute(LoginConstant.SESSION_USER,user);
        return "admin/index";
    }

    @GetMapping("/kaptcha")
    public void kaptcha(HttpServletResponse response,HttpSession session){
        // 生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        // 存入session
        session.setAttribute("kaptcha",text);

        // 将图片输出给浏览器
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image,"png",os);
        } catch (IOException e) {
            logger.error("响应验证码失败"+e.getMessage());
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/admin";
    }
}