import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Labirint {

    Boom boom = new Boom();

    BufferedImage imagePesok;
    BufferedImage imageDiedPesok;
    BufferedImage imageParallelWall;
    BufferedImage imageVerticalWall;

    //массив ботов
    ArrayList<BaseSector> sectors = new ArrayList<>();
    ArrayList<BaseBot> bots = new ArrayList<>();
    ArrayList<BaseBot> diedBots = new ArrayList<>();

    ArrayList<BaseBullet> diedBullets = new ArrayList<>();
    ArrayList<BaseBullet> bullets = new ArrayList<>();
    ArrayList<BotsBaseBullet> diedBotsBullets = new ArrayList<>();
    ArrayList<BotsBaseBullet> botsBullets = new ArrayList<>();

    MyFrame panel;
    Random random = new Random();
    int indexDied = 0;

    public Labirint(MyFrame panel) throws IOException {
        imagePesok = ImageIO.read(new File("data\\Pesok.jpg"));
        imageDiedPesok = ImageIO.read(new File("data\\diedGround.png"));
        imageParallelWall = ImageIO.read(new File("data\\ParallelWall2.png"));
        imageVerticalWall = ImageIO.read(new File("data\\VerticalWall2.png"));

        this.panel = panel;
        sectors.add(new StandardSector(Constants.SDVIG, Constants.SDVIG, panel));
        for (int iter = 0; iter < 10; ++iter) {
            int j1 = random.nextInt(sectors.get(0).cells.size());
            int k1 = random.nextInt(sectors.get(0).cells.get(0).size());
            int type = random.nextInt() % 4 + 1;
            if (type == 1) {
                RedBot redBot = new RedBot(getCell(sectors.size() - 1, j1, k1).x, getCell(sectors.size() - 1, j1, k1).y, j1, k1, sectors.size() - 1, this);
                bots.add(redBot);
            } else if (type == 2) {
                OrangeBot orangeBot = new OrangeBot(getCell(sectors.size() - 1, j1, k1).x, getCell(sectors.size() - 1, j1, k1).y, j1, k1, sectors.size() - 1, this);
                bots.add(orangeBot);
            } else if (type == 3) {
                GreenBot greenBot = new GreenBot(getCell(sectors.size() - 1, j1, k1).x, getCell(sectors.size() - 1, j1, k1).y, j1, k1, sectors.size() - 1, this);
                bots.add(greenBot);
            } else {
                PinkBot pinkBot = new PinkBot(getCell(sectors.size() - 1, j1, k1).x, getCell(sectors.size() - 1, j1, k1).y, j1, k1, sectors.size() - 1, this);
                bots.add(pinkBot);
            }
        }
        addSector();
    }

    public BaseCell getCell(int i, int j, int k) {
        if (i >= sectors.size() || j >= sectors.get(i).cells.size() || k >= sectors.get(i).cells.get(j).size()) {
            return null;
        }
        return sectors.get(i).cells.get(j).get(k);
    }


    public void paint(Graphics g, Human userHuman) {
//        System.out.println(bots.size());
//        System.out.println(diedBots.size());
//        System.out.println(diedBullets.size());
//        System.out.println(bullets.size());
//        System.out.println(diedBotsBullets.size());
//        System.out.println(botsBullets.size());
//        System.out.println(sectors.size());

        sectors.get(userHuman.indexSector).paint(g, imagePesok, imageDiedPesok, imageParallelWall, imageVerticalWall);
        if (userHuman.indexSector > 0){
            sectors.get(userHuman.indexSector-1).paint(g, imagePesok, imageDiedPesok, imageParallelWall, imageVerticalWall);
        }
        if (userHuman.indexSector < sectors.size()-1){
            sectors.get(userHuman.indexSector+1).paint(g, imagePesok, imageDiedPesok, imageParallelWall, imageVerticalWall);
        }

        for (BaseBot bot : bots) {
            if (bot.indexSector >= userHuman.indexSector-1 && bot.indexSector <= userHuman.indexSector + 1){
                if (Constants.DEVELORER) {
                    g.setColor(new Color(239, 124, 124, 255));
                    g.fillRect((int) getCell(bot.indexSector, bot.i, bot.j).x, (int) getCell(bot.indexSector, bot.i, bot.j).y, Constants.R, Constants.R);
                    g.setColor(Color.RED);
                    if (bot.type == 3) {
                        g.setColor(Color.BLACK);
                        g.drawString(Integer.toString(bot.variantOrientation), (int) getCell(bot.indexSector, bot.i, bot.j).x + Constants.R / 2, (int) getCell(bot.indexSector, bot.i, bot.j).y + Constants.R / 2);
                        g.setColor(Color.RED);
                    }
                }
                bot.paint(g);
            }
        }
        for (BaseBullet bullet : bullets) {
            bullet.print(g, this);
        }
//        for (BaseBullet bullet : diedBullets) {
//            bullet.print(g, this);
//        }
        for (BotsBaseBullet bullet : botsBullets) {
            bullet.print(g, this);
        }
//        for (BotsBaseBullet bullet : diedBotsBullets) {
//            bullet.print(g, this);
//        }
        if (boom != null && boom.flag) {
            boom.paint(g, this, panel);
        }
        for (BaseBot diedBot : diedBots){
            if (diedBot.indexSector >= userHuman.indexSector-1 && diedBot.indexSector <= userHuman.indexSector + 1){
                diedBot.paintAfterDied(g, panel);
            }
        }
        boom.paintDied(g, this);
    }

    public BaseSector getSector(int i) {
        if (i >= sectors.size()) {
            int z = 1;
        }
        return sectors.get(i);
    }

    public void go(double v) {
        for (BaseSector sector : sectors) {
            sector.go(v);
        }
        for (BaseBot bot : bots) {
            bot.x += v;
        }
        for (BaseBot diedBot : diedBots) {
            diedBot.x += v;
        }
        for (BaseBullet bullet : bullets) {
            bullet.segment.setX1(bullet.segment.getX1() + v);
            bullet.segment.setX2(bullet.segment.getX2() + v);
        }
        for (BaseBullet bullet : diedBullets) {
            bullet.segment.setX1(bullet.segment.getX1() + v);
            bullet.segment.setX2(bullet.segment.getX2() + v);
        }
        for (BotsBaseBullet bullet : botsBullets) {
            bullet.segment.setX1(bullet.segment.getX1() + v);
            bullet.segment.setX2(bullet.segment.getX2() + v);
        }
        for (BotsBaseBullet bullet : diedBotsBullets) {
            bullet.segment.setX1(bullet.segment.getX1() + v);
            bullet.segment.setX2(bullet.segment.getX2() + v);
        }
    }

    public void addSector() throws IOException {
        sectors.add(new BaseSector(sectors.get(sectors.size() - 1).x + sectors.get(sectors.size() - 1).widthCnt * Constants.R * 2, Constants.SDVIG, panel) {
        });
        for (int i = 1; i < sectors.get(0).verticalWalls.get(0).size(); ++i) {
            if (random.nextInt(3) == 0) {
                sectors.get(sectors.size() - 1).verticalWalls.get(0).get(i).flag = false;
                sectors.get(sectors.size() - 2).verticalWalls.get(sectors.get(sectors.size() - 2).verticalWalls.size() - 1).get(i).flag = false;
            }
        }
        for (int iter = 0; iter < 10; ++iter) {
            int j1 = random.nextInt(sectors.get(0).cells.size());
            int k1 = random.nextInt(sectors.get(0).cells.get(0).size());
            int type = random.nextInt() % 4 + 1;
            if (type == 1) {
                RedBot redBot = new RedBot(getCell(sectors.size() - 1, j1, k1).x, getCell(sectors.size() - 1, j1, k1).y, j1, k1, sectors.size() - 1, this);
                bots.add(redBot);
            } else if (type == 2) {
                OrangeBot orangeBot = new OrangeBot(getCell(sectors.size() - 1, j1, k1).x, getCell(sectors.size() - 1, j1, k1).y, j1, k1, sectors.size() - 1, this);
                bots.add(orangeBot);
            } else if (type == 3) {
                GreenBot greenBot = new GreenBot(getCell(sectors.size() - 1, j1, k1).x, getCell(sectors.size() - 1, j1, k1).y, j1, k1, sectors.size() - 1, this);
                bots.add(greenBot);
            } else {
                PinkBot pinkBot = new PinkBot(getCell(sectors.size() - 1, j1, k1).x, getCell(sectors.size() - 1, j1, k1).y, j1, k1, sectors.size() - 1, this);
                bots.add(pinkBot);
            }
        }
    }

    public void update(long time, Human userHuman, Graphics g) throws IOException {
        boom.buuxx(this, g, userHuman);
        boolean itIsDied = sectors.get(indexDied).update((double) time * Constants.V_POLE);

        for (BaseBot bot : bots) {
            if (bot.indexSector >= userHuman.indexSector-1 && bot.indexSector <= userHuman.indexSector+1){
                bot.update(this, userHuman);
            }
        }

        if (itIsDied) {
            if (indexDied == 0) {
                indexDied = 1;
            } else {
                sectors.remove(0);
                userHuman.indexSector--;
                userHuman.maxIndex--;
                for (BaseBot bot : bots) {
                    bot.indexSector--;
                }
            }
        }

        for (BaseBot bot : bots) {
            if (bot.hp <= 0.0) {
                if (userHuman.aim.bot == bot) {
                    userHuman.aim.flagPrint = false;
                }
                bot.timeDied = System.currentTimeMillis();
                diedBots.add(bot);
            }
        }
        for (BaseBot bot : diedBots) {
            bots.remove(bot);
        }

        ArrayList<BaseBot> diedDiedBots = new ArrayList<>();
        for (BaseBot diedBot : diedBots){
            if (System.currentTimeMillis() - diedBot.timeDied >= Constants.TIME_LIVE_BOT_AFTER_DIED){
                diedDiedBots.add(diedBot);
            }
        }
        for (BaseBot diedBot : diedDiedBots){
            diedBots.remove(diedBot);
        }

        ArrayList<BaseBullet> diedDiedBullets = new ArrayList<>();
        for (BaseBullet bullet : diedBullets) {
            if (System.currentTimeMillis() - bullet.timeDied > Constants.TIME_LIFE_AFTER_DIED) {
                diedDiedBullets.add(bullet);
            }
        }
        for (BaseBullet bullet : diedDiedBullets) {
            diedBullets.remove(bullet);
        }
        ArrayList<BotsBaseBullet> diedDiedBotsBullets = new ArrayList<>();
        for (BotsBaseBullet bullet : diedBotsBullets) {
            if (System.currentTimeMillis() - bullet.timeDied > Constants.TIME_LIFE_AFTER_DIED) {
                diedDiedBotsBullets.add(bullet);
            }
        }
        for (BotsBaseBullet bullet : diedDiedBotsBullets) {
            diedBotsBullets.remove(bullet);
        }

        for (BaseBot bot : bots) {
            if (bot.visu && System.currentTimeMillis() - bot.timeLastShot > bot.timeDelay) {
                BotsBaseBullet bullet = new BotsBaseBullet(bot.x, bot.y, userHuman.x, userHuman.y, bot);
                bot.timeLastShot = System.currentTimeMillis();
                this.addBotsBullet(bullet);
            }
        }

        for (BaseBot bot : bots) {
            if (bot.indexSector >= userHuman.indexSector-1 && bot.indexSector <= userHuman.indexSector+1){
                bot.go(userHuman, time, this);
            }
        }

        BaseSector sector = sectors.get(userHuman.indexSector);
        if (sector.yesKid && sector.iKid == userHuman.i && sector.jKid == userHuman.j){
            sector.yesKid = false;
            userHuman.hill();
            if (Constants.MUST_PLAY_SOUND){
                new Thread(() -> {
                    Sound sound = new Sound(SoundFiles.upgrade);
                    sound.setVolume((float) 1.0);
                    sound.play();
                    sound.join();
                }).start();
            }
        }
    }

    public void addBullet(BaseBullet bullet) {
        bullets.add(bullet);
    }

    public void addBotsBullet(BotsBaseBullet bullet) {
        botsBullets.add(bullet);
    }

    public void goBullets(long time, Graphics g, Human userHuman) {
        ArrayList<BaseBullet> bulletDiedInThisStep = new ArrayList<>();
        for (BaseBullet bullet : bullets) {
            if (bullet.go(this, time, g, userHuman)) {
                bulletDiedInThisStep.add(bullet);
            }
        }
        for (BaseBullet bullet : bulletDiedInThisStep) {
            bullets.remove(bullet);
            diedBullets.add(bullet);
        }
        ArrayList<BotsBaseBullet> botsBulletDiedInThisStep = new ArrayList<>();
        for (BotsBaseBullet bullet : botsBullets) {
            if (bullet.go(this, time, g, userHuman, panel)) {
                botsBulletDiedInThisStep.add(bullet);
            }
        }
        for (BotsBaseBullet bullet : botsBulletDiedInThisStep) {
            botsBullets.remove(bullet);
            diedBotsBullets.add(bullet);
        }
    }

    public void addBoom(Human userHuman) throws IOException {
        boom = new Boom(userHuman);
    }


}
