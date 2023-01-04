import java.awt.*;

public class StandardBullet extends BaseBullet {

    double size = (double)Constants.R * 2.0 / 10.0;
    double mV = Constants.V_NORMAL*10.0;

    public StandardBullet(double x, double y, Vector v, Human human) {
        super(x, y, v, human);
        this.x = x;
        this.y = y;
        this.v = v.normVector(mV);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect((int)(x-size/2), (int)(y-size/2), (int)(size/2), (int)(size/2));
        g.setColor(Color.RED);
        //System.out.println(mV);
    }
}
