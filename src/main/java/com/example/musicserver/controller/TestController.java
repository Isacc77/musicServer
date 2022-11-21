package com.example.musicserver.controller;

import com.example.musicserver.model.User;
import com.example.musicserver.tools.ResponseBodyMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/method")
    public ResponseBodyMessage<User> login(@RequestParam String username,
                                           @RequestParam String password) {

        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

        User user = new User();

        if (username.equals("bit") && password.equals("123456")) {
            user.setUsername(username);
            user.setPassword(password);
            return new ResponseBodyMessage<>(0, "login successfully", user);
        } else {
            return new ResponseBodyMessage<>(0, "login failed", null);
        }

    }

}
