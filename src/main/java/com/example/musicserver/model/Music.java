package com.example.musicserver.model;

import lombok.Data;

@Data
public class Music {
    private int id;
    private int userid;
    private String title;
    private String singer;
    private String time;
    private String url;
}
