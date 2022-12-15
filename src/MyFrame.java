import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.sql.Date;
import java.util.ArrayList;

public class MyFrame extends JFrame implements  KeyEventDispatcher{
    Labirint labirint;
    UserHuman userHuman;
    long timePriviosPrint;

    public MyFrame() {
        timePriviosPrint = System.currentTimeMillis();
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);

        setSize(1000, 760);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setExtendedState(MAXIMIZED_BOTH);

        Constants.R = this.getHeight()/Constants.COUNT_CELL_DOWN/2;
        Constants.V_NORMAL = (double) getHeight() / (double) Constants.V_NORMAL_1;
        //System.out.println(this.getHeight() + " " + Constants.R);
        labirint = new Labirint(this);
        //System.out.println(sector.cells.get(0).size() +" " + sector.cells.get(0).get(0).r);
        userHuman = new UserHuman(Constants.SDVIG+Constants.R, Constants.SDVIG+Constants.R, 0, 0);
        //System.out.println(userHuman.x + " " + userHuman.y);
        Constants.V_POLE = Constants.V_NORMAL/2.0;
    }

    @Override
    public void paint(Graphics g) {
        long nowTime = System.currentTimeMillis();
        System.out.println(nowTime + " " + (nowTime - timePriviosPrint) + " " + Constants.V_NORMAL);
        BufferStrategy bufferStrategy = getBufferStrategy();
        if (bufferStrategy == null) {
            createBufferStrategy(2);
            bufferStrategy = getBufferStrategy();
        }
        g = bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, getWidth(), getHeight());

        if (labirint != null){
            labirint.paint(g);
        }
        if (userHuman != null){
            userHuman.go(labirint, nowTime - timePriviosPrint);
            userHuman.paint(g);
        }

        timePriviosPrint = nowTime;

        g.dispose();
        bufferStrategy.show();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED){
            if (e.getKeyChar() == 'p'){
                userHuman.flagChit = !userHuman.flagChit;
            }

            if (e.getKeyChar() == 's'){
                userHuman.flagDown = true;
            }
            if (e.getKeyChar() == 'w'){
                userHuman.flagUp = true;
            }
            if (e.getKeyChar() == 'a'){
                userHuman.flagLeft = true;
            }
            if (e.getKeyChar() == 'd'){
                userHuman.flagRight = true;
            }

            if (e.getKeyChar() == '+'){
                Constants.V_NORMAL_1 -= 100;
                Constants.V_NORMAL = (double) getHeight() / (double) Constants.V_NORMAL_1;
                userHuman.vHuman = Constants.V_NORMAL;
            }
            if (e.getKeyChar() == '-'){
                Constants.V_NORMAL_1 += 100;
                Constants.V_NORMAL = (double) getHeight() / (double) Constants.V_NORMAL_1;
                userHuman.vHuman = Constants.V_NORMAL;
            }
        }

        if (e.getID() == KeyEvent.KEY_RELEASED){
            if (e.getKeyChar() == 's'){
                userHuman.flagDown = false;
            }
            if (e.getKeyChar() == 'w'){
                userHuman.flagUp = false;
            }
            if (e.getKeyChar() == 'a'){
                userHuman.flagLeft = false;
            }
            if (e.getKeyChar() == 'd'){
                userHuman.flagRight = false;
            }

        }
        return false;
    }
}