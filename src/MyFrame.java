import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class MyFrame extends JFrame implements  KeyEventDispatcher, MouseListener, MouseMotionListener {
    Labirint labirint;
    UserHuman userHuman;
    long timePriviosPrint;
    BufferedImage image;

    public MyFrame() throws IOException {
        addMouseListener(this);
        addMouseMotionListener(this);

        image = ImageIO.read(new File("data\\GameOver.png"));
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
        Constants.V_POLE = Constants.V_NORMAL/Constants.V_POLE_1;

        Constants.FRAME_WIGHT = getWidth();
        Constants.FRAME_HEIGHT = getHeight();
    }

    @Override
    public void paint(Graphics g) {
        long nowTime = System.currentTimeMillis();
        //System.out.println(nowTime + " " + (nowTime - timePriviosPrint) + " " + Constants.V_NORMAL);
        BufferStrategy bufferStrategy = getBufferStrategy();
        if (bufferStrategy == null) {
            createBufferStrategy(2);
            bufferStrategy = getBufferStrategy();
        }
        g = bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, getWidth(), getHeight());


        if (Constants.USER_DIED){

            g.drawImage(image, this.getWidth()/2-this.getHeight()/2, 0, this.getHeight(), this.getHeight(), null);

        }else{
            if (nowTime - Constants.TIME_START_PROGRAM > Constants.TIME_TO_DIED_LABIRINT){
                labirint.update(nowTime - timePriviosPrint);
            }

            if (userHuman != null){
                userHuman.update(labirint);
                Point gMP = getMousePosition();
                if (gMP != null){
                    Vector vectorMouse = new Vector(-(double)gMP.x+userHuman.x, -(double)gMP.y + userHuman.y);
                    Vector vectorUp = new Vector(0.0, 1.0);
                    userHuman.rotate(Math.atan2(vectorUp.vectorComposition(vectorMouse), vectorUp.scalarComposition(vectorMouse)));
                    //System.out.println(vectorMouse.x + " " + vectorMouse.y);
                }
            }

            if (labirint != null){
                labirint.paint(g);
                labirint.goBullets(nowTime - timePriviosPrint);
            }
            if (userHuman != null){
                userHuman.go(labirint, nowTime - timePriviosPrint);
                if (Constants.DEVELORER){
                    g.setColor(Color.GREEN);
                    g.fillRect((int)labirint.getCell(userHuman.indexSector, userHuman.i, userHuman.j).x, (int)labirint.getCell(userHuman.indexSector    , userHuman.i, userHuman.j).y, Constants.R, Constants.R);
                    g.setColor(Color.RED);
                }
                userHuman.paint(g);
            }

            timePriviosPrint = nowTime;
        }

        g.dispose();
        bufferStrategy.show();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        // w - 87
        // a - 65
        // s - 83
        // d - 68
        // p - 80
        // r - 82
        if (e.getID() == KeyEvent.KEY_PRESSED){
            //System.out.println(e.getKeyCode());
            if (e.getKeyCode() == 82){
                if (Constants.DEVELORER == false){
                    Constants.DEVELORER = true;
                    //System.out.println(Constants.DEVELORER);
                }else{
                    Constants.DEVELORER = false;
                    userHuman.flagChit = false;
                    Constants.V_NORMAL_1 = 3000;
                    Constants.V_NORMAL = (double) getHeight() / (double) Constants.V_NORMAL_1;
                    userHuman.vHuman = Constants.V_NORMAL;
                }
            }
            if (Constants.DEVELORER){
                if (e.getKeyCode() == 80){
                    userHuman.flagChit = !userHuman.flagChit;
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
            if (e.getKeyCode() == 83){
                userHuman.flagDown = true;
            }
            if (e.getKeyCode() == 87){
                userHuman.flagUp = true;
            }
            if (e.getKeyCode() == 65){
                userHuman.flagLeft = true;
            }
            if (e.getKeyCode() == 68){
                userHuman.flagRight = true;
            }
        }

        if (e.getID() == KeyEvent.KEY_RELEASED){
            if (e.getKeyCode() == 83){
                userHuman.flagDown = false;
            }
            if (e.getKeyCode() == 87){
                userHuman.flagUp = false;
            }
            if (e.getKeyCode() == 65){
                userHuman.flagLeft = false;
            }
            if (e.getKeyCode() == 68){
                userHuman.flagRight = false;
            }

        }
        return false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        StandardBullet bullet = new StandardBullet(userHuman.x, userHuman.y, new Vector(e.getX()-userHuman.x, e.getY()-userHuman.y), userHuman);
        labirint.addBullet(bullet);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
//        Vector vectorMouse = new Vector(-(double)e.getX()+userHuman.x, -(double)e.getY() + userHuman.y);
//        Vector vectorUp = new Vector(0.0, 1.0);
//        userHuman.rotate(Math.atan2(vectorUp.vectorComposition(vectorMouse), vectorUp.scalarComposition(vectorMouse)));
//        System.out.println(vectorMouse.x + " " + vectorMouse.y);
    }
}