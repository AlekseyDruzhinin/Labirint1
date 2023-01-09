import java.awt.*;

public class StandardBullet extends BaseBullet {

    double size = (double)Constants.R * 2.0 / 10.0;
    double mV = Constants.V_NORMAL*10.0;

    public StandardBullet(double x, double y, Vector v, Human human) {
        super(x, y, v, human);
        this.v = v.normVector(mV);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect((int)(x-size/2), (int)(y-size/2), (int)(size/2), (int)(size/2));
        g.setColor(new Color(92, 221, 239, 255));
        g.drawLine((int)startX, (int)startY, (int)x, (int)y);
        g.setColor(Color.RED);

        //System.out.println(mV);
    }

    @Override
    public void paintLine(Graphics g){
        g.setColor(new Color(92, 221, 239, 255));
        g.drawLine((int)startX, (int)startY, (int)endX, (int)endY);
        g.setColor(Color.RED);
    }
}
