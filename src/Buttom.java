import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Buttom {

    double x, y;
    BufferedImage image;
    AffineTransform tx;
    AffineTransformOp op;
    double size;

    boolean isIt = true;

    public Buttom(double size, double x, double y, String adress, boolean isIt) throws IOException {
        this.isIt = isIt;
        this.size = size;
        image = ImageIO.read(new File(adress));
        double k = (size) / (double) image.getHeight();
        tx = AffineTransform.getScaleInstance(k, k);
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        image = op.filter(image, null);
        double locationX = image.getWidth() / 2;
        double locationY = image.getHeight() / 2;
        tx = AffineTransform.getRotateInstance(0.0, locationX, locationY);
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        this.x = x - image.getWidth() / 2;
        this.y = y;
    }
    public Buttom(double size, double x, double y, String adress) throws IOException {
        // получаем координаты центра верхней полоски и переводим в координаты левого угла
        this.size = size;
        image = ImageIO.read(new File(adress));
        double k = (size) / (double) image.getHeight();
        tx = AffineTransform.getScaleInstance(k, k);
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        image = op.filter(image, null);
        double locationX = image.getWidth() / 2;
        double locationY = image.getHeight() / 2;
        tx = AffineTransform.getRotateInstance(0.0, locationX, locationY);
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        this.x = x - image.getWidth() / 2;
        this.y = y;
    }

    public void paint(Graphics g, Point gM) {
        if (!isPush(gM)) {
            g.drawImage(image, (int) x, (int) y, null);
        } else {
            g.drawImage(image, (int) x-10, (int) y-10, image.getWidth() + 20, image.getHeight()+20, null);
        }
    }

    public boolean isPush(Point gM) {
        if (gM != null && x < gM.x && gM.x < x + image.getWidth() && y < gM.y && gM.y < y + image.getHeight()) {
            return true;
        }
        return false;
    }

    public void setImage(String adress) throws IOException {
        image = ImageIO.read(new File(adress));
        double k = (size) / (double) image.getHeight();
        tx = AffineTransform.getScaleInstance(k, k);
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        image = op.filter(image, null);
        double locationX = image.getWidth() / 2;
        double locationY = image.getHeight() / 2;
        tx = AffineTransform.getRotateInstance(0.0, locationX, locationY);
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
    }
}
