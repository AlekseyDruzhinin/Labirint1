import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Labirint {
    ArrayList<BaseSector> sectors = new ArrayList<>();

    //массив ботов
    ArrayList<BaseBot> bots = new ArrayList<>();

    ArrayList<BaseBullet> diedBullets = new ArrayList<>();
    ArrayList<BaseBullet> bullets = new ArrayList<>();

    MyFrame panel;
    Random random = new Random();
    int indexDied = 0;

    public Labirint(MyFrame panel) throws IOException {
        this.panel = panel;
        sectors.add(new StandardSector(Constants.SDVIG, Constants.SDVIG, panel));
        RedBot redBot = new RedBot(getCell(0, 5, 5).x, getCell(0, 5, 5).y, 5, 5, 0);
        bots.add(redBot);
        addSector();
    }

    public BaseCell getCell(int i, int j, int k) {
        return sectors.get(i).cells.get(j).get(k);
    }

    public void paint(Graphics g) {
        for (BaseSector sector : sectors) {
            sector.paint(g);
        }
        for (BaseBot bot : bots) {
            if (Constants.DEVELORER) {
                g.setColor(new Color(239, 124, 124, 255));
                g.fillRect((int) getCell(bot.indexSector, bot.i, bot.j).x, (int) getCell(bot.indexSector, bot.i, bot.j).y, Constants.R, Constants.R);
                g.setColor(Color.RED);
            }
            bot.paint(g);
        }
        for (BaseBullet bullet : bullets){
            bullet.print(g, this);
        }
        for (BaseBullet bullet : diedBullets){
            bullet.print(g, this);
        }
    }

    public BaseSector getSector(int i) {
        return sectors.get(i);
    }

    public void go(double v) {
        for (BaseSector sector : sectors) {
            sector.go(v);
        }
        for (BaseBot bot : bots) {
            bot.x += v;
        }
        for (BaseBullet bullet : bullets){
            bullet.segment.setX1(bullet.segment.getX1() + v);
            bullet.segment.setX2(bullet.segment.getX2() + v);
        }
        for (BaseBullet bullet : diedBullets){
            bullet.segment.setX1(bullet.segment.getX1() + v);
            bullet.segment.setX2(bullet.segment.getX2() + v);
        }
    }

    public void addSector() {
        sectors.add(new StandardSector(sectors.get(sectors.size() - 1).x + sectors.get(sectors.size() - 1).widthCnt * Constants.R * 2, Constants.SDVIG, panel));
        for (int i = 1; i < sectors.get(0).verticalWalls.get(0).size(); ++i) {
            if (random.nextInt(3) == 0) {
                sectors.get(sectors.size() - 1).verticalWalls.get(0).get(i).flag = false;
                sectors.get(sectors.size() - 2).verticalWalls.get(sectors.get(sectors.size() - 2).verticalWalls.size() - 1).get(i).flag = false;
            }
        }
    }

    public void update(long time, Human userHuman) {
        boolean itIsDied = sectors.get(indexDied).update((double) time * Constants.V_POLE);
        if (itIsDied) {
            if (indexDied == 0) {
                indexDied = 1;
            } else {
                sectors.remove(0);
                userHuman.indexSector--;
                for (BaseBot bot : bots){
                    bot.indexSector--;
                }
            }
        }

        ArrayList<BaseBot> diedBots = new ArrayList<>();
        for (BaseBot bot : bots){
            if (bot.hp <= 0.0){
                diedBots.add(bot);
            }
        }
        for (BaseBot bot : diedBots){
            bots.remove(bot);
        }

        ArrayList<BaseBullet> diedDiedBullets = new ArrayList<>();
        for (BaseBullet bullet : diedBullets){
            if (System.currentTimeMillis() - bullet.timeDied > Constants.TIME_LIFE_AFTER_DIED){
                diedDiedBullets.add(bullet);
            }
        }
        for (BaseBullet bullet : diedDiedBullets){
            diedBullets.remove(bullet);
        }
    }

    public void addBullet(BaseBullet bullet){
        bullets.add(bullet);
    }

    public void goBullets(long time, Graphics g) {
        ArrayList<BaseBullet> bulletDiedInThisStep = new ArrayList<>();
        for (BaseBullet bullet : bullets){
            if (bullet.go(this, time, g)){
                bulletDiedInThisStep.add(bullet);
            }
        }
        for (BaseBullet bullet : bulletDiedInThisStep){
            bullets.remove(bullet);
            diedBullets.add(bullet);
        }
    }
}
