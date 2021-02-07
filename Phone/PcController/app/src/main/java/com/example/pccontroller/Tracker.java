package com.example.pccontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.pccontroller.com.example.pccontroller.MovementHandler;
import com.example.pccontroller.com.example.pccontroller.VariableHolder;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Tracker extends AppCompatActivity {
    long startTime = -69;
    boolean holding=false;
    boolean scrolling=false;
    String room = VariableHolder.GetRoomCode();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        Button submit = (Button)findViewById(R.id.Back);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread UwU = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            
                            Intent activity_main = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(activity_main);
                            return;
                        }
                        catch (Exception ex) {Log.d("lol", ex.toString());}


                    }
                });
                UwU.start();


            }
        });


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            float x = (int)event.getX();
            float y = (int)event.getY();
            final int MAX_DURATION = 300;

            switch (event.getAction() & MotionEvent.ACTION_MASK){
                case MotionEvent.ACTION_MOVE:
                    String movement = MovementHandler.calculate(x,y);
                    int touches = event.getPointerCount();
                    if (movement.contains("_") && scrolling == false) {
                            send_packet(room+"/"+"MOVE/"+movement);
                    }


                    if (movement.contains("_") && scrolling==true && touches == 2) {
                        send_packet(room+"/SCROLL/"+movement);
                    }

                    break;

                case MotionEvent.ACTION_UP:
                    scrolling=false;
                    MovementHandler.reset();
                    if (holding) {
                        holding=false;
                        send_packet(room+"/UP/"+"-1"); // As a third "argument" we send -1, this'll tell the server/pc that the third argument is useless
                    }
                    break;

                case MotionEvent.ACTION_DOWN:
                    // This bit was mostly inspired by this article
                    //https://stackoverflow.com/questions/7314579/how-to-catch-double-tap-events-in-android-using-ontouchlistener
                    // This still needs some improvement
                    Log.d("lol", "Down");

                    if (startTime != -69) {
                        if (System.currentTimeMillis() - startTime <= MAX_DURATION) {
                            // Double click
                            holding=true;
                            Log.d("lol", "Double click");
                            send_packet(room+"/DOWN/"+"-1");
                            startTime = System.currentTimeMillis();
                        }
                        else {
                            Log.d("lol", "No double click");
                            // No double click
                            startTime = System.currentTimeMillis();

                        }


                    }
                    else  {
                        startTime = System.currentTimeMillis();
                    }

                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                    if (event.getPointerCount() == 2) { // Gotta love unnecesary if statements
                        scrolling=true;

                    }


                    break;


            }
        }

        catch (Exception ex) {Log.d("lol",ex.toString());}

        return false;
    }


    void send_packet(String movement) {
            Runnable rn = new Runnable() {
                @Override
                public void run() {
                    try {
                        // Do the networking here....


                        DatagramSocket ds = new DatagramSocket();
                        String message = movement;
                        byte[] room_bytes = message.getBytes();
                        InetAddress ia = InetAddress.getByName("YOUR SERVER IP");
                        DatagramPacket dp = new DatagramPacket(room_bytes, room_bytes.length, ia, 6969);
                        ds.send(dp);

                    }
                    catch (Exception ex) {Log.d("lol", ex.toString());}

                }
            };
            Thread t = new Thread(rn);
            t.start();



    }




}
