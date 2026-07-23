package com.blog.controller;

import com.blog.dto.ApiResult;
import com.blog.dto.LoginRequest;
import com.blog.dto.RegisterRequest;
import com.blog.entity.User;
import com.blog.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ApiResult<User> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request);
        user.setPassword(null); // 返回时隐藏密码
        return ApiResult.success(user);
    }

    @PostMapping("/login")
    public ApiResult<User> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.login(request);
        if (user == null) {
            return ApiResult.error("用户名或密码错误");
        }
        user.setPassword(null);
        return ApiResult.success(user);
    }

    @GetMapping("/{id}")
    public ApiResult<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return ApiResult.error("用户不存在");
        }
        user.setPassword(null);
        return ApiResult.success(user);
    }
}
