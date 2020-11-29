package com.example.aawaz;

public class MessageData {

    public static final int TYPE_RECEIVE = 1;
    public static final int TYPE_SEND = 2;

    private String message;
    private int type;
    private long time;


    public MessageData(String message, int type, long time) {

        this.message = message;
        this.type = type;
        this.time=time;
    }

    public String getMessage() {
        return this.message;
    }

    public int getType() {
        return this.type;
    }

    public long getTime(){
        return this.time;
    }
}
