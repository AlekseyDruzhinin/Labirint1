import java.awt.*;

public class BaseBullet {
    Segment segment;
    Vector v;

    int i, j;
    int indexSector;

    public BaseBullet(double bornX, double bornY, double mouseX, double mouseY, Human userHuman) {
        this.segment = new Segment(bornX, bornY, bornX, bornY);
        this.v = new Vector(mouseX - bornX, mouseY - bornY);
        this.v.setLength(4.0 * Constants.V_NORMAL);
        this.indexSector = userHuman.indexSector;
        this.i = userHuman.i;
        this.j = userHuman.j;
    }

    public void print(Graphics g, Labirint labirint) {
        g.setColor(new Color(144, 43, 187));
        g.drawLine((int) segment.getX1(), (int) segment.getY1(), (int) segment.getX2(), (int) segment.getY2());

        if (Constants.DEVELORER) {
            g.setColor(Color.BLUE);
            BaseCell cell = labirint.getCell(indexSector, i, j);
            g.fillRect((int) cell.x, (int) cell.y, Constants.R, Constants.R);
            g.setColor(Color.RED);
        }
    }

    public void go(Labirint labirint, long time) {
        this.updateCell(labirint, segment.getX2() + (double) time * v.getX(), segment.getY2() + (double) time * v.getY());

        segment.setX2((int) (segment.getX2() + (double) time * v.getX()));
        segment.setY2((int) (segment.getY2() + (double) time * v.getY()));
    }

    public void updateCell(Labirint labirint, double newX, double newY) {
        BaseCell cell = labirint.getCell(indexSector, i, j);
        int di = 0;
        if (newX > cell.x + Constants.R) {
            di = ((int) newX - ((int) cell.x + Constants.R)) / Constants.R;
        } else if (newX < cell.x - Constants.R) {
            di = -(((int) cell.x - Constants.R) - (int) newX) / Constants.R;
        }

        int dj = 0;
        if (newY > cell.y + Constants.R) {
            dj = ((int) newY - ((int) cell.y + Constants.R)) / Constants.R;
        } else if (newY < cell.y - Constants.R) {
            dj = -(((int) cell.y - Constants.R) - (int) newY) / Constants.R;
        }

        BaseSector sector = labirint.getSector(indexSector);
        if (i + di >= sector.cells.size()) {
            indexSector++;
            i = (i + di) % sector.cells.size();
        } else if (i + di < 0) {
            indexSector--;
            i = sector.cells.size() + i + di;
        } else {
            i += di;
        }

        j += dj;
    }
}
