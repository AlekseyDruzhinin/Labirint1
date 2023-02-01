import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Labirint {
    ArrayList<BaseSector> sectors = new ArrayList<>();

    //массив ботов
    ArrayList<BaseBot> bots = new ArrayList<>();

    MyFrame panel;
    Random random = new Random();
    int indexDied = 0;

    public Labirint(MyFrame panel) throws IOException {
        this.panel = panel;
        sectors.add(new StandardSector(Constants.SDVIG, Constants.SDVIG, panel));
        RedBot redBot = new RedBot(getCell(0, 20, 10).x, getCell(0, 20, 10).y, 20, 10, 0);
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

    public void update(long time) {
        boolean itIsDied = sectors.get(indexDied).update((double) time * Constants.V_POLE);
        if (itIsDied) {
            if (indexDied == 0) {
                indexDied = 1;
            } else {
                sectors.remove(0);
            }
        }
    }

    public void goBullets(long time) {
    }
}
