import java.awt.*;
import java.awt.image.BufferedImage;

public class StandardVerticalWall extends BaseWall{
    public StandardVerticalWall(int x, int y, int l, boolean flag) {
        super(x, y, l, flag);
    }

    @Override
    public void paint(Graphics g, BufferedImage image){
        g.drawImage(image, (int)x-2, (int)y, 8, l, null);

//        g.setColor(new Color(103, 80, 26, 255));
//        g.fillRect((int)x-2, (int)y, 4, l);
//        g.setColor(Color.RED);
    }
}
