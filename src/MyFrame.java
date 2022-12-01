import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class MyFrame extends JFrame {
    StandardSector sector;
    public MyFrame() {
        setSize(1080, 760);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        sector = new StandardSector(0, 0, this);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        /*BufferStrategy bufferStrategy = getBufferStrategy();
        if (bufferStrategy == null) {
            createBufferStrategy(2);
            bufferStrategy = getBufferStrategy();
        }
        g = bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, getWidth(), getHeight());*/

        int size_wight = sector.cells.size();
        int size_height = sector.cells.get(0).size();
        try {
            sector.randomGenerate(0, 0, size_height, size_wight, g);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //sector.paint(g);

        /*g.dispose();
        bufferStrategy.show();*/

    }
}