import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Labirint {
    ArrayList<BaseSector> sectors = new ArrayList<>();
    ArrayList<BaseBullet> diedBullets = new ArrayList<>();//список пулек, которые умерли, но слеж ещё рисуется

    MyFrame panel;
    Random random = new Random();

    ArrayList<BaseBullet> userBullets = new ArrayList<>();

    int indexDied = 0;

    public Labirint(MyFrame panel) {
        this.panel = panel;
        sectors.add(new StandardSector(Constants.SDVIG, Constants.SDVIG, panel));
        addSector();
    }

    public BaseCell getCell(int i, int j, int k){
        return sectors.get(i).cells.get(j).get(k);
    }
    public void paint(Graphics g){
        for (BaseSector sector : sectors){
            sector.paint(g);
        }
        for (BaseBullet bullet : userBullets){
            bullet.paint(g);
            if (Constants.DEVELORER){
                g.setColor(Color.BLUE);
                g.fillRect((int)getCell(bullet.indexSector, bullet.i, bullet.j).x, (int)getCell(0, bullet.i, bullet.j).y, Constants.R, Constants.R);
                g.setColor(Color.RED);
            }
        }
        for (BaseBullet bullet : diedBullets){
            bullet.paintLine(g);
        }
    }

    public BaseSector getSector(int i){
        return sectors.get(i);
    }

    public void go(double v){
        for (BaseSector sector : sectors){
            sector.go(v);
        }
        for (BaseBullet bullet : userBullets){
            bullet.x += v;
        }
        for (BaseBullet bullet : diedBullets){
            bullet.x += v;
        }
    }

    public void addSector(){
        sectors.add(new StandardSector(sectors.get(sectors.size()-1).x+ sectors.get(sectors.size()-1).widthCnt * Constants.R * 2, Constants.SDVIG, panel));
        for (int i = 1; i < sectors.get(0).verticalWalls.get(0).size(); ++i){
            if (random.nextInt(3) == 0){
                sectors.get(sectors.size()-1).verticalWalls.get(0).get(i).flag = false;
                sectors.get(sectors.size()-2).verticalWalls.get(sectors.get(sectors.size()-2).verticalWalls.size()-1).get(i).flag = false;
            }
        }
    }

    public void update(long time){
        boolean itIsDied = sectors.get(indexDied).update((double)time * Constants.V_POLE);
        if (itIsDied){
            if (indexDied == 0){
                indexDied = 1;
            }else{
                sectors.remove(0);
            }
        }
    }

    public void addBullet(BaseBullet bullet){
        userBullets.add(bullet);
    }

    public void goBullets(long time){
        ArrayList<BaseBullet> delBullets = new ArrayList<>();
        for (BaseBullet bullet : userBullets){
            if(!bullet.go(this, time)) {
                bullet.died(this, time);
                delBullets.add(bullet);
            }
        }
        for (BaseBullet bullet : delBullets){
            userBullets.remove(bullet);
            diedBullets.add(bullet);
        }
        long nowTime = System.currentTimeMillis();
        for (BaseBullet bullet : diedBullets){
            if (nowTime - bullet.timeDied > Constants.TIME_LIFE_AFTER_DIED){
                diedBullets.remove(bullet);
            }
        }
    }
}
