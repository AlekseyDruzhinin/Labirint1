import java.awt.*;

public abstract class Human {
    double x, y; // координаты центра
    int i, j; // координаты ячейки
    int indexSector = 0; // номер сектора

    boolean flagGoSector = false;

    double vHuman = Constants.V_NORMAL; // скорость

    boolean flagDown = false, flagUp = false, flagLeft = false, flagRight = false;

    boolean flagChit = false;

    public Human(int x, int y, int i, int j) {
        this.x = x;
        this.y = y;
        this.i = i;
        this.j = j;
    }

    public void paint(Graphics g){
        g.setColor(new Color(0xF911F31D, true));
        g.fillOval((int)x-Constants.R/2, (int)y-Constants.R/2, Constants.R, Constants.R);
        g.setColor(Color.BLUE);
        g.fillOval((int)x-1, (int)y-1, 2, 2);
        g.setColor(Color.RED);
        if (flagChit){
            g.setColor(Color.GREEN);
            g.fillRect(0,0, 40, 40);
            g.setColor(Color.RED);
        }
    }

    public void go(Labirint labirint, long time){
        double v = (double)time * vHuman;
        if (indexSector >= labirint.sectors.size()-1){
            labirint.addSector();
        }
        BaseSector sector = labirint.sectors.get(indexSector);
        if (i >= sector.cells.size()/2-2){
            flagGoSector = true;
        }

        if (!flagGoSector){
            //переход по ячейкам
            if (flagDown) {
                y += v;
                if (y > sector.cells.get(i).get(j).y + Constants.R) {
                    j++;
                }
            }
            if (flagUp) {
                y -= v;
                if (y < sector.cells.get(i).get(j).y - Constants.R){
                    j--;
                }
            }
            if (flagLeft) {
                x -= v;
                if (x < sector.cells.get(i).get(j).x - Constants.R) {
                    i--;
                }
            }
            if (flagRight) {
                x += v;
                if (x > sector.cells.get(i).get(j).x + Constants.R) {
                    i++;
                }
            }

            //удары о стенки
            if (!flagChit){
                //ячейка в которой мы
                while (j+1 < sector.parallelWalls.size() && i+1 < sector.parallelWalls.get(j+1).size() && y+Constants.R/2 > sector.parallelWalls.get(j+1).get(i+1).y && sector.parallelWalls.get(j+1).get(i+1).flag){
                    y -= v;
                }
                while (j < sector.parallelWalls.size() && i+1 < sector.parallelWalls.get(j).size() && y-Constants.R/2 < sector.parallelWalls.get(j).get(i+1).y && sector.parallelWalls.get(j).get(i+1).flag){
                    y += v;
                }
                while (i < sector.verticalWalls.size() && j+1 < sector.verticalWalls.get(i).size() && x-Constants.R/2 < sector.verticalWalls.get(i).get(j+1).x && sector.verticalWalls.get(i).get(j+1).flag){
                    x += v;
                }
                while (i+1 < sector.verticalWalls.size() && j+1 < sector.verticalWalls.get(i+1).size() && x+Constants.R/2 > sector.verticalWalls.get(i+1).get(j+1).x && sector.verticalWalls.get(i+1).get(j+1).flag){
                    x -= v;
                }

                //ячейка слева
                while (j+1 < sector.parallelWalls.size() && i < sector.parallelWalls.get(j+1).size() && sector.parallelWalls.get(j+1).get(i).flag && x-Constants.R/2 < sector.parallelWalls.get(j+1).get(i).x + sector.parallelWalls.get(j+1).get(i).l && y+Constants.R/2 > sector.parallelWalls.get(j+1).get(i).y){
                    x += v;
                    y -= v;
                }
                while (j < sector.parallelWalls.size() && i < sector.parallelWalls.get(j).size() && sector.parallelWalls.get(j).get(i).flag && x-Constants.R/2 < sector.parallelWalls.get(j).get(i).x + sector.parallelWalls.get(j).get(i).l && y-Constants.R/2 < sector.parallelWalls.get(j).get(i).y){
                    x += v;
                    y += v;
                }

                //ячейка справа
                while (j+1 < sector.parallelWalls.size() && i+2 < sector.parallelWalls.get(j+1).size() && sector.parallelWalls.get(j+1).get(i+2).flag && x+Constants.R/2 > sector.parallelWalls.get(j+1).get(i+2).x && y+Constants.R/2 > sector.parallelWalls.get(j+1).get(i+2).y){
                    x -= v;
                    y -= v;
                }
                while (j < sector.parallelWalls.size() && i+2 < sector.parallelWalls.get(j).size() && sector.parallelWalls.get(j).get(i+2).flag && x+Constants.R/2 > sector.parallelWalls.get(j).get(i+2).x && y-Constants.R/2 < sector.parallelWalls.get(j).get(i+2).y){
                    x -= v;
                    y += v;
                }


                //ячейка сверху
                while (i+1 < sector.verticalWalls.size() && j < sector.verticalWalls.get(i+1).size() && sector.verticalWalls.get(i+1).get(j).flag && y-Constants.R/2 < sector.verticalWalls.get(i+1).get(j).y + sector.verticalWalls.get(i).get(j).l && x+Constants.R/2 > sector.verticalWalls.get(i+1).get(j).x){
                    y += v;
                    x -= v;
                }
                while (i < sector.verticalWalls.size() && j < sector.verticalWalls.get(i).size() && sector.verticalWalls.get(i).get(j).flag && y-Constants.R/2 < sector.verticalWalls.get(i).get(j).y + sector.verticalWalls.get(i).get(j).l && x-Constants.R/2 < sector.verticalWalls.get(i).get(j).x){
                    y += v;
                    x += v;
                }

                //ячейка снизу
                while (i+1 < sector.verticalWalls.size() && j+2 < sector.verticalWalls.get(i+1).size() && sector.verticalWalls.get(i+1).get(j+2).flag && y+Constants.R/2 > sector.verticalWalls.get(i+1).get(j+2).y && x+Constants.R/2 > sector.verticalWalls.get(i+1).get(j+2).x){
                    y -= v;
                    x -= v;
                }
                while (i < sector.verticalWalls.size() && j+2 < sector.verticalWalls.get(i).size() && sector.verticalWalls.get(i).get(j+2).flag && y+Constants.R/2 > sector.verticalWalls.get(i).get(j+2).y && x-Constants.R/2 < sector.verticalWalls.get(i).get(j+2).x){
                    y -= v;
                    x += v;
                }
            }
        }else{
            if (flagDown) {
                y += v;
                if (y > sector.cells.get(i).get(j).y + Constants.R){
                    j++;
                }
            }
            if (flagUp) {
                y -= v;
                if (y < sector.cells.get(i).get(j).y - Constants.R){
                    j--;
                }
            }
            if (flagLeft) {
                labirint.go(v);
                if (x < sector.cells.get(i).get(j).x - Constants.R){
                    i--;
                }
                if (i < 0){
                    i = sector.cells.size()-1;
                    --indexSector;
                    sector = labirint.sectors.get(indexSector);
                }
            }
            if (flagRight) {
                labirint.go(-v);
                if (x > sector.cells.get(i).get(j).x + Constants.R){
                    i++;
                }
                if (i >= sector.cells.size()){
                    i = 0;
                    ++indexSector;
                    sector = labirint.sectors.get(indexSector);
                }
            }

            if (!flagChit){
                //ячейка в которой мы
                while (j+1 < sector.parallelWalls.size() && i+1 < sector.parallelWalls.get(j+1).size() && y+Constants.R/2 > sector.parallelWalls.get(j+1).get(i+1).y && sector.parallelWalls.get(j+1).get(i+1).flag){
                    y -= v;
                }
                while (j < sector.parallelWalls.size() && i+1 < sector.parallelWalls.get(j).size() && y-Constants.R/2 < sector.parallelWalls.get(j).get(i+1).y && sector.parallelWalls.get(j).get(i+1).flag){
                    y += v;
                }
                while (i < sector.verticalWalls.size() && j+1 < sector.verticalWalls.get(i).size() && x-Constants.R/2 < sector.verticalWalls.get(i).get(j+1).x && sector.verticalWalls.get(i).get(j+1).flag){
                    labirint.go(-v);
                }
                while (i+1 < sector.verticalWalls.size() && j+1 < sector.verticalWalls.get(i+1).size() && x+Constants.R/2 > sector.verticalWalls.get(i+1).get(j+1).x && sector.verticalWalls.get(i+1).get(j+1).flag){
                    labirint.go(v);
                }

                //ячейка слева
                while (j+1 < sector.parallelWalls.size() && i < sector.parallelWalls.get(j+1).size() && sector.parallelWalls.get(j+1).get(i).flag && x-Constants.R/2 < sector.parallelWalls.get(j+1).get(i).x + sector.parallelWalls.get(j+1).get(i).l && y+Constants.R/2 > sector.parallelWalls.get(j+1).get(i).y){
                    labirint.go(-v);
                    y -= v;
                }
                while (j < sector.parallelWalls.size() && i < sector.parallelWalls.get(j).size() && sector.parallelWalls.get(j).get(i).flag && x-Constants.R/2 < sector.parallelWalls.get(j).get(i).x + sector.parallelWalls.get(j).get(i).l && y-Constants.R/2 < sector.parallelWalls.get(j).get(i).y){
                    labirint.go(-v);
                    y += v;
                }

                //ячейка справа
                while (j+1 < sector.parallelWalls.size() && i+2 < sector.parallelWalls.get(j+1).size() && sector.parallelWalls.get(j+1).get(i+2).flag && x+Constants.R/2 > sector.parallelWalls.get(j+1).get(i+2).x && y+Constants.R/2 > sector.parallelWalls.get(j+1).get(i+2).y){
                    labirint.go(v);
                    y -= v;
                }
                while (j < sector.parallelWalls.size() && i+2 < sector.parallelWalls.get(j).size() && sector.parallelWalls.get(j).get(i+2).flag && x+Constants.R/2 > sector.parallelWalls.get(j).get(i+2).x && y-Constants.R/2 < sector.parallelWalls.get(j).get(i+2).y){
                    labirint.go(v);
                    y += v;
                }


                //ячейка сверху
                while (i+1 < sector.verticalWalls.size() && j < sector.verticalWalls.get(i+1).size() && sector.verticalWalls.get(i+1).get(j).flag && y-Constants.R/2 < sector.verticalWalls.get(i+1).get(j).y + sector.verticalWalls.get(i).get(j).l && x+Constants.R/2 > sector.verticalWalls.get(i+1).get(j).x){
                    y += v;
                    labirint.go(v);
                }
                while (i < sector.verticalWalls.size() && j < sector.verticalWalls.get(i).size() && sector.verticalWalls.get(i).get(j).flag && y-Constants.R/2 < sector.verticalWalls.get(i).get(j).y + sector.verticalWalls.get(i).get(j).l && x-Constants.R/2 < sector.verticalWalls.get(i).get(j).x){
                    y += v;
                    labirint.go(-v);
                }

                //ячейка снизу
                while (i+1 < sector.verticalWalls.size() && j+2 < sector.verticalWalls.get(i+1).size() && sector.verticalWalls.get(i+1).get(j+2).flag && y+Constants.R/2 > sector.verticalWalls.get(i+1).get(j+2).y && x+Constants.R/2 > sector.verticalWalls.get(i+1).get(j+2).x){
                    y -= v;
                    labirint.go(v);
                }
                while (i < sector.verticalWalls.size() && j+2 < sector.verticalWalls.get(i).size() && sector.verticalWalls.get(i).get(j+2).flag && y+Constants.R/2 > sector.verticalWalls.get(i).get(j+2).y && x-Constants.R/2 < sector.verticalWalls.get(i).get(j+2).x){
                    y -= v;
                    labirint.go(-v);
                }
            }
        }

    }

    public void update(Labirint labirint){
        BaseSector sector = labirint.sectors.get(indexSector);
        if (x - sector.x-Constants.R/2 < sector.rightBound){
            Constants.USER_DIED = true;
        }
    }
}

