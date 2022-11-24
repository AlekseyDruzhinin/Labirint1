import java.awt.*;

public abstract class BaseCell {
    protected int x, y; // координаты центра
    protected int r = Constants.R; // радиус клетки

    // на вход координаты левого верхнего угла
    public BaseCell(int x, int y) {
        this.x = x+r;
        this.y = y+r;
        this.r = r;
    }

    public void paint(Graphics g){
        g.drawRect(x-r, y-r, 2*r, 2*r);
    }
}
