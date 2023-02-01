import java.awt.*;

public class BaseBullet {
    Segment segment;
    Vector v;

    public BaseBullet(double bornX, double bornY, double mouseX, double mouseY) {
        this.segment = new Segment(bornX, bornY, bornX, bornY);
        this.v = new Vector(mouseX - bornX, mouseY - bornY);
        this.v.setLength(4.0 * Constants.V_NORMAL);
    }

    public void print(Graphics g){
        g.setColor(new Color(144, 43, 187));
        g.drawLine((int)segment.getX1(), (int)segment.getY1(), (int)segment.getX2(), (int)segment.getY2());
        g.setColor(Color.RED);
    }

    public void go(Labirint labirint, long time){
        segment.setX2(segment.getX2() + time*v.getX());
        segment.setY2(segment.getY2() + time*v.getY());
    }
}
