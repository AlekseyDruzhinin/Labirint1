import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public abstract class BaseBot{
    double x, y; // координаты центра
    int i, j; // координаты ячейки
    int indexSector = 0; // номер сектора

    double hp = 1.0; // hp - число от 0 до 1 (доля закрышенного прямоугольника)

    long timeDelay = 500;
    long timeLastShot;

    BufferedImage image;
    BufferedImage imageAfterDied;
    AffineTransform tx;
    AffineTransformOp op;
    long timeDied;

    int type = 1;
    boolean visu = false;

    int variantOrientation;
    Random random = new Random();
    Color colorBullets;

    double angleInRadians = 0.0; // Угол поворота в градусах
    public BaseBot(double x, double y, int i, int j, int indexSector, Labirint labirint) throws IOException {
        this.x = x;
        this.y = y;
        this.i = i;
        this.j = j;
        this.indexSector = indexSector;
        timeLastShot = System.currentTimeMillis();
        imageAfterDied = ImageIO.read(new File("data\\diedBot1.png"));
    }

    public void paint(Graphics g) {
        random = new Random();
        g.setColor(new Color(0xF911F31D, true));
        //g.fillOval((int)x-Constants.R/2, (int)y-Constants.R/2, Constants.R, Constants.R);
        //g.drawImage(image, (int)x-Constants.R/2, (int)y-Constants.R/2, Constants.R, Constants.R, null);
        //System.out.println(image.getWidth() + " " + image.getHeight());
        BufferedImage newImage = op.filter(image, null);
        //System.out.println(newImage.getWidth() + " " + newImage.getHeight());
        g.drawImage(newImage, (int) x - Constants.R, (int) y - Constants.R, null);
        g.setColor(Color.BLUE);
        g.fillOval((int) x - 1, (int) y - 1, 2, 2);
        g.setColor(Color.RED);
//        System.out.println(i + " " + j + " " + indexSector);
        g.setColor(Color.RED);
        g.fillRect((int)(x)-Constants.R, (int)y - Constants.R/2, (int)(((double)Constants.R*2) * hp), Constants.R/10);
    }
    public void paintAfterDied(Graphics g, MyFrame frame) {
        random = new Random();
        g.setColor(new Color(0xF911F31D, true));
        Graphics2D g2d = (Graphics2D) g;
        float alpha = 1.0f - (float)(System.currentTimeMillis()-this.timeDied)/Constants.TIME_LIVE_BOT_AFTER_DIED;
        if (alpha > 0.0f && alpha <= 1.0f){
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.drawImage(imageAfterDied, (int) x - Constants.R, (int) y - Constants.R, 2*Constants.R, 2*Constants.R, null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }

    public void rotate(double angleInRadians) {
        double locationX = image.getWidth() / 2;
        double locationY = image.getHeight() / 2;
        tx = AffineTransform.getRotateInstance(angleInRadians, locationX, locationY);
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
    }

    public void hit(){
        if (Constants.MUST_PLAY_SOUND){
            new Thread(() -> {
                Sound sound = new Sound(SoundFiles.damage);
                sound.setVolume((float) 0.8);
                sound.play();
                sound.join();
            }).start();
        }
        hp -= Constants.DAMAGE_USER_BULLET;
        if (hp <= 0.0){
            Constants.CNT_DIED_BOTS.update();
        }
    }


    public void update(Labirint labirint, Human userHuman) {
        BaseSector sector = labirint.sectors.get(labirint.indexDied);
        if (x - sector.x - Constants.R / 2 < sector.rightBound) {
            hp = 0.0;
        }

        visu = true;
        Segment botHuman = new Segment(x, y, userHuman.x, userHuman.y);
        if (userHuman.indexSector >= this.indexSector && userHuman.indexSector - this.indexSector <= 2){
            for (int i = this.indexSector; i <= userHuman.indexSector; i++) {
                BaseSector sector1 = labirint.sectors.get(i);
                for (ArrayList<StandardParallelWall> wallps : sector1.parallelWalls) {
                    for (StandardParallelWall wallp : wallps) {
                        if (wallp.flag && botHuman.isIntersection(new Segment(wallp))) {
                            visu = false;
                        }
                    }
                }
                for (ArrayList<StandardVerticalWall> wallvs : sector1.verticalWalls) {
                    for (StandardVerticalWall wallv : wallvs) {
                        if (wallv.flag && botHuman.isIntersection(new Segment(wallv))) {
                            visu = false;
                        }
                    }
                }
            }
        }else if (userHuman.indexSector == this.indexSector - 1){
            for (int i = this.indexSector; i >= userHuman.indexSector; i--) {
                BaseSector sector1 = labirint.sectors.get(i);
                for (ArrayList<StandardParallelWall> wallps : sector1.parallelWalls) {
                    for (StandardParallelWall wallp : wallps) {
                        if (wallp.flag && botHuman.isIntersection(new Segment(wallp))) {
                            visu = false;
                        }
                    }
                }
                for (ArrayList<StandardVerticalWall> wallvs : sector1.verticalWalls) {
                    for (StandardVerticalWall wallv : wallvs) {
                        if (wallv.flag && botHuman.isIntersection(new Segment(wallv))) {
                            visu = false;
                        }
                    }
                }
            }
        }

        if (visu) {
            Vector vectorUp = new Vector(0.0, -1.0);
            Vector vectorHuman = new Vector(this.x, this.y, userHuman.x, userHuman.y);
            rotate(Math.atan2(vectorUp.vectorComposition(vectorHuman), vectorUp.scalarComposition(vectorHuman)));
        }
    }

    public void go(Human userHuman, long time, Labirint labirint) throws IOException {}
}
