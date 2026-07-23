package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.dto.LoginRequest;
import com.blog.dto.RegisterRequest;
import com.blog.entity.User;

public interface UserService extends IService<User> {

    /**
     * 注册
     * @return 注册后的用户（不含密码）
     */
    User register(RegisterRequest request);

    /**
     * 登录
     * @return 登录成功返回用户，失败返回 null
     */
    User login(LoginRequest request);
}
