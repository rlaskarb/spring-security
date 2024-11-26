package com.ohgiraffers.sessionsecurity.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @GetMapping("/")
    public String mainPage(){
        return "main";
    }


    /*필기  관리자만 들어갈수 있는 지 TEST */
    @GetMapping("/admin/page")
    public ModelAndView adminPage(ModelAndView mv){
        mv.setViewName("admin/admin");
        return mv;
    }

    @GetMapping("/user/page")
    public ModelAndView userPage(ModelAndView mv){
        mv.setViewName("/user/user");
        return mv;
    }



}
