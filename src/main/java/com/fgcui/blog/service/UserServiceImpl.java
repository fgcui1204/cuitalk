package com.fgcui.blog.service;

import com.fgcui.blog.po.User;
import com.fgcui.blog.repository.UserRepository;
import com.fgcui.blog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {

        User user = userRepository.findByUsernameAndPassword(username, MD5Utils.encode(password));
        return user;
    }
}
