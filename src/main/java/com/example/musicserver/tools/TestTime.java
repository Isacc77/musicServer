package com.example.musicserver.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestTime {
    public static void main(String[] args) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sf.format(new Date());
        System.out.println("Current time: " + time );

        SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time2 = sf2.format(new Date());
        System.out.println("Current time: " + time2 );
    }
}
