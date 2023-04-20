import java.awt.*;
import java.io.File;

public class BaseBullet {
    Segment segment;
    Vector v;

    int i, j;
    int indexSector;

    MyPoint pointDiedBullet = new MyPoint(0.0, 0.0);

    long timeDied = 0;

    boolean flagDoneGun = false;

    public BaseBullet(double bornX, double bornY, double mouseX, double mouseY) {
        if (!flagDoneGun && Constants.MUST_PLAY_SOUND){
            new Thread(() -> {
                Sound sound = new Sound(SoundFiles.gun);
                sound.setVolume((float) 0.6);
                sound.play();
                sound.join();
                flagDoneGun = true;
            }).start();
        }
        this.segment = new Segment(bornX, bornY, bornX, bornY);
        this.v = new Vector(mouseX - bornX, mouseY - bornY);
        this.v.setLength(Constants.V_BULLET);
    }

    public BaseBullet(double bornX, double bornY, double mouseX, double mouseY, Human userHuman){
        if (!flagDoneGun && Constants.MUST_PLAY_SOUND){
            new Thread(() -> {
                Sound sound = new Sound(SoundFiles.gun);
                sound.setVolume((float) 0.6);
                sound.play();
                sound.join();
                flagDoneGun = true;
            }).start();
        }
        this.segment = new Segment(bornX, bornY, bornX, bornY);
        this.v = new Vector(mouseX - bornX, mouseY - bornY);
        this.v.setLength(Constants.V_BULLET);
        this.indexSector = userHuman.indexSector;
        this.i = userHuman.i;
        this.j = userHuman.j;
    }

    public BaseBullet(double bornX, double bornY, Vector v, Human userHuman){
        this.segment = new Segment(bornX, bornY, bornX, bornY);
        this.v = v;
        this.v.setLength(Constants.V_BULLET);
        this.indexSector = userHuman.indexSector;
        this.i = userHuman.i;
        this.j = userHuman.j;
    }

    public void print(Graphics g, Labirint labirint){
        print(g, labirint, new Color(144, 43, 187));
    }
    public void print(Graphics g, Labirint labirint, Color color) {
        g.setColor(color);
        int x1, y1;
        if (segment.getX1() < segment.getX2()){
            x1 = (int)(segment.getX2() - (double)Constants.SIZE_BULLET * (v.x/Math.sqrt((v.x*v.x + v.y*v.y))));
        }else{
            x1 = (int)(segment.getX2() - (double)Constants.SIZE_BULLET * (v.x/Math.sqrt((v.x*v.x + v.y*v.y))));
        }
        if (segment.getY1() < segment.getY2()){
            y1 = (int)(segment.getY2() - (double)Constants.SIZE_BULLET * (v.y/Math.sqrt((v.x*v.x + v.y*v.y))));
        }else{
            y1 = (int)(segment.getY2() - (double)Constants.SIZE_BULLET * (v.y/Math.sqrt((v.x*v.x + v.y*v.y))));
        }
//        g.drawRect(x1, y1, (int) segment.getX2(), (int) segment.getY2());
        g.fillOval(x1, y1, 8, 8);

        /*if (Constants.DEVELORER) {
            g.setColor(Color.BLUE);
            BaseCell cell = labirint.getCell(indexSector, i, j);
            g.fillRect((int) cell.x, (int) cell.y, Constants.R, Constants.R);
            g.setColor(Color.RED);
        }*/
    }

    public boolean go(Labirint labirint, long time, Graphics g, Human userHuman) {
        boolean flagIAmDied1 = this.checkDied(labirint, time, g, userHuman);
        this.updateCell(labirint, segment.getX2() + (double) time * v.getX(), segment.getY2() + (double) time * v.getY());
        boolean flagIAmDied = flagIAmDied1;
        for (BaseBot bot : labirint.bots){
            if (bot.indexSector <= userHuman.indexSector + 1 && bot.indexSector >= userHuman.indexSector - 1 && iAmWin(labirint, bot, time)){
                flagIAmDied = true;
                bot.hit();
            }
        }
        if (!flagIAmDied1) {
            segment.setX2((segment.getX2() + (double) time * v.getX()));
            segment.setY2((segment.getY2() + (double) time * v.getY()));
        }

        return flagIAmDied;
    }

    public void updateCell(Labirint labirint, double newX, double newY) {
        /*BaseCell cell = labirint.getCell(indexSector, i, j);
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
        */

    }

    public boolean checkDied(Labirint labirint, long time, Graphics g, Human userHuman) {
        //System.out.println(i + " " + j);
        if (segment.getX2() <= labirint.sectors.get(Math.max(0, userHuman.indexSector-1)).x){
            return true;
        }
        if (segment.getX2() >= labirint.sectors.get(Math.min(labirint.sectors.size()-1, userHuman.indexSector+2)).x){
            return true;
        }
        if (segment.getX2() <= labirint.sectors.get(0).cells.get(0).get(0).x-Constants.R){
            return true;
        }
        if (segment.getY2() <= labirint.sectors.get(0).cells.get(0).get(0).y-Constants.R){
            return true;
        }
        if (segment.getY2() >= labirint.sectors.get(0).cells.get(0).get(labirint.sectors.get(0).cells.get(0).size()-1).y+Constants.R){
            return true;
        }
        Segment jump = new Segment(segment.getX2(), segment.getY2(), segment.getX2() + (double) time * v.getX(), segment.getY2() + (double) time * v.getY());
        MyPoint pointFirstDied = new MyPoint(0.0, 0.0);
        MyPoint pointStart = new MyPoint(jump.getX1(), jump.getY1());
        for (int indexSector1 = Math.max(0, userHuman.indexSector-1); indexSector1 < Math.min(labirint.sectors.size(), userHuman.indexSector+2); ++indexSector1) {
//            System.out.println(Math.max(0, userHuman.indexSector-1) + " " + labirint.sectors.size() + " " + userHuman.indexSector+2 + " " + Math.min(labirint.sectors.size(), userHuman.indexSector+2));
            BaseSector sector = labirint.getSector(indexSector1);
            for (int i = 0; i < sector.cells.size(); ++i) {
                for (int j = 0; j < sector.cells.get(0).size(); ++j) {
                    MyPoint pointDied = new MyPoint(0.0, 0.0);
                    Segment wallp = new Segment(sector.parallelWalls.get(j).get(i + 1));
                    if (sector.parallelWalls.get(j).get(i + 1).flag == true) {
                        if (jump.isIntersection(wallp)) {
                            pointDied = jump.getIntersection(wallp);
//                        pointDied.print(g);
                        }
                    }

                    wallp = new Segment(sector.parallelWalls.get(j + 1).get(i + 1));
                    if (sector.parallelWalls.get(j + 1).get(i + 1).flag == true) {
                        if (jump.isIntersection(wallp)) {
                            pointDied = jump.getIntersection(wallp);
//                        pointDied.print(g);
                        }
                    }


                    Segment wallv = new Segment(sector.verticalWalls.get(i).get(j + 1));
                    if (sector.verticalWalls.get(i).get(j + 1).flag == true) {
                        if (jump.isIntersection(wallv)) {
                            pointDied = jump.getIntersection(wallv);
//                        pointDied.print(g);
                        }
                    }

                    wallv = new Segment(sector.verticalWalls.get(i + 1).get(j + 1));
                    if (sector.verticalWalls.get(i + 1).get(j + 1).flag == true) {
                        if (jump.isIntersection(wallv)) {
                            pointDied = jump.getIntersection(wallv);
//                        pointDied.print(g);
                        }
                    }
                    if (pointFirstDied.x == 0.0 && pointFirstDied.y == 0.0) {
                        pointFirstDied = new MyPoint(pointDied);
                    } else if (pointStart.lenght(pointFirstDied) > pointStart.lenght(pointDied)) {
                        pointFirstDied = new MyPoint(pointDied);
                    }
                }
            }
        }

        if (pointFirstDied.x != 0.0 && pointFirstDied.y != 0.0) {
            pointDiedBullet = new MyPoint(pointFirstDied);
            segment.setX2(pointDiedBullet.x);
            segment.setY2(pointDiedBullet.y);
            timeDied = System.currentTimeMillis();
            return true;
        } else {
            return false;
        }
    }

    public boolean iAmWin(Labirint labirint, BaseBot bot, long time) {
        MyPoint A = new MyPoint(this.segment.getX2(), this.segment.getY2());
        MyPoint B = new MyPoint(this.segment.getX2() + (double)time * v.x, this.segment.getY2() + (double)time *v.y);
        MyPoint O = new MyPoint(bot.x, bot.y);
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
