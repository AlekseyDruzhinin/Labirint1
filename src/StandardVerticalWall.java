import java.awt.*;

public class StandardVerticalWall extends BaseWall{
    public StandardVerticalWall(int x, int y, int l) {
        super(x, y, l);
    }

    @Override
    public void paint(Graphics g){
        g.setColor(Color.ORANGE);
        g.fillRect(x-2, y, 4, l);
    }
}
