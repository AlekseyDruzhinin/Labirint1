import java.awt.*;

public abstract class BaseBullet {
    double x, y;
    Vector v;

    double size;

    int indexSector;

    int i, j;
    double mV;

    public BaseBullet(double x, double y, Vector v, Human human) {
        this.x = x;
        this.y = y;
        this.v = v;
        this.i = human.i;
        this.j = human.j;
        this.indexSector = human.indexSector;
    }

    public void paint(Graphics g){

    }

    public boolean go(Labirint labirint, long time){
        if (y > labirint.getCell(indexSector, i, j).y + Constants.R) {
            j += (y - labirint.getCell(indexSector, i, j).y + Constants.R) / (2 * Constants.R);
        }
        if (y < labirint.getCell(indexSector, i, j).y - Constants.R) {
            j -= (-y + labirint.getCell(indexSector, i, j).y - Constants.R) / (2 * Constants.R);
        }
        if (x < labirint.getCell(indexSector, i, j).x - Constants.R) {
            i -= (-x + labirint.getCell(indexSector, i, j).x - Constants.R) / (2 * Constants.R);
        }
        if (i < 0) {
            i = labirint.getSector(indexSector).cells.size() - i;
            --indexSector;
        }
        if (x > labirint.getCell(indexSector, i, j).x + Constants.R) {
            i += (x - labirint.getCell(indexSector, i, j).x + Constants.R) / (2 * Constants.R);
        }
        if (i >= labirint.getSector(indexSector).cells.size()) {
            i %= labirint.getSector(indexSector).cells.size();
            ++indexSector;
        }
//        System.out.println(indexSector);
        x += (double) time * v.x;
        y += (double) time * v.y;
//        System.out.println(mV);

        BaseSector sector = labirint.getSector(indexSector);
        //ячейка в которой мы
        while (j + 1 < sector.parallelWalls.size() && i + 1 < sector.parallelWalls.get(j + 1).size() && y + Constants.R / 2 > sector.parallelWalls.get(j + 1).get(i + 1).y && sector.parallelWalls.get(j + 1).get(i + 1).flag) {
            return false;
        }
        while (j < sector.parallelWalls.size() && i + 1 < sector.parallelWalls.get(j).size() && y - Constants.R / 2 < sector.parallelWalls.get(j).get(i + 1).y && sector.parallelWalls.get(j).get(i + 1).flag) {
            return false;
        }
        while (i < sector.verticalWalls.size() && j + 1 < sector.verticalWalls.get(i).size() && x - Constants.R / 2 < sector.verticalWalls.get(i).get(j + 1).x && sector.verticalWalls.get(i).get(j + 1).flag) {
            return false;
        }
        while (i + 1 < sector.verticalWalls.size() && j + 1 < sector.verticalWalls.get(i + 1).size() && x + Constants.R / 2 > sector.verticalWalls.get(i + 1).get(j + 1).x && sector.verticalWalls.get(i + 1).get(j + 1).flag) {
            return false;
        }

        //ячейка слева
        while (j + 1 < sector.parallelWalls.size() && i < sector.parallelWalls.get(j + 1).size() && sector.parallelWalls.get(j + 1).get(i).flag && x - Constants.R / 2 < sector.parallelWalls.get(j + 1).get(i).x + sector.parallelWalls.get(j + 1).get(i).l && y + Constants.R / 2 > sector.parallelWalls.get(j + 1).get(i).y) {
            return false;
        }
        while (j < sector.parallelWalls.size() && i < sector.parallelWalls.get(j).size() && sector.parallelWalls.get(j).get(i).flag && x - Constants.R / 2 < sector.parallelWalls.get(j).get(i).x + sector.parallelWalls.get(j).get(i).l && y - Constants.R / 2 < sector.parallelWalls.get(j).get(i).y) {
            return false;
        }

        //ячейка справа
        while (j + 1 < sector.parallelWalls.size() && i + 2 < sector.parallelWalls.get(j + 1).size() && sector.parallelWalls.get(j + 1).get(i + 2).flag && x + Constants.R / 2 > sector.parallelWalls.get(j + 1).get(i + 2).x && y + Constants.R / 2 > sector.parallelWalls.get(j + 1).get(i + 2).y) {
            return false;
        }
        while (j < sector.parallelWalls.size() && i + 2 < sector.parallelWalls.get(j).size() && sector.parallelWalls.get(j).get(i + 2).flag && x + Constants.R / 2 > sector.parallelWalls.get(j).get(i + 2).x && y - Constants.R / 2 < sector.parallelWalls.get(j).get(i + 2).y) {
            return false;
        }


        //ячейка сверху
        while (i + 1 < sector.verticalWalls.size() && j < sector.verticalWalls.get(i + 1).size() && sector.verticalWalls.get(i + 1).get(j).flag && y - Constants.R / 2 < sector.verticalWalls.get(i + 1).get(j).y + sector.verticalWalls.get(i).get(j).l && x + Constants.R / 2 > sector.verticalWalls.get(i + 1).get(j).x) {
            return false;
        }
        while (i < sector.verticalWalls.size() && j < sector.verticalWalls.get(i).size() && sector.verticalWalls.get(i).get(j).flag && y - Constants.R / 2 < sector.verticalWalls.get(i).get(j).y + sector.verticalWalls.get(i).get(j).l && x - Constants.R / 2 < sector.verticalWalls.get(i).get(j).x) {
            return false;
        }

        //ячейка снизу
        while (i + 1 < sector.verticalWalls.size() && j + 2 < sector.verticalWalls.get(i + 1).size() && sector.verticalWalls.get(i + 1).get(j + 2).flag && y + Constants.R / 2 > sector.verticalWalls.get(i + 1).get(j + 2).y && x + Constants.R / 2 > sector.verticalWalls.get(i + 1).get(j + 2).x) {
            return false;
        }
        while (i < sector.verticalWalls.size() && j + 2 < sector.verticalWalls.get(i).size() && sector.verticalWalls.get(i).get(j + 2).flag && y + Constants.R / 2 > sector.verticalWalls.get(i).get(j + 2).y && x - Constants.R / 2 < sector.verticalWalls.get(i).get(j + 2).x) {
            return false;
        }
        return true;
    }
}
