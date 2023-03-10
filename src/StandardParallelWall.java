import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StandardParallelWall extends BaseWall{

    public StandardParallelWall(double x, double y, int l, boolean flag) throws IOException {
        super(x, y, l, flag);
        //image = ImageIO.read(new File("data\\parallel_wal.jpg"));
    }

    @Override
    public void paint(Graphics g){
        g.setColor(new Color(103, 80, 26, 255));
        g.fillRect((int)x, (int)y-2, l, 4);
        g.setColor(Color.RED);
        //g.drawImage(image, (int)x, (int)y-2, l, 4, null);
    }
}
