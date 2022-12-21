import java.io.IOException;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        Constants.TIME_START_PROGRAM = System.currentTimeMillis();
        MyFrame frame = new MyFrame();
        while (true){
            frame.repaint();

            //Thread.sleep(10000);
        }
        //System.out.println("End");
    }
}