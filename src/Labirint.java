import java.awt.*;
import java.util.ArrayList;

public class Labirint {
    ArrayList<BaseSector> sectors = new ArrayList<>();

    public Labirint(MyFrame panel) {
        sectors.add(new StandardSector(Constants.SDVIG, Constants.SDVIG, panel));
        sectors.add(new StandardSector(Constants.SDVIG+sectors.get(0).widthCnt * Constants.R * 2, Constants.SDVIG, panel));
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
}
