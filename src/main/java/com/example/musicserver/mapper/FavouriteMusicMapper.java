package com.example.musicserver.mapper;

import com.example.musicserver.model.Music;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;

@Mapper
public interface FavouriteMusicMapper {

    /**
     * query favourite music by music ID and User ID
     *
     * @param userId
     * @param musicId
     * @return
     */
    Music findFavouriteMusic(int userId, int musicId);

    /**
     * add music to favourite list
     *
     * @param userId
     * @param musicId
     * @return
     */
    int insertFavouriteMusic(int userId, int musicId);

    /**
     * query all the favourite music
     *
     * @param userId
     * @return
     */
    List<Music> findFavouriteMusicByUserId(int userId);

    /**
     * query target favourite music by music name (support fuzzy query)
     *
     * @param musicName
     * @param userId
     * @return
     */
    List<Music> findFavouriteMusicByKeyAndUId(String musicName, int userId);

    /**
     * remove target favourite song
     *
     * @param userId
     * @param musicId
     * @return
     */
    int deleteFavouriteMusic(int userId, int musicId);

    /**
     * delete the music by its id
     *
     * @param musicId
     * @return
     */
    int deleteFavouriteMusicById(int musicId);

}
