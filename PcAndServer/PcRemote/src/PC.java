
import java.awt.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

class PC extends Commands
{
    public static float ScrollMultiplier = 0.1f;
    public static void main(String[] args) throws IOException, AWTException
    {
        DatagramSocket ds = new DatagramSocket();

        String mes = "SetReceiver";
        byte[] b = (mes).getBytes();

        InetAddress ia = InetAddress.getByName("YOUR SERVER IP");
        DatagramPacket dp = new DatagramPacket(b,b.length, ia,6969);
        ds.send(dp);

        byte[] incoming_data = new byte[1024];
        DatagramPacket incoming = new DatagramPacket(incoming_data, incoming_data.length);
        ds.receive(incoming);
        String msg = new String(incoming.getData());
        System.out.println(msg);

        Robot rb = new Robot();

        while (true) {
            try
            {
                byte[] cmd_bytes = new byte[1024];
                DatagramPacket cmd = new DatagramPacket(cmd_bytes, cmd_bytes.length);

                ds.receive(cmd); // Our message in bytes
                String UnsplittedCoords = new String(cmd.getData()).trim(); // The .trim might cause problems or fix it
                if (!UnsplittedCoords.equals("ping"))
                {

                    Commands.SendCommand(UnsplittedCoords);
                }

            }
            catch (Exception ex) {
                System.out.println(ex);
            }

        }



    }


}
