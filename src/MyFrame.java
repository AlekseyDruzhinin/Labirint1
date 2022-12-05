import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class MyFrame extends JFrame {
    StandardSector sector;
    public MyFrame() {
        setSize(1000, 760);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setExtendedState(MAXIMIZED_BOTH);
        Constants.R = this.getHeight()/Constants.COUNT_CELL_DOWN/2;
        //System.out.println(this.getHeight() + " " + Constants.R);
        sector = new StandardSector(0, 0, this);
        //System.out.println(sector.cells.get(0).size() +" " + sector.cells.get(0).get(0).r);

    }

    @Override
    public void paint(Graphics g) {
        /*BufferStrategy bufferStrategy = getBufferStrategy();
        if (bufferStrategy == null) {
            createBufferStrategy(2);
            bufferStrategy = getBufferStrategy();
        }
        g = bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, getWidth(), getHeight());
        */

        if (sector != null){
            sector.paint(g);
        }

        /*
        g.dispose();
        bufferStrategy.show();
        */
    }
}