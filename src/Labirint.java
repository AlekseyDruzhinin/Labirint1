import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Labirint {
    ArrayList<BaseSector> sectors = new ArrayList<>();
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
            g.setColor(Color.GREEN);
            g.fillRect((int)getCell(0, bullet.i, bullet.j).x, (int)getCell(0, bullet.i, bullet.j).y, Constants.R, Constants.R);
            g.setColor(Color.GREEN);
        }
    }

    public void go(double v){
        for (BaseSector sector : sectors){
            sector.go(v);
        }
        for (BaseBullet bullet : userBullets){
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
        for (BaseBullet bullet : userBullets){
            bullet.go(this, time);
        }
    }
}
