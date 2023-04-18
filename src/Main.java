import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Main {
    static FileWriter writer;

    static {
        try {
            writer = new FileWriter("files\\fps.txt", false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Main() throws IOException {
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        Constants.TIME_START_PROGRAM = System.currentTimeMillis();
        MyFrame frame = new MyFrame();
        long time=System.currentTimeMillis();
        int fps=0;




        while (true){
            while (!Constants.RESTART_GAME){
                frame.repaint();
                Thread.sleep(10);
                fps++;
                if(System.currentTimeMillis()-time>=1000){
                    Integer FPS = fps;
                    writer.write(FPS.toString() + '\n');
                    writer.flush();
                    fps=0;
                    time=System.currentTimeMillis();
                }
            }
            frame.dispose();
            Constants.rebuild();
            Constants.TIME_START_PROGRAM = System.currentTimeMillis();
            frame = new MyFrame();
            time=System.currentTimeMillis();
            fps=0;
            Constants.RESTART_GAME = false;
        }
    }
}