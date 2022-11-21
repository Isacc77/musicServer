package com.example.musicserver.mapper;

import com.example.musicserver.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User login(User loginUser);

    User selectByName(String username);
}
