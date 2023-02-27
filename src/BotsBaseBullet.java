import java.awt.*;

public class BotsBaseBullet extends BaseBullet{

    Color color;
    public BotsBaseBullet(double bornX, double bornY, double mouseX, double mouseY, BaseBot bot) {
        super(bornX, bornY, mouseX, mouseY);
        this.indexSector = bot.indexSector;
        this.i = bot.i;
        this.j = bot.j;
        this.color = bot.colorBullets;
    }

    @Override
    public boolean go(Labirint labirint, long time, Graphics g) {
        boolean flagIAmDied1 = this.checkDied(labirint, time, g);
        this.updateCell(labirint, segment.getX2() + (double) time * v.getX(), segment.getY2() + (double) time * v.getY());
        boolean flagIAmDied = flagIAmDied1;
        if (!flagIAmDied1) {
            segment.setX2((segment.getX2() + (double) time * v.getX()));
            segment.setY2((segment.getY2() + (double) time * v.getY()));
        }

        return flagIAmDied;
    }

    @Override
    public void print(Graphics g, Labirint labirint){
        print(g, labirint, color);
    }
}
