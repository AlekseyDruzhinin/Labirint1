import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class GreenBot extends BaseBot {


    public GreenBot(double x, double y, int i, int j, int indexSector) throws IOException {
        super(x, y, i, j, indexSector);

        colorBullets = new Color(236, 98, 11, 255);

        this.image = ImageIO.read(new File("data\\Bot3.png"));

        double k = 2.0 * (double) Constants.R / (double) image.getHeight();
        tx = AffineTransform.getScaleInstance(k, k);
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        image = op.filter(image, null);

        this.rotate(0.0);
    }

    @Override
    public void go(Human userHuman, long time, Labirint labirint) {
        int variantOrientation = random.nextInt(4);


        if (variantOrientation == 0) { //UP
            y -= Constants.V_BOTS * time;
        } else if (variantOrientation == 1) { //DOWN
            y += Constants.V_BOTS * time;
        } else if (variantOrientation == 2) { //LEFT
            x -= Constants.V_BOTS * time;
        } else { //RIGHT
            x += Constants.V_BOTS * time;
        }
    }
}
