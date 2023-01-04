import java.awt.*;

public abstract class BaseBullet {
    double x, y;
    Vector v;

    double size;

    int i, j;
    double mV;

    public BaseBullet(double x, double y, Vector v, Human human) {
        this.x = x;
        this.y = y;
        this.v = v;
        this.i = human.i;
        this.j = human.j;
    }

    public void paint(Graphics g){

    }

    public void go(Labirint labirint, long time){
        if (x + (double) time * v.x > labirint.getCell(0, i, j).x+Constants.R/2){
            j++;
        }
        if (x + (double) time * v.x < labirint.getCell(0, i, j).x-Constants.R/2){
            j--;
        }
        if (y + (double) time * v.y > labirint.getCell(0, i, j).y+Constants.R/2){
            i++;
        }
        if (y + (double) time * v.y < labirint.getCell(0, i, j).y-Constants.R/2){
            i--;
        }
        x += (double) time * v.x;
        y += (double) time * v.y;
//        System.out.println(mV);
    }
}
