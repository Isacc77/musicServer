package com.example.musicserver.tools;

import lombok.Data;

@Data
public class ResponseBodyMessage<T> {
    private int status; //status code,0 or 1 represent success, -1 represent failed
    private String message; // status information
    private T data; // the data return to frontend

    public ResponseBodyMessage(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
