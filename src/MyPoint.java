import java.awt.*;

public class MyPoint {
    double x, y;

    public MyPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void print(Graphics g){
        g.setColor(Color.GREEN);
        g.fillRect((int)x, (int)y, 10, 10);
        g.setColor(Color.RED);
    }
}
