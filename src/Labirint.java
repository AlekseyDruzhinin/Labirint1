import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Labirint {
    ArrayList<BaseSector> sectors = new ArrayList<>();
    MyFrame panel;
    Random random = new Random();

    public Labirint(MyFrame panel) {
        this.panel = panel;
        sectors.add(new StandardSector(Constants.SDVIG, Constants.SDVIG, panel));
        addSector();
    }

    public void paint(Graphics g){
        for (BaseSector sector : sectors){
            sector.paint(g);
        }
    }

    public void go(double v){
        for (BaseSector sector : sectors){
            sector.go(v);
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
}
