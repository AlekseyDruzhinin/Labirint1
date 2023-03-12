import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class BaseCell {
    protected double x, y; // координаты центра
    protected int r = Constants.R; // радиус клетки

    boolean iAmDied = false;

    // на вход координаты левого верхнего угла
    public BaseCell(double x, double y) throws IOException {
        this.x = x+r;
        this.y = y+r;
        this.r = r;
    }

    public BaseCell (BaseCell cell){
        this.x = cell.x;
        this.y = cell.y;
    }

    public void paint(Graphics g, BufferedImage image){
//        g.drawRect((int)x-r, (int)y-r, 2*r, 2*r);
        if (!iAmDied){
            g.drawImage(image, (int)x-r, (int)y-r, 2*r, 2*r, null);
        }
    }
}
