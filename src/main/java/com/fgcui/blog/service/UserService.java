package com.fgcui.blog.service;

import com.fgcui.blog.po.User;

public interface UserService {
    User checkUser(String username, String password);
}
