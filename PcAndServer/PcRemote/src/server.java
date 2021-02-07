import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class server
{
    public static Map<String, DatagramPacket> dictionary = new HashMap<String, DatagramPacket>();
    public static void main(String[] args) throws IOException, InterruptedException

    {
        DatagramSocket ds = new DatagramSocket(6969);
        int code = 0;

    while (true) {
        byte[] b1 = new byte[1024];
        DatagramPacket dp = new DatagramPacket(b1, b1.length);
        ds.receive(dp);
        InetAddress IP = dp.getAddress();
        String message = new String(dp.getData()).trim();

        // From here on it should be a seperate class, I'm just lazy.
        if (message.trim().equals("SetReceiver"))
        {
            code ++;
            String StringCode = Integer.toString(code);
            dictionary.put(StringCode, dp);
            byte[] msg = ("Succes your code is: "+code).getBytes();
            DatagramPacket PacketToSend = new DatagramPacket(msg, msg.length, IP, dp.getPort());
            ds.send(PacketToSend);

            ping(ds, StringCode); // Makes the server ping the PC every x seconds


        }
        if (message.contains("CONNECTION")) {

            // Phone is sending us a room to connect to
            String cde = message.split("_")[1];
            if (!dictionary.containsKey(cde))
            {
                // The room the phone tries to "connect" to doesn't exist
                InetAddress error_destination = dp.getAddress();
                byte[] answer = ("Error").getBytes();
                DatagramPacket error_packet = new DatagramPacket(answer, answer.length, error_destination, dp.getPort());
                ds.send(error_packet);


            }

            else {

                // The room our phone tries to connect to does exist
                InetAddress succes_destination = dp.getAddress();
                byte[] answer_succes = ("Succes").getBytes();
                DatagramPacket succes_packet = new DatagramPacket(answer_succes, answer_succes.length, succes_destination, dp.getPort());
                ds.send(succes_packet);

            }
        }

        if (!message.contains("SetReceiver") && !message.contains("CONNECTION")) {
            // A command was sent
            try {
                String room = message.split("/")[0].trim();
                byte[] ByteMessage = message.getBytes();
                DatagramPacket target_packet = dictionary.get(room);
                InetAddress TargetAdd = target_packet.getAddress();

                DatagramPacket packet = new DatagramPacket(ByteMessage, ByteMessage.length, TargetAdd, target_packet.getPort());
                ds.send(packet);

            }
            catch (Exception ex) {

            }



        }

    }

    }


    // So this function is very important
    // Basically if the PC doesn't receive anything after a while
    // it'll for some reason stop receiving packets.
    // this function keeps pinging the server
    // I am not sure what is causing this bug but this work-around seems to be working fine

    // It is important we do this in a different thread and there is a delay.
    // An "extra" packet every few seconds isn't going to make our program slow down a shitton
    // If we remove the delay it'll most likely have a huge impact on performance so let's not


    public static void ping(DatagramSocket ds, String room) {
        Thread pinger = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    while (true) {
                        byte[] ByteMessage = "ping".getBytes();
                        InetAddress target = dictionary.get(room).getAddress();
                        DatagramPacket packet = new DatagramPacket(ByteMessage, ByteMessage.length, target, dictionary.get(room).getPort());
                        ds.send(packet);
                        Thread.sleep(5000); // A delay so our program doesn't die

                    }

                }
                catch (Exception ex) {
                    System.out.println(ex);
                }

            }
        });
        pinger.start();

    }

}

// TO DO:
// Make the server check if the PC is still available, if not notify the phone
