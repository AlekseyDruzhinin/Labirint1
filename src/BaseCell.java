import java.awt.*;

public abstract class BaseCell {
    protected double x, y; // координаты центра
    protected int r = Constants.R; // радиус клетки

    // на вход координаты левого верхнего угла
    public BaseCell(double x, double y) {
        this.x = x+r;
        this.y = y+r;
        this.r = r;
    }

    public BaseCell (BaseCell cell){
        this.x = cell.x;
        this.y = cell.y;
    }

    public void paint(Graphics g){
        g.drawRect((int)x-r, (int)y-r, 2*r, 2*r);
    }
}
