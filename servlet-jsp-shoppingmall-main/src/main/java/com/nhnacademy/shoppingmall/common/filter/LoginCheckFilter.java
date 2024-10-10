package com.nhnacademy.shoppingmall.common.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class LoginCheckFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        //todo#10 /mypage/ 하위경로의 접근은 로그인한 사용자만 접근할 수 있습니다.
        String servletPath = req.getServletPath();

        if(servletPath.startsWith("/mypage/")) {
            Object user = req.getSession().getAttribute("user");

            if(Objects.isNull(user)){
                res.sendRedirect(req.getContextPath() + "/login.do");
                return;
            }
        }
        chain.doFilter(req, res);
    }
}