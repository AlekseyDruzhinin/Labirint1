import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class BaseCell {
    protected double x, y; // координаты центра
    protected int r = Constants.R; // радиус клетки

    BufferedImage image;

    // на вход координаты левого верхнего угла
    public BaseCell(double x, double y) throws IOException {
        this.x = x+r;
        this.y = y+r;
        this.r = r;
        this.image = ImageIO.read(new File("data\\Pesok.jpg"));
    }

    public BaseCell (BaseCell cell){
        this.x = cell.x;
        this.y = cell.y;
    }

    public void paint(Graphics g){
//        g.drawRect((int)x-r, (int)y-r, 2*r, 2*r);
        g.drawImage(image, (int)x-r, (int)y-r, 2*r, 2*r, null);
    }
}
