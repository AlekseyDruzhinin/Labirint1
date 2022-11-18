import java.awt.*;

public class StandardParallelWall extends BaseWall{
    public StandardParallelWall(int x, int y, int l) {
        super(x, y, l);
    }

    @Override
    public void paint(Graphics g){
        g.setColor(Color.RED);
        g.fillRect(x, y-2, l, 4);
    }
}
