import java.awt.*;

public class StandardParallelWall extends BaseWall{
    public StandardParallelWall(int x, int y, int l, boolean flag) {
        super(x, y, l, flag);
    }

    @Override
    public void paint(Graphics g){
        g.setColor(Color.RED);
        g.fillRect(x, y-2, l, 4);
    }
}
