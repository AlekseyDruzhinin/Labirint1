import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class BaseWall {
    public double x, y; // координаты левого верхнего угла
    public int l; // длинна стены (2r клетки);
    public boolean flag;
    BufferedImage image;

    public BaseWall(double x, double y, int l, boolean flag) {
        this.x = x;
        this.y = y;
        this.l = l;
        this.flag = flag;
    }


    public void paint(Graphics g, BufferedImage image){};
}
