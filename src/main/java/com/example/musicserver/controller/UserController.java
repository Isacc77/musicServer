package com.example.musicserver.controller;

import com.example.musicserver.mapper.UserMapper;
import com.example.musicserver.model.User;
import com.example.musicserver.tools.MyConstant;
import com.example.musicserver.tools.ResponseBodyMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping("/login")
    public ResponseBodyMessage<User> login(@RequestParam String username, @RequestParam String password,
                                           HttpServletRequest request){

        User user = userMapper.selectByName(username);

        if(user == null){
            System.out.println("login failed");
            return new ResponseBodyMessage<>(-1, "password or username incorrect!", user);
        }else{
            boolean flg = bCryptPasswordEncoder.matches(password, user.getPassword());
            if(!flg){
                return new ResponseBodyMessage<>(-1,
                        "password or username incorrect!", user);
            }
            request.getSession().setAttribute(MyConstant.USERINFO_SESSION_KEY, user);
            return new ResponseBodyMessage<>(0, "login successfully", user);
        }
    }
}
