package com.example.pccontroller.com.example.pccontroller;


public class MovementHandler {
    static float previousX = -696969;
    static float previousY = -696969;

    public static String calculate(float x, float y) {
        if (previousX == -696969 && previousY == -696969) {
            previousX = x;
            previousY = y;
            return "";

        }
        else {
            float newX = x-previousX;
            float newY = previousY-y;

            previousX = x;
            previousY = y;

            return newX+"_"+newY;

        }
    }

    public static void reset() {
        previousX = -696969;
        previousY=-696969;
    }





}
