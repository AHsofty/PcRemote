package com.example.pccontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.pccontroller.com.example.pccontroller.VariableHolder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.SocketHandler;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText room = (EditText) findViewById(R.id.room);
        Button submit = (Button)findViewById(R.id.Submit);
        TextView tv = (TextView)findViewById(R.id.RC);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable rn = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (room.getText().toString().equals("") || room.getText().toString() == null) { // Makes sure we dont send an empty string.

                                return;
                            }


                            // We send the room code we try to connect to
                            DatagramSocket ds = new DatagramSocket();
                            String message = "CONNECTION_"+room.getText().toString();
                            byte[] room_bytes = message.getBytes();
                            InetAddress ia = InetAddress.getByName("YOUR SERVER IP");
                            DatagramPacket dp = new DatagramPacket(room_bytes, room_bytes.length, ia, 6969);
                            ds.send(dp);

                            // We get a response telling us whether the room we try to connect actually exists or not
                            byte[] response = new byte[30];
                            DatagramPacket response_packet = new DatagramPacket(response, response.length);
                            ds.receive(response_packet);
                            String resp = new String(response_packet.getData()).trim();
                            Log.d("lol", "Response is: "+resp.trim());

                            if (resp.equals("Error")) {
                                // The room we try to connect to doesn't exist
                                tv.setText("The given room does not exist, please try again...");

                            }
                            else if (resp.equals("Succes")){
                                // The room we're trying to connect to does exist
                                Log.d("lol", "SUCCES");
                                VariableHolder.SetRoomCode(room.getText().toString());
                                Intent NextScreen = new Intent(getApplicationContext(), Tracker.class);
                                startActivity(NextScreen);


                            }



                        }
                        catch (Exception ex) {
                            Log.d("lol", ex.toString());
                            return;

                        }
                    }
                };
                Thread t = new Thread(rn);
                t.start();
            }
        });




    }
}