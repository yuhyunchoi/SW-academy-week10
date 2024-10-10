package com.nhnacademy.shoppingmall.common.filter;

import com.nhnacademy.shoppingmall.user.domain.User;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class AdminCheckFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        //todo#11 /admin/ 하위 요청은 관리자 권한의 사용자만 접근할 수 있습니다. ROLE_USER가 접근하면 403 Forbidden 에러처리
        if(req.getServletPath().equals("/admin/")){
            User user = (User) req.getSession().getAttribute("userAuth");
            if(Objects.isNull(user) || !"ROLE_USER".equals("userAuth")){
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "403 Forbidden");
                return;
            }
        }
        chain.doFilter(req, res);
    }
}
