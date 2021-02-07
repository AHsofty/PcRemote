import java.awt.*;
import java.awt.event.InputEvent;

public class Commands
{
    public static float ScrollMultiplier = 0.1f;


    public static void SendCommand(String command) throws AWTException
    {
        Robot rb = new Robot();
        String code = command.split("/")[0].trim();
        String cmd = command.split("/")[1].trim();
        String data = command.split("/")[2].trim();


        switch (cmd) {
            case "MOVE":
                Point p = MouseInfo.getPointerInfo().getLocation();
                String[] parts = data.split("_");
                float x = Float.parseFloat(parts[0]);
                float y = Float.parseFloat(parts[1]);
                float newX = p.x+x;
                float newY = p.y-y;
                rb.mouseMove(Math.round(newX), Math.round(newY));

                break;

            case "DOWN":
                rb.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                break;

            case "UP":
                rb.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                break;

            case "SCROLL":
                // MOVES IN THE y-direction
                String Δy = data.split("_")[1];
                if (Float.parseFloat(Δy) > 0) {
                    // Move up
                    for (int i = 0; i < Math.round(Float.parseFloat(Δy) * ScrollMultiplier); i++) {
                        rb.mouseWheel(1);

                    }
                }
                else if (Float.parseFloat(Δy) < 0) {
                    // Move down
                    for (int i = 0; i > Math.round(Float.parseFloat(Δy) * ScrollMultiplier); i--) {
                        rb.mouseWheel(-1);

                    }
                }


                break;

        }

    }

}
