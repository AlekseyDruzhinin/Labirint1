import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.io.File;
import java.io.IOException;

public class RedBot extends BaseBot {

    public RedBot(double x, double y, int i, int j, int indexSector) throws IOException {
        super(x, y, i, j, indexSector);

        colorBullets = new Color(58, 14, 14, 255);

        this.image = ImageIO.read(new File("data\\Bot1.png"));

        double k = 2.0 * (double) Constants.R / (double) image.getHeight();
        tx = AffineTransform.getScaleInstance(k, k);
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        image = op.filter(image, null);

        this.rotate(0.0);
    }
}
