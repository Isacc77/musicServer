package com.example.musicserver.controller;

import com.example.musicserver.mapper.FavouriteMusicMapper;
import com.example.musicserver.mapper.MusicMapper;
import com.example.musicserver.model.Music;
import com.example.musicserver.model.User;
import com.example.musicserver.tools.MyConstant;
import com.example.musicserver.tools.ResponseBodyMessage;
import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/music")

public class MusicController {
    @Value("${music.local.path}")
    private String SAVE_PATH;

    @Autowired
    private MusicMapper musicMapper;

    @Resource
    private FavouriteMusicMapper favouriteMusicMapper;

    @RequestMapping("/upload")
    public ResponseBodyMessage<Boolean> insertMusic(@RequestParam String singer,
                                                    @RequestParam("filename") MultipartFile file,
                                                    HttpServletRequest request,
                                                    HttpServletResponse resp) {
        // 1. check login status
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null ||
                httpSession.getAttribute(MyConstant.USERINFO_SESSION_KEY) == null) {
            System.out.println("Please, login your account.");
            return new ResponseBodyMessage<>(-1,
                    "Please, login your account.", false);
        }

        // 1.5 check database whether already exist the music
        // check music name and singer
        String fileNameAndType = file.getOriginalFilename();

        int index = fileNameAndType.lastIndexOf(".");
        String title = fileNameAndType.substring(0, index);

        Music music = musicMapper.selectByMusic(title,singer);
        if (music!=null && music.getTitle().equals(title) && music.getSinger().equals(singer)){
            return new ResponseBodyMessage<>(-1,"Your music list already contains this song!",false);
        }

        // 2.upload to server
         //xxx.mp3

        System.out.println("fileNameAndType: " + fileNameAndType);

        String path = SAVE_PATH + fileNameAndType;

        File dest = new File(path);

        if (!dest.exists()) {
            dest.mkdir();
        }

        try {
            file.transferTo(dest);//upload file to dest
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseBodyMessage<>(-1, "Upload to server failed！", false);
        }

        //3. data upload to db
        // 1. prepare data 2. use insert method

        User user = (User) httpSession.getAttribute(MyConstant.USERINFO_SESSION_KEY);
        int userid = user.getId();

        //play music  -> http request
        String url = "/music/get?path=" + title;

        //get uploading time
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sf.format(new Date());

        try {
            int ret = 0;
            ret = musicMapper.insert(title, singer, time, url, userid);
            if (ret == 1) {
                //redirect to music list
                resp.sendRedirect("/list.html");
                return new ResponseBodyMessage<>(0, "Upload to db successfully！", true);
            } else {
                return new ResponseBodyMessage<>(-1, "Upload to db failed！", false);
            }
        } catch (BindingException | IOException e) {
            dest.delete();
            return new ResponseBodyMessage<>(-1, "Upload to db failed！", false);
        }
    }

    @RequestMapping("/get")
    public ResponseEntity<byte[]> func(String path){
        File file = new File(SAVE_PATH+ "/" + path);
        byte[] a = null;
        try {
            a = Files.readAllBytes(file.toPath());
            if(a == null) return ResponseEntity.badRequest().build();
            return ResponseEntity.ok(a);
        } catch (IOException e ){
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * delete a song
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public ResponseBodyMessage<Boolean> deleteMusicById(@RequestParam String id){
        // 1. check music whether exists
        int musicId = Integer.parseInt(id);

        // 2. exists and delete
        Music music = musicMapper.findMusicById(musicId);
        if(music == null){
            System.out.println("Target ID doesn't exists");
            return new ResponseBodyMessage<>(-1, "Server didn't find the target", false);
        }else{
            // 2.1 delete the data in database
            int ret = musicMapper.deleteMusicById(musicId);
            if(ret == 1){
                // 2.2 delete the data in server
                int index = music.getUrl().lastIndexOf("=");
                String fileName = music.getUrl().substring(index + 1);

                File file = new File(SAVE_PATH + "/" + fileName +".mp3");
                System.out.println("Current path: " + file.getPath());

                if(file.delete()){
                    //single: At the same time, we need to delete the music in the favourite list
                    favouriteMusicMapper.deleteFavouriteMusicById(musicId);
                    return new ResponseBodyMessage<>(0, "delete the target in server successfully", true);
                } else{
                    return new ResponseBodyMessage<>(-1, "delete the target in server failed", false);
                }
            }else {
                return new ResponseBodyMessage<>(-1, "fail to delete the target", false);
            }
        }
    }

    /**
     * delete multiple targets at once
     * @param id
     * @return
     */
    @RequestMapping("deleteSel")
    public ResponseBodyMessage<Boolean> deleteSelMusic(@RequestParam("id[]") List<Integer> id){
        System.out.println("id[]: " + id);
        int sum = 0;
        for(int i = 0; i < id.size(); i++){
            int musicId = id.get(i);
            Music music = musicMapper.findMusicById(musicId);
            if(music == null){
                System.out.println("Target ID doesn't exists");
                return new ResponseBodyMessage<>(-1, "Server didn't find the target", false);
            }
            int ret = musicMapper.deleteMusicById(musicId);
            if(ret == 1){
                // delete the data in server
                int index = music.getUrl().lastIndexOf("=");
                String fileName = music.getUrl().substring(index + 1);
                File file = new File(SAVE_PATH + "/" + fileName +".mp3");
                if(file.delete()){
                    //multiple: At the same time, we need to delete the music in the favourite list
                    favouriteMusicMapper.deleteFavouriteMusicById(musicId);
                    sum += ret;
                } else{
                    return new ResponseBodyMessage<>(-1, "delete the target in server failed", false);
                }
            } else {
                return new ResponseBodyMessage<>(-1, "delete the target in database failed", false);
            }
        }
        if(sum == id.size()){
            System.out.println("delete successfully");
            return new ResponseBodyMessage<>(0, "delete the targets in server and database successfully", true);
        }else{
            System.out.println("delete failed");
            return new ResponseBodyMessage<>(-1, "delete the target failed", false);
        }
    }

    /**
     * Based on param to query music, param can be null(query all the music).
     * @param musicName
     * @return
     */
    @RequestMapping("/findmusic")
    public ResponseBodyMessage<List<Music>> findMusic(@RequestParam(required = false) String musicName) {
        List<Music> musicList = null;
        if(musicName != null){
            musicList = musicMapper.findMusicByName(musicName);
        } else{
            musicList = musicMapper.findAllMusic();
        }
        return new ResponseBodyMessage<>(0, "query the music successfully", musicList);
    }

}