import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.io.File;
import java.io.IOException;

public class OrangeBot extends BaseBot{

    public OrangeBot(double x, double y, int i, int j, int indexSector, Labirint labirint) throws IOException {
        super(x, y, i, j, indexSector, labirint);
        type = 2;

        colorBullets = new Color(236, 98, 11, 255);

        this.image = ImageIO.read(new File("data\\Bot2.png"));

        double k = 2.0 * (double) Constants.R / (double) image.getHeight();
        tx = AffineTransform.getScaleInstance(k, k);
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        image = op.filter(image, null);

        this.rotate(0.0);
    }
    @Override
    public void go(Human userHuman, long time, Labirint labirint){
        if (visu){
            Vector v = new Vector(userHuman.x - x, userHuman.y - y);
            v.setLength(Constants.V_BOTS * (double)time);
            x += v.x;
            y += v.y;

            //переход по ячейкам
            if (y > labirint.getCell(indexSector, i, j).y + Constants.R) {
                j += (y - labirint.getCell(indexSector, i, j).y + Constants.R) / (2 * Constants.R);
            }
            if (y < labirint.getCell(indexSector, i, j).y - Constants.R) {
                j -= (-y + labirint.getCell(indexSector, i, j).y - Constants.R) / (2 * Constants.R);
            }
            if (x < labirint.getCell(indexSector, i, j).x - Constants.R) {
                i -= (-x + labirint.getCell(indexSector, i, j).x - Constants.R) / (2 * Constants.R);
                if (i < 0) {
                    i = labirint.getSector(indexSector).cells.size() - 1;
                    --indexSector;
                }
            }
            if (x > labirint.getCell(indexSector, i, j).x + Constants.R) {
                i += (x - labirint.getCell(indexSector, i, j).x + Constants.R) / (2 * Constants.R);
                if (i >= labirint.getSector(indexSector).cells.size()) {
                    i = 0;
                    ++indexSector;
                }
            }
        }
    }
}
