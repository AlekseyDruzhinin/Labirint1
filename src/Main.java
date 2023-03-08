import java.io.IOException;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        Constants.TIME_START_PROGRAM = System.currentTimeMillis();
        MyFrame frame = new MyFrame();
        while (true){
            frame.repaint();

            //Thread.sleep(10000);
        }
    }
}