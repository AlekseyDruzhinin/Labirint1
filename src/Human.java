import java.awt.*;

public abstract class Human {
    double x, y; // координаты центра
    int i, j; // координаты ячейки

    double v = Constants.V_NORMAL; // скорость

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

    public void go(BaseSector sector){
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
            x -= v;
            if (x < sector.cells.get(i).get(j).x - Constants.R){
                i--;
            }
        }
        if (flagRight) {
            x += v;
            if (x > sector.cells.get(i).get(j).x + Constants.R){
                i++;
            }
        }

        if (!flagChit){
            while (y+Constants.R/2 > sector.parallelWalls.get(j+1).get(i+1).y && sector.parallelWalls.get(j+1).get(i+1).flag){
                y -= v;
            }
            while (y-Constants.R/2 < sector.parallelWalls.get(j).get(i+1).y && sector.parallelWalls.get(j).get(i+1).flag){
                y += v;
            }
            while (x-Constants.R/2 < sector.verticalWalls.get(i).get(j+1).x && sector.verticalWalls.get(i).get(j+1).flag){
                x += v;
            }
            while (x+Constants.R/2 > sector.verticalWalls.get(i+1).get(j+1).x && sector.verticalWalls.get(i+1).get(j+1).flag){
                x -= v;
            }
        }


    }
}
