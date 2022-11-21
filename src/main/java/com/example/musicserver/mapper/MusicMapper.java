package com.example.musicserver.mapper;

import com.example.musicserver.model.Music;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MusicMapper {

    /**
     * Insert music
     *
     * @param title
     * @param singer
     * @param time
     * @param url
     * @param userid
     * @return
     */
    int insert(String title, String singer, String time, String url, int userid);

    /**
     * query music based on title and singer
     *
     * @param title
     * @param singer
     * @return
     */
    Music selectByMusic(String title, String singer);

    /**
     * check music id whether already exists.
     *
     * @param id
     * @return
     */
    Music findMusicById(int id);

    /**
     * delete music with target ID
     *
     * @param musicId
     * @return
     */
    int deleteMusicById(int musicId);

    /**
     * query all the music
     *
     * @return
     */
    List<Music> findAllMusic();

    /**
     * query the target music
     *
     * @param title
     * @return
     */
    List<Music> findMusicByName(String title);

}
