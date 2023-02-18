import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class BaseBot{

    double x, y; // координаты центра
    int i, j; // координаты ячейки
    int indexSector = 0; // номер сектора

    double hp = 1.0; // hp - число от 0 до 1 (доля закрышенного прямоугольника)

    BufferedImage image;
    AffineTransform tx;
    AffineTransformOp op;

    double angleInRadians = 0.0; // Угол поворота в градусах
    public BaseBot(double x, double y, int i, int j, int indexSector) throws IOException {
        this.x = x;
        this.y = y;
        this.i = i;
        this.j = j;
        this.indexSector = indexSector;
    }

    public void paint(Graphics g) {
        g.setColor(new Color(0xF911F31D, true));
        //g.fillOval((int)x-Constants.R/2, (int)y-Constants.R/2, Constants.R, Constants.R);
        //g.drawImage(image, (int)x-Constants.R/2, (int)y-Constants.R/2, Constants.R, Constants.R, null);
        //System.out.println(image.getWidth() + " " + image.getHeight());
        BufferedImage newImage = op.filter(image, null);
        //System.out.println(newImage.getWidth() + " " + newImage.getHeight());
        g.drawImage(newImage, (int) x - Constants.R, (int) y - Constants.R, null);
        g.setColor(Color.BLUE);
        g.fillOval((int) x - 1, (int) y - 1, 2, 2);
        g.setColor(Color.RED);
//        System.out.println(i + " " + j + " " + indexSector);
        g.setColor(Color.BLACK);
        g.fillRect((int)(x)-Constants.R, (int)y - Constants.R/2, (int)(((double)Constants.R*2) * hp), Constants.R/10);
    }

    public void rotate(double angleInRadians) {
        double locationX = image.getWidth() / 2;
        double locationY = image.getHeight() / 2;
        tx = AffineTransform.getRotateInstance(angleInRadians, locationX, locationY);
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
    }

    public void hit(){
        hp -= Constants.DAMAGE_USER_BULLET;
    }

    public void update(Labirint labirint, Human userHuman) {
        BaseSector sector = labirint.sectors.get(labirint.indexDied);
        if (x - sector.x - Constants.R / 2 < sector.rightBound) {
            hp = 0.0;
        }

        Vector vectorUp = new Vector(0.0, -1.0);
        Vector vectorHuman = new Vector(this.x, this.y, userHuman.x, userHuman.y);
        rotate(Math.atan2(vectorUp.vectorComposition(vectorHuman), vectorUp.scalarComposition(vectorHuman)));
    }
}
