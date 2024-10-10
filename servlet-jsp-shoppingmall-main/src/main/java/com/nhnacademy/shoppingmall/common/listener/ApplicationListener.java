package com.nhnacademy.shoppingmall.common.listener;

import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.exception.UserNotFoundException;
import com.nhnacademy.shoppingmall.user.repository.impl.UserRepositoryImpl;
import com.nhnacademy.shoppingmall.user.service.UserService;
import com.nhnacademy.shoppingmall.user.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
public class ApplicationListener implements ServletContextListener {
    private final UserService userService = new UserServiceImpl(new UserRepositoryImpl());
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //todo#12 application 시작시 테스트 계정인 admin,user 등록합니다. 만약 존재하면 등록하지 않습니다.
        log.info("Application started, initializing test accounts");
        try{
            User admin = userService.getUser("admin");
            log.info("Admin user already exists : {}", admin);

        }catch(UserNotFoundException e){
            User admin = new User(
                    "admin",
                    "admin_name",
                    "admin1234",
                    "1999-02-12",
                    User.Auth.ROLE_ADMIN,
                    0,
                    LocalDateTime.now(),
                    null);
            userService.saveUser(admin);
            log.info("Admin user registered : {}", admin);
        }

        try{
            User user = userService.getUser("user");
            log.info("User already exists : {}", user);
        }catch(UserNotFoundException e){
            User user = new User(
                    "user",
                    "user1234",
                    "user12345",
                    "2002-09-30",
                    User.Auth.ROLE_USER,
                    1000000,
                    LocalDateTime.now(),
                    LocalDateTime.now());

            userService.saveUser(user);
            log.info("User registered : {}", user);
        }
    }
}
