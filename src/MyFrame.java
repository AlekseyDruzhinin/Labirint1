import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class MyFrame extends JFrame {
    StandardSector sector;
    UserHuman userHuman;
    public MyFrame() {
        setSize(1000, 760);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setExtendedState(MAXIMIZED_BOTH);
        Constants.R = this.getHeight()/Constants.COUNT_CELL_DOWN/2;
        //System.out.println(this.getHeight() + " " + Constants.R);
        sector = new StandardSector(Constants.SDVIG, Constants.SDVIG, this);
        //System.out.println(sector.cells.get(0).size() +" " + sector.cells.get(0).get(0).r);
        userHuman = new UserHuman(Constants.SDVIG+Constants.R, Constants.SDVIG+Constants.R, 0, 0, 10);
        System.out.println(userHuman.x + " " + userHuman.y);
    }

    @Override
    public void paint(Graphics g) {
        BufferStrategy bufferStrategy = getBufferStrategy();
        if (bufferStrategy == null) {
            createBufferStrategy(2);
            bufferStrategy = getBufferStrategy();
        }
        g = bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, getWidth(), getHeight());


        if (sector != null){
            sector.paint(g);
        }
        if (userHuman != null){
            userHuman.paint(g);
        }

        g.dispose();
        bufferStrategy.show();

    }
}