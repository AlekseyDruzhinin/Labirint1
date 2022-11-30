import java.awt.*;

public class StandardVerticalWall extends BaseWall{
    public StandardVerticalWall(int x, int y, int l, boolean flag) {
        super(x, y, l, flag);
    }

    @Override
    public void paint(Graphics g){
        g.setColor(Color.RED);
        g.fillRect(x-2, y, 4, l);
    }
}
