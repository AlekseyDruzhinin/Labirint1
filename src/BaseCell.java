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
    public void paintPart(Graphics g, BufferedImage image, BufferedImage imageDied, double k){
//        g.drawRect((int)x-r, (int)y-r, 2*r, 2*r);
        g.drawImage(imageDied, (int)x-r, (int)y-r, 2*r, (int)(2*r*k), null);
    }

    public void paint(Graphics g, BufferedImage image, BufferedImage imageDied, double k){
//        g.drawRect((int)x-r, (int)y-r, 2*r, 2*r);
        if (!iAmDied){
            g.drawImage(image, (int)x-r, (int)y-r, 2*r, 2*r, null);
        }else{
            g.drawImage(imageDied, (int)x-r, (int)y-r, (int)(2*r*k), (int)(2*r), null);
            g.drawImage(image, (int)(x-r+2*r*k), (int)y-r, (int)(2*r*(1-k)+4), (int)(2*r), null);
        }
    }
}
