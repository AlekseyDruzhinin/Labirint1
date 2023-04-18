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
import java.io.*;
import java.util.ArrayList;


public class MyFrame extends JFrame implements KeyEventDispatcher, MouseListener, MouseMotionListener {
    Scoreboard scoreboard;
    Labirint labirint;
    UserHuman userHuman;
    long timePriviosPrint;
    BufferedImage imageBloodBackground;

    BufferedImage image;
    BufferedImage imageClock;
    BufferedImage imageBoom;
    BufferedImage imageCheckMark;
    BufferedImage imageCntBots;
    BufferedImage imageCntWay;

    BufferedImage imageBackGround;
    BufferedImage imageBackGround1;
    BufferedImage imageEnterBackGround;
    ArrayList<Buttom> buttoms = new ArrayList<>();

    AffineTransform tx;
    AffineTransformOp op;

    Buttom pauseButtom;
    Buttom startButtom;
    Buttom settingButtom;
    Buttom exitButtom;

    public MyFrame() throws IOException {
        Constants.TIME_START_PROGRAM = System.currentTimeMillis();
        this.imageBloodBackground = ImageIO.read(new File("data\\blood_background.png"));
        imageBackGround = ImageIO.read(new File("data\\Sky.jpg"));
        imageBackGround1 = ImageIO.read(new File("data\\Sand1.jpg"));
        imageEnterBackGround = ImageIO.read(new File("data\\EnterBackGround1.jpg"));
        imageCheckMark = ImageIO.read(new File("data\\CheckMark.png"));

        addMouseListener(this);
        addMouseMotionListener(this);

        image = ImageIO.read(new File("data\\GameOver.png"));
        imageClock = ImageIO.read(new File("data\\clock1.png"));
        imageBoom = ImageIO.read(new File("data\\boom.gif"));
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

        buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2, "data\\ButtomStart.png"));
        buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2 + getWidth() / 28.0 * 5 / 4, "data\\ButtomRecords.png"));
        buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2 + getWidth() / 28.0 * 2.0 * 5 / 4, "data\\ButtomSetting.png"));
        buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2 + getWidth() / 28.0 * 3.0 * 5 / 4, "data\\ButtomInfo.png"));

        {
            FileReader reader = new FileReader("files\\Constants.txt");
            Constants.MUST_PLAY_MUSIC = (reader.read() == '1');
            Constants.MUST_PLAY_SOUND = (reader.read() == '1');
        }
        Constants.writeConstants();

        Constants.TIME_LAST_BUM = 0;

        pauseButtom = new Buttom(getWidth() / 40, getWidth() / 2 + (int) (2.25 * (double) Constants.R), Constants.SDVIG / 2, "data\\pause.png");
        startButtom = new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2, "data\\ButtomContinue.png");
        settingButtom = new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2 + getWidth() / 28.0 * 5 / 4, "data\\ButtomSetting.png");
        exitButtom = new Buttom(getWidth() / 16, getWidth() / 2 + getWidth() / 32, 6 * getHeight() / 8, "data\\ButtumExit.png", false);
    }

    @Override
    public void paint(Graphics g) {
//        System.out.println(buttoms.size());
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
                    Sound soundMenu = new Sound(SoundFiles.Giornos_Theme);
                    soundMenu.setVolume((float) 0.75);
//                    soundMenu.play();
                    while (!Constants.START_GAME) {
                        if (Constants.MUST_PLAY_MUSIC) {
                            if (!soundMenu.isPlaying()) {
//                                System.out.println("..");
                                soundMenu.play();
                            }
                        } else {
                            soundMenu.stop();
                        }
                    }
                    Constants.MUSIC_GAME = 0;
                    Constants.TIME_START_PROGRAM = System.currentTimeMillis();
                    soundMenu.stop();
                }).start();
            }
            g.drawImage(imageEnterBackGround, 0, 0, this.getWidth(), this.getHeight(), null);
            if (!Constants.IN_RECORDS || (Constants.IN_RECORDS && Constants.TYPE_OF_RECORDS == 0)) {
                for (Buttom buttom : buttoms) {
                    if (buttom != null) {
                        buttom.paint(g, getMousePosition());
                    }
                }
            } else {
                if (buttoms != null && buttoms.get(buttoms.size() - 1) != null) {
                    buttoms.get(buttoms.size() - 1).paint(g, getMousePosition());
                }
                if (scoreboard != null) {
                    scoreboard.paint(g);
                }
            }

        } else {
            g.drawImage(imageBackGround, 0, 0, this.getWidth(), this.getHeight(), null);
            if (System.currentTimeMillis() - Constants.TIME_HIT <= Constants.TIME_LIFE_BACKGROUND_BLOOD) {
                g.drawImage(imageBloodBackground, 0, 0, this.getWidth(), this.getHeight(), null);
            }
            if (Constants.USER_DIED) {
                if (Constants.GAME_OVER == 0) {
                    if (Constants.MUST_PLAY_SOUND) {
                        new Thread(() -> {
//                        Sound.playSound("data\\music\\Game_over.wav");
                            Sound sound = new Sound(SoundFiles.Game_over);
                            sound.play();
                        }).start();
                    }
                    Constants.GAME_OVER++;
                }
                g.drawImage(image, this.getWidth() / 2 - this.getHeight() / 2, 0, this.getHeight(), this.getHeight(), null);
                g.drawImage(imageBloodBackground, 0, 0, this.getWidth(), this.getHeight(), null);
                if (System.currentTimeMillis() - Constants.TIME_USER_DIED >= 3000) {
                    exitButtom.paint(g, getMousePosition());
                    exitButtom.isIt = true;
                }
            } else {
                if (!Constants.PAUSE_MENU) {
                    if (Constants.MUSIC_GAME == 0) {
                        Constants.MUSIC_GAME = 1;
                        new Thread(() -> {
                            Sound soundGame = new Sound(SoundFiles.Music);
                            soundGame.setVolume((float) 0.65);
                            Sound soundDied = new Sound(SoundFiles.End);
                            soundDied.setVolume((float) 0.65);
                            while (Constants.START_GAME) {
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
                                if (!Constants.MUST_PLAY_MUSIC) {
                                    soundGame.stop();
                                    soundDied.stop();
                                }
                            }
                            soundGame.stop();
                            soundDied.stop();
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
                        labirint.paint(g, userHuman);
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
                    g.drawImage(imageCntBots, this.getWidth() - 150 - Constants.R * 6 - Constants.R * 5 / 4, Constants.SDVIG / 15 * 7 - Constants.R / 8, Constants.R * 5 / 4, Constants.R * 5 / 4, null);
                    g.drawImage(imageCntWay, this.getWidth() - 150 - Constants.R * 12 - Constants.R * 5 / 4, Constants.SDVIG / 15 * 7 - Constants.R / 8, Constants.R * 5 / 4, Constants.R * 5 / 4, null);

                    g.setFont(new Font("TimesRoman", Font.BOLD + Font.ITALIC, 40));
                    g.setColor(Color.BLACK);
                    g.drawString(Time.timeToString(), this.getWidth() - 146, Constants.SDVIG / 8 * 5 + Constants.R / 2 + 4);
                    g.drawString(Constants.CNT_DIED_BOTS.getString(), this.getWidth() - 146 - Constants.R * 6, Constants.SDVIG / 8 * 5 + Constants.R / 2 + 4);
                    g.drawString(Constants.CNT_WAY.getString(), this.getWidth() - 146 - Constants.R * 12, Constants.SDVIG / 8 * 5 + Constants.R / 2 + 4);

                    g.setColor(Color.WHITE);
                    g.drawString(Time.timeToString(), this.getWidth() - 150, Constants.SDVIG / 8 * 5 + Constants.R / 2);
                    g.drawString(Constants.CNT_DIED_BOTS.getString(), this.getWidth() - 150 - Constants.R * 6, Constants.SDVIG / 8 * 5 + Constants.R / 2);
                    g.drawString(Constants.CNT_WAY.getString(), this.getWidth() - 150 - Constants.R * 12, Constants.SDVIG / 8 * 5 + Constants.R / 2);

                    g.drawImage(imageBoom, getWidth() / 2, Constants.SDVIG / 4, (int) (1.75 * Constants.R), (int) (1.75 * Constants.R), null);
                    if (System.currentTimeMillis() - Constants.TIME_LAST_BUM <= Constants.TIME_UPGRADE_BOOM) {
                        Integer time = (int) (Constants.TIME_UPGRADE_BOOM - System.currentTimeMillis() + Constants.TIME_LAST_BUM) / 1000;
                        g.setFont(new Font("TimesRoman", Font.BOLD + Font.ITALIC, 25));
                        g.setColor(Color.WHITE);
//                        System.out.println(time);
                        if (time >= 10) {
                            g.drawString(time.toString(), getWidth() / 2 + (int) (1.75 * Constants.R / 8), (int) (Constants.SDVIG * 0.85));
                        } else {
                            g.drawString(time.toString(), getWidth() / 2 + (int) (0.45 * Constants.R), (int) (Constants.SDVIG * 0.85));
                        }
                    } else {
                        if (!Constants.BOOM_IS_READY) {
                            if (Constants.MUST_PLAY_SOUND) {
                                new Thread(() -> {
                                    Sound sound = new Sound(SoundFiles.yesboom);
                                    sound.setVolume((float) 0.75);
                                    sound.play();
                                    sound.join();
                                }).start();
                            }
                            Constants.BOOM_IS_READY = true;
                        }
                        g.drawImage(imageCheckMark, getWidth() / 2 + (int) (0.4 * Constants.R), (int) (Constants.SDVIG * 0.6), (int) (0.6 * Constants.R), (int) (0.6 * Constants.R), null);
                    }
                    pauseButtom.paint(g, getMousePosition());

                    labirint.paintBushes(g, userHuman);
                } else {
                    g.drawImage(imageEnterBackGround, 0, 0, getWidth(), getHeight(), null);
                    if (!Constants.IN_SETTING) {
                        startButtom.paint(g, getMousePosition());
                        settingButtom.paint(g, getMousePosition());
                    } else {
                        for (Buttom buttom : buttoms) {
                            buttom.paint(g, getMousePosition());
                        }
                    }
                }
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
        // q - 81
        // ' ' - 32
//        System.out.println(e.getKeyCode());
        if (e.getID() == KeyEvent.KEY_PRESSED) {
//            System.out.println(e.getKeyCode());
            if (e.getKeyCode() == 32 && !labirint.boom.flag && Constants.START_GAME) {
                if (Constants.BOOM_IS_READY) {
                    try {
                        labirint.addBoom(userHuman);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    if (Constants.MUST_PLAY_SOUND) {
                        new Thread(() -> {
                            Sound sound = new Sound(SoundFiles.mimo);
                            sound.setVolume((float) 1.00);
                            sound.play();
                            sound.join();
                        }).start();
                    }
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
            if (Constants.USER_DIED) {
                if (ModifiersEx == 1024 && exitButtom.isIt && exitButtom.isPush(getMousePosition())) {
                    Constants.RESTART_GAME = true;
                }
            } else {
                if (Constants.PAUSE_MENU) {
                    if (!Constants.IN_SETTING) {
                        if (ModifiersEx == 1024 && startButtom.isPush(getMousePosition())) {
                            Constants.PAUSE_MENU = false;
                            long deltaTime = System.currentTimeMillis() - Constants.TIME_STOP;
                            Constants.TIME_START_PROGRAM += deltaTime;
                            Constants.TIME_LAST_BUM += deltaTime;
                            for (BaseBot diedBot : labirint.diedBots) {
                                diedBot.timeDied += deltaTime;
                            }
                        }
                        if (ModifiersEx == 1024 && settingButtom.isPush(getMousePosition())) {
                            Constants.IN_SETTING = true;
                            buttoms = new ArrayList<>();
                            if (Constants.MUST_PLAY_MUSIC) {
                                try {
                                    buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2, "data\\ButtomMusicOn.png"));
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            } else {
                                try {
                                    buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2, "data\\ButtomMusicOff.png"));
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                            if (Constants.MUST_PLAY_SOUND) {
                                try {
                                    buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2 + getWidth() / 28.0 * 5 / 4, "data\\ButtomSoundOn.png"));
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            } else {
                                try {
                                    buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2 + getWidth() / 28.0 * 5 / 4, "data\\ButtomSoundOff.png"));
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }

                            try {
                                buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2 + getWidth() / 28.0 * 2.0 * 5 / 4, "data\\ButtomBack.png"));
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    } else {
                        if (ModifiersEx == 1024 && buttoms.get(2).isPush(getMousePosition())) {
                            Constants.IN_SETTING = false;
                            buttoms = new ArrayList<>();
                            try {
                                buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2, "data\\ButtomStart.png"));
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            try {
                                buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2 + getWidth() / 28.0 * 5 / 4, "data\\ButtomRecords.png"));
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            try {
                                buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2 + getWidth() / 28.0 * 2.0 * 5 / 4, "data\\ButtomSetting.png"));
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            try {
                                buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2 + getWidth() / 28.0 * 3.0 * 5 / 4, "data\\ButtomInfo.png"));
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }

                        } else if (ModifiersEx == 1024 && buttoms.get(0).isPush(getMousePosition())) {
                            Constants.MUST_PLAY_MUSIC = !Constants.MUST_PLAY_MUSIC;
                            try {
                                Constants.writeConstants();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            if (Constants.MUST_PLAY_MUSIC) {
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
                        } else if (ModifiersEx == 1024 && buttoms.get(1).isPush(getMousePosition())) {
                            Constants.MUST_PLAY_SOUND = !Constants.MUST_PLAY_SOUND;
                            try {
                                Constants.writeConstants();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            if (Constants.MUST_PLAY_SOUND) {
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
                } else {
                    if (ModifiersEx == 1024 && pauseButtom.isPush(getMousePosition())) {
                        Constants.PAUSE_MENU = true;
                        Constants.TIME_STOP = System.currentTimeMillis();
                    }
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
                }
            }
        } else {
//            System.out.println(buttom.isPush(getMousePosition()));
            if (!Constants.IN_SETTING && !Constants.IN_RECORDS) {
                if (ModifiersEx == 1024 && buttoms.get(0).isPush(getMousePosition())) {
                    Constants.START_GAME = true;
                }
                if (ModifiersEx == 1024 && buttoms.get(2).isPush(getMousePosition())) {
                    Constants.IN_SETTING = true;
                    buttoms = new ArrayList<>();
                    if (Constants.MUST_PLAY_MUSIC) {
                        try {
                            buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2, "data\\ButtomMusicOn.png"));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        try {
                            buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2, "data\\ButtomMusicOff.png"));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    if (Constants.MUST_PLAY_SOUND) {
                        try {
                            buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2 + getWidth() / 28.0 * 5 / 4, "data\\ButtomSoundOn.png"));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        try {
                            buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2 + getWidth() / 28.0 * 5 / 4, "data\\ButtomSoundOff.png"));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                    try {
                        buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2 + getWidth() / 28.0 * 2.0 * 5 / 4, "data\\ButtomBack.png"));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                if (ModifiersEx == 1024 && buttoms.get(1).isPush(getMousePosition())) {
                    Constants.IN_RECORDS = true;
                    buttoms = new ArrayList<>();
                    try {
                        buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2, "data\\ButtomRecord`sTypeWay.png"));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2 + getWidth() / 28.0 * 1.0 * 5 / 4, "data\\ButtomRecord`sTypeBots.png"));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2 + getWidth() / 28.0 * 2.0 * 5 / 4, "data\\ButtomRecord`sTypeTime.png"));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2 + getWidth() / 28.0 * 3.0 * 5 / 4, "data\\ButtomBack.png"));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            } else if (Constants.IN_SETTING) {
                if (ModifiersEx == 1024 && buttoms.get(2).isPush(getMousePosition())) {
                    Constants.IN_SETTING = false;
                    buttoms = new ArrayList<>();
                    try {
                        buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2, "data\\ButtomStart.png"));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2 + getWidth() / 28.0 * 5 / 4, "data\\ButtomRecords.png"));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2 + getWidth() / 28.0 * 2.0 * 5 / 4, "data\\ButtomSetting.png"));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2 + getWidth() / 28.0 * 3.0 * 5 / 4, "data\\ButtomInfo.png"));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                } else if (ModifiersEx == 1024 && buttoms.get(0).isPush(getMousePosition())) {
                    Constants.MUST_PLAY_MUSIC = !Constants.MUST_PLAY_MUSIC;
                    try {
                        Constants.writeConstants();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (Constants.MUST_PLAY_MUSIC) {
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
                } else if (ModifiersEx == 1024 && buttoms.get(1).isPush(getMousePosition())) {
                    Constants.MUST_PLAY_SOUND = !Constants.MUST_PLAY_SOUND;
                    try {
                        Constants.writeConstants();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (Constants.MUST_PLAY_SOUND) {
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
            } else if (Constants.IN_RECORDS) {
                if (Constants.TYPE_OF_RECORDS == 0) {
                    if (ModifiersEx == 1024 && buttoms.get(3).isPush(getMousePosition())) {
                        Constants.IN_RECORDS = false;
                        buttoms = new ArrayList<>();
                        try {
                            buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2, "data\\ButtomStart.png"));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        try {
                            buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2 + getWidth() / 28.0 * 5 / 4, "data\\ButtomRecords.png"));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        try {
                            buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2 + getWidth() / 28.0 * 2.0 * 5 / 4, "data\\ButtomSetting.png"));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        try {
                            buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2 + getWidth() / 28.0 * 3.0 * 5 / 4, "data\\ButtomInfo.png"));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    if (ModifiersEx == 1024 && buttoms.get(0).isPush(getMousePosition())) {
                        Constants.TYPE_OF_RECORDS = 1;
                        try {
                            scoreboard = new Scoreboard(getWidth() / 28.0, getWidth(), getHeight(), "files\\ways_records.txt");
                            buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2 + getWidth() / 28.0 * 4.0 * 5 / 4, "data\\ButtomBack.png"));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    if (ModifiersEx == 1024 && buttoms.get(1).isPush(getMousePosition())) {
                        Constants.TYPE_OF_RECORDS = 2;
                        try {
                            scoreboard = new Scoreboard(getWidth() / 28.0, getWidth(), getHeight(), "files\\bots_records.txt");
                            buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2 + getWidth() / 28.0 * 4.0 * 5 / 4, "data\\ButtomBack.png"));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    if (ModifiersEx == 1024 && buttoms.get(2).isPush(getMousePosition())) {
                        Constants.TYPE_OF_RECORDS = 3;
                        try {
                            scoreboard = new Scoreboard(getWidth() / 28.0, getWidth(), getHeight(), "files\\times_records.txt");
                            buttoms.add(new Buttom(getWidth() / 28.0, getWidth() / 2 + Constants.R / 2, getHeight() / 2 + getWidth() / 28.0 * 4.0 * 5 / 4, "data\\ButtomBack.png"));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                } else if (Constants.TYPE_OF_RECORDS != 0) {
                    if (buttoms != null && buttoms.get(buttoms.size() - 1).isPush(getMousePosition())) {
                        Constants.TYPE_OF_RECORDS = 0;
                        scoreboard = null;
                        buttoms.remove(buttoms.get(buttoms.size() - 1));
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

    }
}