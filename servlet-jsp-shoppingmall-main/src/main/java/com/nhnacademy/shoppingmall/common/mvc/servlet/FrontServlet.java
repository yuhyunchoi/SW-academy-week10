package com.nhnacademy.shoppingmall.common.mvc.servlet;

import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.common.mvc.view.ViewResolver;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.common.mvc.controller.ControllerFactory;

import lombok.extern.slf4j.Slf4j;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.Set;

@Slf4j
@WebServlet(name = "frontServlet",urlPatterns = {"*.do"})
public class FrontServlet extends HttpServlet {
    private ControllerFactory controllerFactory;
    private ViewResolver viewResolver;

    @Override
    public void init() throws ServletException {
        //todo#7-1 controllerFactory를 초기화 합니다.
        ServletContext context = getServletContext();
        controllerFactory = new ControllerFactory();

        Set<Class<?>> controllerClasses = (Set<Class<?>>) context.getAttribute(ControllerFactory.CONTEXT_CONTROLLER_FACTORY_NAME);
        if(controllerClasses != null) {
            try{
                controllerFactory.initialize(controllerClasses, context);
            }catch(Exception e){
                log.error("Failed to initialize controller", e);
                throw new ServletException(e);
            }
        }else {
            log.warn("No controller factory found in ServletContext");
        }
        //todo#7-2 viewResolver를 초기화 합니다.
        viewResolver = new ViewResolver(ViewResolver.DEFAULT_PREFIX, ViewResolver.DEFAULT_POSTFIX);
        context.setAttribute("VIEW_RESOLVER", viewResolver);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp){
        try{
            //todo#7-3 Connection pool로 부터 connection 할당 받습니다. connection은 Thread 내에서 공유됩니다.
            Connection connection = DbConnectionThreadLocal.getConnection();

            BaseController baseController = (BaseController) controllerFactory.getController(req);
            String viewName = baseController.execute(req,resp);

            if(viewResolver.isRedirect(viewName)){
                String redirectUrl = viewResolver.getRedirectUrl(viewName);
                log.debug("redirectUrl:{}",redirectUrl);
                //todo#7-6 redirect: 로 시작하면  해당 url로 redirect 합니다.
                resp.sendRedirect(redirectUrl);
            }else {
                String layout = viewResolver.getLayOut(viewName);
                log.debug("viewName:{}", viewResolver.getPath(viewName));
                req.setAttribute(ViewResolver.LAYOUT_CONTENT_HOLDER, viewResolver.getPath(viewName));
                RequestDispatcher rd = req.getRequestDispatcher(layout);
                rd.include(req, resp);
            }
        }catch (Exception e){
            log.error("error:{}",e);
            DbConnectionThreadLocal.setSqlError(true);
            //todo#7-5 예외가 발생하면 해당 예외에 대해서 적절한 처리를 합니다.
            try {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An internal error occurred");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }finally {
            //todo#7-4 connection을 반납합니다.
            DbConnectionThreadLocal.reset();
        }
    }


}