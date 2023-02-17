import java.awt.*;

public class MyPoint {
    double x, y;

    public MyPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public MyPoint(MyPoint point){
        this.x = point.x;;
        this.y = point.y;
    }

    public void print(Graphics g){
        g.setColor(Color.GREEN);
        g.fillRect((int)x, (int)y, 4, 4);
        g.setColor(Color.RED);
    }

    public double lenght(MyPoint point){
        return Math.sqrt((point.x-this.x)*(point.x-this.x) + (point.y-this.y)*(point.y-this.y));
    }
}
