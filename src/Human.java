import java.awt.*;

public abstract class Human {
    int x, y; // координаты центра
    int i, j; // координаты ячейки

    int v; // скорость

    public Human(int x, int y, int i, int j, int v) {
        this.x = x;
        this.y = y;
        this.i = i;
        this.j = j;
        this.v = v;
    }

    public void paint(Graphics g){
        g.fillOval(x-Constants.R/2, y-Constants.R/2, Constants.R, Constants.R);
    }
}
