import java.awt.*;

public abstract class BaseWall {
    protected int x, y; // координаты левого верхнего угла
    protected int l; // длинна стены (2r клетки);

    public BaseWall(int x, int y, int l) {
        this.x = x;
        this.y = y;
        this.l = l;
    }

    public void paint(Graphics g){};
}
