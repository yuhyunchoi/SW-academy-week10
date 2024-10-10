package com.nhnacademy.shoppingmall.controller.auth;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@RequestMapping(method = RequestMapping.Method.GET,value = "/login.do")
public class LoginController implements BaseController {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        //todo#13-1 session이 존재하고 로그인이 되어 있다면 redirect:/index.do 반환 합니다.
        HttpSession session = req.getSession(false);
        if(Objects.isNull(session)){
            return "redirect:/index.do";
        }

        return "shop/login/login_form";
    }
}
