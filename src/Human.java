import java.awt.*;

public abstract class Human {
    double x, y; // координаты центра
    int i, j; // координаты ячейки

    double v = Constants.V_NORMAL; // скорость

    boolean flagDown = false, flagUp = false, flagLeft = false, flagRight = false;

    public Human(int x, int y, int i, int j) {
        this.x = x;
        this.y = y;
        this.i = i;
        this.j = j;
    }

    public void paint(Graphics g){
        g.fillOval((int)x-Constants.R/2, (int)y-Constants.R/2, Constants.R, Constants.R);
    }

    public void go(BaseSector sector){
        if (flagDown) {
            y += v;
        }
        if (flagUp) {
            y -= v;
        }
        if (flagLeft) {
            x -= v;
        }
        if (flagRight) {
            x += v;
        }
    }
}
