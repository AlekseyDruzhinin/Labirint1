import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class PinkBot extends BaseBot{
    boolean flagVertical = false;

    int StartI;
    int StartJ;
    int StartSector;

    public PinkBot(double x, double y, int i, int j, int indexSector, Labirint labirint) throws IOException {
        super(x, y, i, j, indexSector, labirint);
        type = 3;
        colorBullets = new Color(236, 98, 11, 255);

        variantOrientation = Math.abs(random.nextInt()) % 4;
//        System.out.println(random.nextInt());

        this.image = ImageIO.read(new File("data\\Bot4.png"));

        double k = 2.0 * (double) Constants.R / (double) image.getHeight();
        tx = AffineTransform.getScaleInstance(k, k);
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        image = op.filter(image, null);

        this.rotate(0.0);
        StartI = i;
        StartJ = j;
        StartSector = indexSector;
    }

    @Override
    public void go(Human userHuman, long time, Labirint labirint) throws IOException {


        /*if (variantOrientation == 0) { //UP
            y -= Constants.V_BOTS * time;
        } else if (variantOrientation == 1) { //DOWN
            y += Constants.V_BOTS * time;
        } else if (variantOrientation == 2) { //LEFT
            x -= Constants.V_BOTS * time;
        } else { //RIGHT
            x += Constants.V_BOTS * time;
        }*/

        double v = (double) time * Constants.V_BOTS;
        BaseSector sector = labirint.getSector(indexSector);

        //переход по ячейкам
        if (variantOrientation == 1) {
            y += v;
            if (y > labirint.getCell(indexSector, i, labirint.sectors.get(indexSector).cells.get(i).size()-1).y + Constants.R){
                y -= v;
                variantOrientation = 0;
            }
            while (y > labirint.getCell(indexSector, i, j).y + Constants.R) {
                ++j;
            }
        }
        if (variantOrientation == 0) {
            y -= v;
            if (y < labirint.getCell(indexSector, i, 0).y - Constants.R){
                y += v;
                variantOrientation = 1;
            }
            while (y < labirint.getCell(indexSector, i, j).y - Constants.R) {
                --j;
            }
        }
        if (variantOrientation == 2) {
            x -= v;
            if (x < labirint.getCell(0, 0, j).x - Constants.R){
                x += v;
                variantOrientation = 3;
            }
            while (x < labirint.getCell(indexSector, i, j).x - Constants.R) {
                --i;
                if (i < 0) {
                    i = labirint.getSector(indexSector).cells.size() - 1;
                    --indexSector;
                    sector = labirint.sectors.get(indexSector);
                }
            }
        }
        if (variantOrientation == 3) {
            x += v;
            if (x > labirint.getCell(labirint.sectors.size()-1, labirint.sectors.get(labirint.sectors.size()-1).cells.size()-1, j).x + Constants.R){
                x -= v;
                variantOrientation = 2;
            }
            if (x > labirint.getCell(indexSector, i, j).x + Constants.R) {
                ++i;
                if (i >= labirint.getSector(indexSector).cells.size()) {
                    i = 0;
                    ++indexSector;
                    sector = labirint.sectors.get(indexSector);
                }
            }
        }

        //удары о стенки
        //ячейка в которой мы
        if (j + 1 < sector.parallelWalls.size() && i + 1 < sector.parallelWalls.get(j + 1).size() && y + Constants.R / 2 > sector.parallelWalls.get(j + 1).get(i + 1).y && sector.parallelWalls.get(j + 1).get(i + 1).flag) {
            variantOrientation = 0;
        }
        if (j < sector.parallelWalls.size() && i + 1 < sector.parallelWalls.get(j).size() && y - Constants.R / 2 < sector.parallelWalls.get(j).get(i + 1).y && sector.parallelWalls.get(j).get(i + 1).flag) {
            variantOrientation = 1;
        }
        if (i < sector.verticalWalls.size() && j + 1 < sector.verticalWalls.get(i).size() && x - Constants.R / 2 < sector.verticalWalls.get(i).get(j + 1).x && sector.verticalWalls.get(i).get(j + 1).flag) {
            variantOrientation = 3;
        }
        if (i + 1 < sector.verticalWalls.size() && j + 1 < sector.verticalWalls.get(i + 1).size() && x + Constants.R / 2 > sector.verticalWalls.get(i + 1).get(j + 1).x && sector.verticalWalls.get(i + 1).get(j + 1).flag) {
            variantOrientation = 2;
        }
        while (j + 1 < sector.parallelWalls.size() && i + 1 < sector.parallelWalls.get(j + 1).size() && y + Constants.R / 2 > sector.parallelWalls.get(j + 1).get(i + 1).y && sector.parallelWalls.get(j + 1).get(i + 1).flag) {
            y -= v;
        }
        while (j < sector.parallelWalls.size() && i + 1 < sector.parallelWalls.get(j).size() && y - Constants.R / 2 < sector.parallelWalls.get(j).get(i + 1).y && sector.parallelWalls.get(j).get(i + 1).flag) {
            y += v;
        }
        while (i < sector.verticalWalls.size() && j + 1 < sector.verticalWalls.get(i).size() && x - Constants.R / 2 < sector.verticalWalls.get(i).get(j + 1).x && sector.verticalWalls.get(i).get(j + 1).flag) {
            x += v;
        }
        while (i + 1 < sector.verticalWalls.size() && j + 1 < sector.verticalWalls.get(i + 1).size() && x + Constants.R / 2 > sector.verticalWalls.get(i + 1).get(j + 1).x && sector.verticalWalls.get(i + 1).get(j + 1).flag) {
            x -= v;
        }

        //ячейка слева
        while (j + 1 < sector.parallelWalls.size() && i < sector.parallelWalls.get(j + 1).size() && sector.parallelWalls.get(j + 1).get(i).flag && x - Constants.R / 2 < sector.parallelWalls.get(j + 1).get(i).x + sector.parallelWalls.get(j + 1).get(i).l && y + Constants.R / 2 > sector.parallelWalls.get(j + 1).get(i).y) {
            x += v;
            y -= v;
        }
        while (j < sector.parallelWalls.size() && i < sector.parallelWalls.get(j).size() && sector.parallelWalls.get(j).get(i).flag && x - Constants.R / 2 < sector.parallelWalls.get(j).get(i).x + sector.parallelWalls.get(j).get(i).l && y - Constants.R / 2 < sector.parallelWalls.get(j).get(i).y) {
            x += v;
            y += v;
        }

        //ячейка справа
        while (j + 1 < sector.parallelWalls.size() && i + 2 < sector.parallelWalls.get(j + 1).size() && sector.parallelWalls.get(j + 1).get(i + 2).flag && x + Constants.R / 2 > sector.parallelWalls.get(j + 1).get(i + 2).x && y + Constants.R / 2 > sector.parallelWalls.get(j + 1).get(i + 2).y) {
            x -= v;
            y -= v;
        }
        while (j < sector.parallelWalls.size() && i + 2 < sector.parallelWalls.get(j).size() && sector.parallelWalls.get(j).get(i + 2).flag && x + Constants.R / 2 > sector.parallelWalls.get(j).get(i + 2).x && y - Constants.R / 2 < sector.parallelWalls.get(j).get(i + 2).y) {
            x -= v;
            y += v;
        }


        //ячейка сверху
        while (i + 1 < sector.verticalWalls.size() && j < sector.verticalWalls.get(i + 1).size() && sector.verticalWalls.get(i + 1).get(j).flag && y - Constants.R / 2 < sector.verticalWalls.get(i + 1).get(j).y + sector.verticalWalls.get(i).get(j).l && x + Constants.R / 2 > sector.verticalWalls.get(i + 1).get(j).x) {
            y += v;
            x -= v;
        }
        while (i < sector.verticalWalls.size() && j < sector.verticalWalls.get(i).size() && sector.verticalWalls.get(i).get(j).flag && y - Constants.R / 2 < sector.verticalWalls.get(i).get(j).y + sector.verticalWalls.get(i).get(j).l && x - Constants.R / 2 < sector.verticalWalls.get(i).get(j).x) {
            y += v;
            x += v;
        }

        //ячейка снизу
        while (i + 1 < sector.verticalWalls.size() && j + 2 < sector.verticalWalls.get(i + 1).size() && sector.verticalWalls.get(i + 1).get(j + 2).flag && y + Constants.R / 2 > sector.verticalWalls.get(i + 1).get(j + 2).y && x + Constants.R / 2 > sector.verticalWalls.get(i + 1).get(j + 2).x) {
            y -= v;
            x -= v;
        }
        while (i < sector.verticalWalls.size() && j + 2 < sector.verticalWalls.get(i).size() && sector.verticalWalls.get(i).get(j + 2).flag && y + Constants.R / 2 > sector.verticalWalls.get(i).get(j + 2).y && x - Constants.R / 2 < sector.verticalWalls.get(i).get(j + 2).x) {
            y -= v;
            x += v;
        }
    }
}
