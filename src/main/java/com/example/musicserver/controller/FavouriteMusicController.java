package com.example.musicserver.controller;

import com.example.musicserver.mapper.FavouriteMusicMapper;
import com.example.musicserver.model.Music;
import com.example.musicserver.model.User;
import com.example.musicserver.tools.MyConstant;
import com.example.musicserver.tools.ResponseBodyMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/favouritemusic")
public class FavouriteMusicController {

    @Resource
    private FavouriteMusicMapper favouriteMusicMapper;

    @RequestMapping("/myfavourite")
    public ResponseBodyMessage<Boolean> myFavourite(@RequestParam String id,
                                                    HttpServletRequest request) {
        int musicId = Integer.parseInt(id);
        System.out.println("musicId: " + musicId);

        // check login status
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null ||
                httpSession.getAttribute(MyConstant.USERINFO_SESSION_KEY) == null) {
            System.out.println("Please, login your account.");
            return new ResponseBodyMessage<>(-1,
                    "Please, login your account.", false);
        }

        User user = (User) httpSession.getAttribute(MyConstant.USERINFO_SESSION_KEY);
        int userId = user.getId();
        System.out.println("userId: " + userId);

        Music music = favouriteMusicMapper.findFavouriteMusic(userId, musicId);
        if (music != null) {
            return new ResponseBodyMessage<>(-1,
                    "The music is already in your favourite list ", false);
        } else {
            int ret = favouriteMusicMapper.insertFavouriteMusic(userId, musicId);
            if (ret == 1) {
                return new ResponseBodyMessage<>(0, "add successfully", true);
            } else {
                return new ResponseBodyMessage<>(-1, "add failed", false);
            }
        }
    }

    @RequestMapping("/findfavouritemusic")
    public ResponseBodyMessage<List<Music>> findFavouriteMusic(
            @RequestParam(required = false) String musicName,
            HttpServletRequest request) {

        // check login status
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null || httpSession.getAttribute(MyConstant.USERINFO_SESSION_KEY) == null) {
            System.out.println("Please, login your account.");
            return new ResponseBodyMessage<>(-1, "Before your query, login your account please.", null);
        }
        User user = (User) httpSession.getAttribute(MyConstant.USERINFO_SESSION_KEY);
        int userId = user.getId();
        System.out.println("userId: " + userId);

        List<Music> musicList = null;
        if (musicName == null) {
            musicList = favouriteMusicMapper.findFavouriteMusicByUserId(userId);
        } else {
            musicList = favouriteMusicMapper.findFavouriteMusicByKeyAndUId(musicName, userId);
        }
        return new ResponseBodyMessage<>(0, " query all the music information successfully", musicList);
    }

    @RequestMapping("/deletefavouritemusic")
    public ResponseBodyMessage<Boolean> deleteFavouriteMusic(
            @RequestParam String id, HttpServletRequest request) {
        int musicId = Integer.parseInt(id);

        HttpSession httpSession = request.getSession(false);
        if (httpSession == null || httpSession.getAttribute(MyConstant.USERINFO_SESSION_KEY) == null) {
            System.out.println("Please, login your account.");
            return new ResponseBodyMessage<>(-1, "Before your delete, login your account please.", null);
        }
        User user = (User) httpSession.getAttribute(MyConstant.USERINFO_SESSION_KEY);
        int userId = user.getId();
        System.out.println("userId: " + userId);

        int ret = favouriteMusicMapper.deleteFavouriteMusic(userId, musicId);
        if (ret == 1) {
            return new ResponseBodyMessage<>(0, "delete the song from your favourite list successfully", true);
        } else {
            return new ResponseBodyMessage<>(-1, " failed to delete", false);
        }
    }

}
