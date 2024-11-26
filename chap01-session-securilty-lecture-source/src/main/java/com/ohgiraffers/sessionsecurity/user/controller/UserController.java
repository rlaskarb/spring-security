package com.ohgiraffers.sessionsecurity.user.controller;

import com.ohgiraffers.sessionsecurity.user.model.dto.SignupDTO;
import com.ohgiraffers.sessionsecurity.user.model.serivce.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user/*")
public class UserController {

    @Autowired
    private MemberService memberService;

    @GetMapping("signup")
    public void signupPage(){}

    @PostMapping("signup")
    public ModelAndView signup(@ModelAttribute SignupDTO signupDTO, ModelAndView mv ){

        Integer result = memberService.regist(signupDTO);

        String message = null;

        /*필기 : controller 의 역할을 어떠한 view */
        if(result == null){
            message = "중복된 회원이 존재합니다.";
        }else if(result == 0){
            message ="비상 서버 내부에서 오류가 발생했어요!";
            mv.setViewName("user/signup");
        } else if (result >= 1) {
            message = "회원가입을 축하합니닷";
            mv.setViewName("auth/login");
        }

        mv.addObject("message",message);

        return mv;

    }
}
