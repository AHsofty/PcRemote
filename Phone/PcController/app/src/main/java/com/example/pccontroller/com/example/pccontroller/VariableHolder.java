package com.example.pccontroller.com.example.pccontroller;

public class VariableHolder {
    public static String room;

    public static synchronized void SetRoomCode(String code) {
        room = code;
    }

    public static synchronized String GetRoomCode() {
        return room;
    }




}
