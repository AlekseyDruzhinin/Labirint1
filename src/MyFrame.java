import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class MyFrame extends JFrame implements KeyEventDispatcher, MouseListener, MouseMotionListener {
    Labirint labirint;
    UserHuman userHuman;
    long timePriviosPrint;
    BufferedImage imageBloodBackground;

    BufferedImage image;
    BufferedImage imageClock;
    BufferedImage imageCntBots;
    BufferedImage imageCntWay;

    BufferedImage imageBackGround;
    BufferedImage imageBackGround1;
    BufferedImage imageEnterBackGround;
    ArrayList<Buttom> buttoms = new ArrayList<>();

    AffineTransform tx;
    AffineTransformOp op;

    public MyFrame() throws IOException {
        this.imageBloodBackground = ImageIO.read(new File("data\\blood_background.png"));
        imageBackGround = ImageIO.read(new File("data\\Sky.jpg"));
        imageBackGround1 = ImageIO.read(new File("data\\Sand1.jpg"));
        imageEnterBackGround = ImageIO.read(new File("data\\ImageBackGround2.jpg"));

        addMouseListener(this);
        addMouseMotionListener(this);

        image = ImageIO.read(new File("data\\GameOver.png"));
        imageClock = ImageIO.read(new File("data\\clock.png"));
        imageCntBots = ImageIO.read(new File("data\\BotForCnt.png"));
        imageCntWay = ImageIO.read(new File("data\\ForCntWay.png"));
        timePriviosPrint = System.currentTimeMillis();
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);

        setSize(1000, 760);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setExtendedState(MAXIMIZED_BOTH);
        Constants.R = this.getHeight() / Constants.COUNT_CELL_DOWN / 2;
        Constants.V_NORMAL = (double) getHeight() / (double) Constants.V_NORMAL_1;
        //System.out.println(this.getHeight() + " " + Constants.R);
        labirint = new Labirint(this);
        //System.out.println(sector.cells.get(0).size() +" " + sector.cells.get(0).get(0).r);
        userHuman = new UserHuman(Constants.SDVIG + Constants.R, Constants.SDVIG + Constants.R, 0, 0);
        //System.out.println(userHuman.x + " " + userHuman.y);
        Constants.V_POLE = Constants.V_NORMAL / Constants.V_POLE_1;

        Constants.FRAME_WIGHT = getWidth();
        Constants.FRAME_HEIGHT = getHeight();

        Constants.SIZE_BULLET = getHeight() / 100;

        Constants.V_BOTS = 0.5 * Constants.V_NORMAL;

        Constants.SQRT_LEN_AIM = (double) (Constants.R * Constants.R);
        Constants.V_BULLET = 4.0 * Constants.V_NORMAL;

        Constants.CNT_DIED_BOTS = new MyString(0);
        Constants.CNT_WAY = new MyString(0);

        buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2, getHeight() / 2, "data\\ButtomStart.png"));
        buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2, getHeight() / 2 + getWidth() / 28.0 * 5 / 4, "data\\ButtomRecords.png"));
        buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2, getHeight() / 2 + getWidth() / 28.0 * 2.0 * 5 / 4, "data\\ButtomSetting.png"));
        buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2, getHeight() / 2 + getWidth() / 28.0 * 3.0 * 5 / 4, "data\\ButtomInfo.png"));

        {
            FileReader reader = new FileReader("files\\Constants.txt");
            Constants.MUST_PLAY_MUSIC = (reader.read() == '1');
            Constants.MUST_PLAY_SOUND = (reader.read() == '1');
        }
        Constants.writeConstants();
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


        if (!Constants.START_GAME) {
            if (Constants.MUSIC_GAME == 0) {
                Constants.MUSIC_GAME = 1;
                new Thread(() -> {
                    Sound soundMenu = new Sound(new File("data\\music\\Giornos_Theme.wav"));
                    soundMenu.setVolume((float) 0.75);
//                    soundMenu.play();
                    while (!Constants.START_GAME) {
                        if (Constants.MUST_PLAY_MUSIC){
                            if (!soundMenu.isPlaying()) {
                                System.out.println("..");
                                soundMenu.play();
                            }
                        }else{
                            soundMenu.stop();
                        }
                    }
                    Constants.MUSIC_GAME = 0;
                    Constants.TIME_START_PROGRAM = System.currentTimeMillis();
                    soundMenu.stop();
                }).start();
            }
            g.drawImage(imageEnterBackGround, 0, 0, this.getWidth(), this.getHeight(), null);
            for (Buttom buttom : buttoms) {
                if (buttom != null) {
                    buttom.paint(g, getMousePosition());
                }
            }

        } else {
            g.drawImage(imageBackGround, 0, 0, this.getWidth(), this.getHeight(), null);
            if (System.currentTimeMillis() - Constants.TIME_HIT <= Constants.TIME_LIFE_BACKGROUND_BLOOD) {
                g.drawImage(imageBloodBackground, 0, 0, this.getWidth(), this.getHeight(), null);
            }
            if (Constants.USER_DIED) {
                if (Constants.GAME_OVER == 0) {
                    if (Constants.MUST_PLAY_SOUND){
                        new Thread(() -> {
//                        Sound.playSound("data\\music\\Game_over.wav");
                            Sound sound = new Sound(new File("data\\music\\Game_over.wav"));
                            sound.play();
                        }).start();
                    }
                    Constants.GAME_OVER++;
                }
                g.drawImage(image, this.getWidth() / 2 - this.getHeight() / 2, 0, this.getHeight(), this.getHeight(), null);
                g.drawImage(imageBloodBackground, 0, 0, this.getWidth(), this.getHeight(), null);
            } else {
                if (Constants.MUSIC_GAME == 0) {
                    Constants.MUSIC_GAME = 1;
                    new Thread(() -> {
                        Sound soundGame = new Sound(new File("data\\music\\Music.wav"));
                        soundGame.setVolume((float) 0.65);
                        Sound soundDied = new Sound(new File("data\\music\\End.wav"));
                        soundDied.setVolume((float) 0.65);
                        while (true) {
                            if (!soundGame.isPlaying() && !Constants.USER_DIED && Constants.MUST_PLAY_MUSIC) {
                                soundGame.play();
                                soundDied.stop();
                            }
                            if (Constants.USER_DIED && Constants.MUST_PLAY_MUSIC) {
                                soundGame.stop();
                            }
                            if (Constants.USER_DIED && !soundDied.playing && Constants.MUST_PLAY_MUSIC) {
                                soundDied.play();
                            }
                            if (!Constants.MUST_PLAY_MUSIC){
                                soundGame.stop();
                                soundDied.stop();
                            }
                        }
                    }).start();
                }
                if (nowTime - Constants.TIME_START_PROGRAM > Constants.TIME_TO_DIED_LABIRINT) {
                    try {
                        labirint.update(nowTime - timePriviosPrint, userHuman, g);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                if (userHuman != null) {
                    userHuman.update(labirint);
                    Point gMP = getMousePosition();
                /*if (gMP != null){
                    Vector vectorMouse = new Vector(-(double)gMP.x+userHuman.x, -(double)gMP.y + userHuman.y);
                    Vector vectorUp = new Vector(0.0, 1.0);
                    userHuman.rotate(Math.atan2(vectorUp.vectorComposition(vectorMouse), vectorUp.scalarComposition(vectorMouse)));
                    //System.out.println(vectorMouse.x + " " + vectorMouse.y);
                }*/
                    userHuman.rotateToAim(gMP);
                }

                if (labirint != null) {
                    labirint.paint(g);
                    long mega_bufer = nowTime - timePriviosPrint;
                    labirint.goBullets(mega_bufer, g, userHuman);
                }
                if (userHuman != null) {
                    try {
                        userHuman.go(labirint, nowTime - timePriviosPrint);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (Constants.DEVELORER) {
                        g.setColor(Color.GREEN);
                        g.fillRect((int) labirint.getCell(userHuman.indexSector, userHuman.i, userHuman.j).x, (int) labirint.getCell(userHuman.indexSector, userHuman.i, userHuman.j).y, Constants.R, Constants.R);
                        g.setColor(Color.RED);
                    }
                    userHuman.paint(g);
                }

                g.drawImage(imageClock, this.getWidth() - 150 - Constants.R * 5 / 4, Constants.SDVIG / 15 * 7 - Constants.R / 8, Constants.R * 5 / 4, Constants.R * 5 / 4, null);
                g.drawString(Time.timeToString(), this.getWidth() - 150, Constants.SDVIG / 8 * 5 + Constants.R / 2);
                g.drawImage(imageCntBots, this.getWidth() - 150 - Constants.R * 6 - Constants.R * 5 / 4, Constants.SDVIG / 15 * 7 - Constants.R / 8, Constants.R * 5 / 4, Constants.R * 5 / 4, null);
                g.drawString(Constants.CNT_DIED_BOTS.getString(), this.getWidth() - 150 - Constants.R * 6, Constants.SDVIG / 8 * 5 + Constants.R / 2);
                g.drawImage(imageCntWay, this.getWidth() - 150 - Constants.R * 12 - Constants.R * 5 / 4, Constants.SDVIG / 15 * 7 - Constants.R / 8, Constants.R * 5 / 4, Constants.R * 5 / 4, null);
                g.drawString(Constants.CNT_WAY.getString(), this.getWidth() - 150 - Constants.R * 12, Constants.SDVIG / 8 * 5 + Constants.R / 2);

                timePriviosPrint = nowTime;
            }
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
        // q - 81
        // ' ' - 32
//        System.out.println(e.getKeyCode());
        if (e.getID() == KeyEvent.KEY_PRESSED) {
//            System.out.println(e.getKeyCode());
            if (e.getKeyCode() == 32 && !labirint.boom.flag) {
                try {
                    labirint.addBoom(userHuman);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if (Constants.DEVELORER && e.getKeyCode() == 66) {
                Constants.FLAG_CHIT_BULLET = !Constants.FLAG_CHIT_BULLET;
            }
            if (e.getKeyCode() == 82) {
                if (Constants.DEVELORER == false) {
                    Constants.DEVELORER = true;
                    //System.out.println(Constants.DEVELORER);
                } else {
                    Constants.DEVELORER = false;
                    userHuman.flagChit = false;
                    Constants.V_NORMAL_1 = 3000;
                    Constants.V_NORMAL = (double) getHeight() / (double) Constants.V_NORMAL_1;
                    userHuman.vHuman = Constants.V_NORMAL;
                }
            }
            if (Constants.DEVELORER) {
                if (e.getKeyCode() == 80) {
                    userHuman.flagChit = !userHuman.flagChit;
                }

                if (e.getKeyChar() == '+') {
                    Constants.V_NORMAL_1 -= 100;
                    Constants.V_NORMAL = (double) getHeight() / (double) Constants.V_NORMAL_1;
                    userHuman.vHuman = Constants.V_NORMAL;
                }
                if (e.getKeyChar() == '-') {
                    Constants.V_NORMAL_1 += 100;
                    Constants.V_NORMAL = (double) getHeight() / (double) Constants.V_NORMAL_1;
                    userHuman.vHuman = Constants.V_NORMAL;
                }
            }
            if (e.getKeyCode() == 83) {
                userHuman.flagDown = true;
            }
            if (e.getKeyCode() == 87) {
                userHuman.flagUp = true;
            }
            if (e.getKeyCode() == 65) {
                userHuman.flagLeft = true;
            }
            if (e.getKeyCode() == 68) {
                userHuman.flagRight = true;
            }
        }

        if (e.getID() == KeyEvent.KEY_RELEASED) {
            if (e.getKeyCode() == 83) {
                userHuman.flagDown = false;
            }
            if (e.getKeyCode() == 87) {
                userHuman.flagUp = false;
            }
            if (e.getKeyCode() == 65) {
                userHuman.flagLeft = false;
            }
            if (e.getKeyCode() == 68) {
                userHuman.flagRight = false;
            }

        }
        return false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // 1024 - правая
        // 4096 - левая
        // 2048 - колёсико
        int ModifiersEx = e.getModifiersEx();
//        System.out.println(ModifiersEx);
        double x = e.getX();
        double y = e.getY();
        if (Constants.START_GAME) {
            if (ModifiersEx == 2048) {
                userHuman.aim.flagPrint = false;
            } else if (ModifiersEx == 1024) {
                BaseBullet bullet;
                BaseBot aimBot = userHuman.aim.bot;
                if (!userHuman.aim.flagPrint) {
                    bullet = new BaseBullet(userHuman.x, userHuman.y, e.getX(), e.getY(), userHuman);
                } else if (aimBot.type == 1 || aimBot.type == 2) {
                    bullet = new BaseBullet(userHuman.x, userHuman.y, userHuman.aim.bot.x, userHuman.aim.bot.y, userHuman);
                } else if (aimBot.variantOrientation == 2 || aimBot.variantOrientation == 3) {
                    if (aimBot.y > userHuman.y) {
                        double xb = aimBot.x - userHuman.x;
                        double yb = aimBot.y - userHuman.y;
                        double vp = Constants.V_BULLET;
                        double vb;
                        if (aimBot.variantOrientation == 2) {
                            vb = -Constants.V_BOTS;
                        } else {
                            vb = Constants.V_BOTS;
                        }
                        double l = vb / vp;
                        double cosDelta = 1 / (xb * xb + yb * yb) * (l * yb * yb + xb * Math.sqrt(xb * xb + (1 - l * l) * yb * yb));
                        double vpx = vp * cosDelta;
                        double vpy = Math.sqrt(vp * vp - vpx * vpx);
                        bullet = new BaseBullet(userHuman.x, userHuman.y, new Vector(vpx, vpy), userHuman);
                    } else {
                        double xb = aimBot.x - userHuman.x;
                        double yb = -(aimBot.y - userHuman.y);
                        double vp = Constants.V_BULLET;
                        double vb;
                        if (aimBot.variantOrientation == 2) {
                            vb = -Constants.V_BOTS;
                        } else {
                            vb = Constants.V_BOTS;
                        }
                        double l = vb / vp;
                        double cosDelta = 1 / (xb * xb + yb * yb) * (l * yb * yb + xb * Math.sqrt(xb * xb + (1 - l * l) * yb * yb));
                        double vpx = vp * cosDelta;
                        double vpy = Math.sqrt(vp * vp - vpx * vpx);
                        bullet = new BaseBullet(userHuman.x, userHuman.y, new Vector(vpx, -vpy), userHuman);
                    }
                } else if (aimBot.variantOrientation == 0 || aimBot.variantOrientation == 1) {
                    if (aimBot.x > userHuman.x) {
                        double yb = aimBot.y - userHuman.y;
                        double xb = aimBot.x - userHuman.x;
                        double vp = Constants.V_BULLET;
                        double vb;
                        if (aimBot.variantOrientation == 0) {
                            vb = -Constants.V_BOTS;
                        } else {
                            vb = Constants.V_BOTS;
                        }
                        double l = vb / vp;
                        double cosDelta = 1 / (yb * yb + xb * xb) * (l * xb * xb + yb * Math.sqrt(yb * yb + (1 - l * l) * xb * xb));
                        double vpy = vp * cosDelta;
                        double vpx = Math.sqrt(vp * vp - vpy * vpy);
                        bullet = new BaseBullet(userHuman.x, userHuman.y, new Vector(vpx, vpy), userHuman);
                    } else {
                        double yb = aimBot.y - userHuman.y;
                        double xb = -(aimBot.x - userHuman.x);
                        double vp = Constants.V_BULLET;
                        double vb;
                        if (aimBot.variantOrientation == 0) {
                            vb = -Constants.V_BOTS;
                        } else {
                            vb = Constants.V_BOTS;
                        }
                        double l = vb / vp;
                        double cosDelta = 1 / (yb * yb + xb * xb) * (l * xb * xb + yb * Math.sqrt(yb * yb + (1 - l * l) * xb * xb));
                        double vpy = vp * cosDelta;
                        double vpx = Math.sqrt(vp * vp - vpy * vpy);
                        bullet = new BaseBullet(userHuman.x, userHuman.y, new Vector(-vpx, vpy), userHuman);
                    }
                } else {
                    bullet = new BaseBullet(userHuman.x, userHuman.y, userHuman.aim.bot.x, userHuman.aim.bot.y, userHuman);
                }
                labirint.addBullet(bullet);
            } else {
                for (BaseBot bot : labirint.bots) {
                    if ((x - bot.x) * (x - bot.x) + (y - bot.y) * (y - bot.y) < Constants.SQRT_LEN_AIM) {
                        try {
                            userHuman.aim = new Aim(bot);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        } else {
//            System.out.println(buttom.isPush(getMousePosition()));
            if (!Constants.IN_SETTING) {
                if (ModifiersEx == 1024 && buttoms.get(0).isPush(getMousePosition())) {
                    Constants.START_GAME = true;
                }
                if (ModifiersEx == 1024 && buttoms.get(2).isPush(getMousePosition())) {
                    Constants.IN_SETTING = true;
                    buttoms = new ArrayList<>();
                    if (Constants.MUST_PLAY_MUSIC){
                        try {
                            buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2, getHeight() / 2, "data\\ButtomMusicOn.png"));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }else{
                        try {
                            buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2, getHeight() / 2, "data\\ButtomMusicOff.png"));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    if (Constants.MUST_PLAY_SOUND){
                        try {
                            buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2, getHeight() / 2 + getWidth() / 28.0 * 5 / 4, "data\\ButtomSoundOn.png"));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }else{
                        try {
                            buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2, getHeight() / 2 + getWidth() / 28.0 * 5 / 4, "data\\ButtomSoundOff.png"));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                    try {
                        buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2, getHeight() / 2 + getWidth() / 28.0 * 2.0 * 5 / 4, "data\\ButtomBack.png"));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            } else {
                if (ModifiersEx == 1024 && buttoms.get(2).isPush(getMousePosition())) {
                    Constants.IN_SETTING = false;
                    buttoms = new ArrayList<>();
                    try {
                        buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2, getHeight() / 2, "data\\ButtomStart.png"));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2, getHeight() / 2 + getWidth() / 28.0 * 5 / 4, "data\\ButtomRecords.png"));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2, getHeight() / 2 + getWidth() / 28.0 * 2.0 * 5 / 4, "data\\ButtomSetting.png"));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2, getHeight() / 2 + getWidth() / 28.0 * 3.0 * 5 / 4, "data\\ButtomInfo.png"));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                } else if (ModifiersEx == 1024 && buttoms.get(0).isPush(getMousePosition())){
                    Constants.MUST_PLAY_MUSIC = !Constants.MUST_PLAY_MUSIC;
                    try {
                        Constants.writeConstants();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (Constants.MUST_PLAY_MUSIC){
                        try {
                            buttoms.get(0).setImage("data\\ButtomMusicOn.png");
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        try {
                            buttoms.get(0).setImage("data\\ButtomMusicOff.png");
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                } else if (ModifiersEx == 1024 && buttoms.get(1).isPush(getMousePosition())){
                    Constants.MUST_PLAY_SOUND = !Constants.MUST_PLAY_SOUND;
                    try {
                        Constants.writeConstants();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (Constants.MUST_PLAY_SOUND){
                        try {
                            buttoms.get(1).setImage("data\\ButtomSoundOn.png");
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        try {
                            buttoms.get(1).setImage("data\\ButtomSoundOff.png");
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        }

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