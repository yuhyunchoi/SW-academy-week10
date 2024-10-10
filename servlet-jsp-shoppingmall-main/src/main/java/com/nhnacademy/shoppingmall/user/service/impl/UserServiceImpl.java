package com.nhnacademy.shoppingmall.user.service.impl;

import com.nhnacademy.shoppingmall.user.exception.UserNotFoundException;
import com.nhnacademy.shoppingmall.user.service.UserService;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.repository.UserRepository;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(String userId){
        //todo#4-1 회원조회
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public void saveUser(User user) {
        //todo#4-2 회원등록
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        //todo#4-3 회원수정
        userRepository.save(user);
    }

    @Override
    public void deleteUser(String userId) {
        //todo#4-4 회원삭제
        userRepository.deleteByUserId(userId);
    }

    @Override
    public User doLogin(String userId, String userPassword) {
        //todo#4-5 로그인 구현, userId, userPassword로 일치하는 회원 조회
        return userRepository.findByUserIdAndUserPassword(userId, userPassword).orElseThrow(() -> new UserNotFoundException(userId));
    }

}
