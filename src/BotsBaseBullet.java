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
    public boolean go(Labirint labirint, long time, Graphics g, Human userHuman) {
        boolean flagIAmDied1 = this.checkDied(labirint, time, g);
        this.updateCell(labirint, segment.getX2() + (double) time * v.getX(), segment.getY2() + (double) time * v.getY());
        boolean flagIAmDied = flagIAmDied1;
        if (iAmWin(labirint, userHuman, time)){
            flagIAmDied = true;
            userHuman.hit();
        }
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
    public boolean iAmWin(Labirint labirint, Human userHuman, long time) {
        MyPoint A = new MyPoint(this.segment.getX2(), this.segment.getY2());
        MyPoint B = new MyPoint(this.segment.getX2() + (double)time * v.x, this.segment.getY2() + (double)time *v.y);
        MyPoint O = new MyPoint(userHuman.x, userHuman.y);
        Vector AB = new Vector(A, B);
        Vector BA = new Vector(B, A);
        Vector AO = new Vector(A, O);
        Vector BO = new Vector(B, O);

        boolean ans = false;
        if (A.lenght(O) < (double)Constants.R / 4.0) {
            ans = true;
        } else if (B.lenght(O) < (double)Constants.R / 4.0) {
            ans = true;
        } else if (AB.scalarComposition(AO) > 0.0 && BA.scalarComposition(BO) > 0.0) {
            double cosA = (AB.length * AB.length + AO.length * AO.length - BO.length * BO.length) / (2.0 * AB.length * AO.length);
            if (AO.length * Math.sqrt(1-cosA*cosA) < (double)Constants.R / 4.0) {
                ans = true;
            }
        }
        if (ans) {
            timeDied = System.currentTimeMillis();
            return true;
        } else {
            return false;
        }
    }
}
