package com.shiro.controller;

import com.shiro.config.UserRealm;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Login {

    @GetMapping("/main")
    public String thymeleaf(Model model) {
        model.addAttribute("name", "管理员");
        return "main";
    }

    @RequestMapping("/add")
    public String add() {
        return "user/add";
    }

    @GetMapping("/update")
    public String update() {
        return "user/update";
    }

    /**
     * 登录失败跳转登录页面
     * @return
     */
    @GetMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    @PostMapping("login")
    public String login(String name,String password,Model model){
        /*
        * 使用shiro编写认证操作*/
        //1获取subject
        Subject subject = SecurityUtils.getSubject();
        //2封装用户数据
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(name,password);
        //
        try{
            subject.login(usernamePasswordToken);
        }catch (UnknownAccountException e){
            model.addAttribute("msg","用户名不存在");
            return "/login";
        }catch (IncorrectCredentialsException e){
            model.addAttribute("msg","密码错误");
            return "/login";
        }
        return "redirect:main";
    }

    @GetMapping("/unAuth")
    public String unAuth(){
        return "noAuth";
    }
}
