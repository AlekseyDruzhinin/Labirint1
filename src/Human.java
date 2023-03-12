import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Human extends BaseHuman {

    double x, y; // координаты центра
    int i, j; // координаты ячейки
    int indexSector = 0; // номер сектора

    BufferedImage image;
    Aim aim = new Aim();

    boolean flagGoSector = false;

    double vHuman = Constants.V_NORMAL; // скорость

    boolean flagDown = false, flagUp = false, flagLeft = false, flagRight = false;

    boolean flagChit = false;

    double hp = 1.0; // hp - число от 0 до 1 (доля закрышенного прямоугольника)
    AffineTransform tx;
    AffineTransformOp op;

    double angleInRadians = 0.0; // Угол поворота в градусах

    public Human(double x, double y, int i, int j) throws IOException {
        this.x = x;
        this.y = y;
        this.i = i;
        this.j = j;
        this.image = ImageIO.read(new File("data\\Human2.png"));

        double k = 2.0 * (double) Constants.R / (double) image.getHeight();
        tx = AffineTransform.getScaleInstance(k, k);
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        image = op.filter(image, null);

        this.rotate(0.0);

    }

    public void paint(Graphics g) {
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
        if (flagChit) {
            if (Constants.DEVELORER){
                g.setColor(Color.GREEN);
                g.fillRect(0, 0, 40, 40);
                g.setColor(Color.RED);
            }
        }
        g.setColor(Color.RED);
        g.fillRect(Constants.SDVIG/2, Constants.SDVIG/2, (int)(((double)Constants.R*15.0) * hp), Constants.R/2);
        g.setColor(Color.BLACK);
        g.drawRect(Constants.SDVIG/2, Constants.SDVIG/2, (int)(((double)Constants.R*15.0)), Constants.R/2);
        g.setColor(Color.RED);

        aim.print(g);
    }

    public void hit(Graphics g, MyFrame frame){
        Constants.TIME_HIT = System.currentTimeMillis();
        hp -= Constants.DAMAGE_BOT_BULLET;
        if (hp <= 0.0){
            Constants.USER_DIED = true;
        }
        new Thread(() -> {
            Sound sound = new Sound(new File("data\\music\\damage.wav"));
            sound.setVolume((float) 0.8);
            sound.play();
            sound.join();
        }).start();
    }
    public void go(Labirint labirint, long time) throws IOException {
        double v = (double) time * vHuman;
        if (indexSector >= labirint.sectors.size() - 1) {
            labirint.addSector();
        }
        BaseSector sector = labirint.getSector(indexSector);
        if (i >= sector.cells.size() / 2 - 2) {
            flagGoSector = true;
        }

        if (!flagGoSector) {
            //переход по ячейкам
            if (flagDown) {
                y += v;
                if (y > labirint.getCell(indexSector, i, j).y + Constants.R) {
                    j += (y - labirint.getCell(indexSector, i, j).y + Constants.R) / (2 * Constants.R);
                }
            }
            if (flagUp) {
                y -= v;
                if (y < labirint.getCell(indexSector, i, j).y - Constants.R) {
                    j -= (-y + labirint.getCell(indexSector, i, j).y - Constants.R) / (2 * Constants.R);
                }
            }
            if (flagLeft) {
                x -= v;
                if (x < labirint.getCell(indexSector, i, j).x - Constants.R) {
                    i -= (-x + labirint.getCell(indexSector, i, j).x - Constants.R) / (2 * Constants.R);
                }
            }
            if (flagRight) {
                x += v;
                if (x > labirint.getCell(indexSector, i, j).x + Constants.R) {
                    i += (x - labirint.getCell(indexSector, i, j).x + Constants.R) / (2 * Constants.R);
                }
            }

            //удары о стенки
            if (!flagChit) {
                //ячейка в которой мы
                while (j + 1 < sector.parallelWalls.size() && i + 1 < sector.parallelWalls.get(j + 1).size() && y + Constants.R / 2 > sector.parallelWalls.get(j + 1).get(i + 1).y && sector.parallelWalls.get(j + 1).get(i + 1).flag) {
                    y -= v;
                }
                while (j < sector.parallelWalls.size() && i + 1 < sector.parallelWalls.get(j).size() && y - Constants.R / 2 < sector.parallelWalls.get(j).get(i + 1).y && sector.parallelWalls.get(j).get(i + 1).flag) {
                    y += v;
                }
                while (i < sector.verticalWalls.size() && j + 1 < sector.verticalWalls.get(i).size() && x - Constants.R / 2 < sector.verticalWalls.get(i).get(j + 1).x && sector.verticalWalls.get(i).get(j + 1).flag) {
                    x += v;
                }
                while (i + 1 < sector.verticalWalls.size() && j + 1 < sector.verticalWalls.get(i + 1).size() && x + Constants.R / 2 > sector.verticalWalls.get(i + 1).get(j + 1).x && sector.verticalWalls.get(i + 1).get(j + 1).flag) {
                    x -= v;
                }

                //ячейка слева
                while (j + 1 < sector.parallelWalls.size() && i < sector.parallelWalls.get(j + 1).size() && sector.parallelWalls.get(j + 1).get(i).flag && x - Constants.R / 2 < sector.parallelWalls.get(j + 1).get(i).x + sector.parallelWalls.get(j + 1).get(i).l && y + Constants.R / 2 > sector.parallelWalls.get(j + 1).get(i).y) {
                    x += v;
                    y -= v;
                }
                while (j < sector.parallelWalls.size() && i < sector.parallelWalls.get(j).size() && sector.parallelWalls.get(j).get(i).flag && x - Constants.R / 2 < sector.parallelWalls.get(j).get(i).x + sector.parallelWalls.get(j).get(i).l && y - Constants.R / 2 < sector.parallelWalls.get(j).get(i).y) {
                    x += v;
                    y += v;
                }

                //ячейка справа
                while (j + 1 < sector.parallelWalls.size() && i + 2 < sector.parallelWalls.get(j + 1).size() && sector.parallelWalls.get(j + 1).get(i + 2).flag && x + Constants.R / 2 > sector.parallelWalls.get(j + 1).get(i + 2).x && y + Constants.R / 2 > sector.parallelWalls.get(j + 1).get(i + 2).y) {
                    x -= v;
                    y -= v;
                }
                while (j < sector.parallelWalls.size() && i + 2 < sector.parallelWalls.get(j).size() && sector.parallelWalls.get(j).get(i + 2).flag && x + Constants.R / 2 > sector.parallelWalls.get(j).get(i + 2).x && y - Constants.R / 2 < sector.parallelWalls.get(j).get(i + 2).y) {
                    x -= v;
                    y += v;
                }


                //ячейка сверху
                while (i + 1 < sector.verticalWalls.size() && j < sector.verticalWalls.get(i + 1).size() && sector.verticalWalls.get(i + 1).get(j).flag && y - Constants.R / 2 < sector.verticalWalls.get(i + 1).get(j).y + sector.verticalWalls.get(i).get(j).l && x + Constants.R / 2 > sector.verticalWalls.get(i + 1).get(j).x) {
                    y += v;
                    x -= v;
                }
                while (i < sector.verticalWalls.size() && j < sector.verticalWalls.get(i).size() && sector.verticalWalls.get(i).get(j).flag && y - Constants.R / 2 < sector.verticalWalls.get(i).get(j).y + sector.verticalWalls.get(i).get(j).l && x - Constants.R / 2 < sector.verticalWalls.get(i).get(j).x) {
                    y += v;
                    x += v;
                }

                //ячейка снизу
                while (i + 1 < sector.verticalWalls.size() && j + 2 < sector.verticalWalls.get(i + 1).size() && sector.verticalWalls.get(i + 1).get(j + 2).flag && y + Constants.R / 2 > sector.verticalWalls.get(i + 1).get(j + 2).y && x + Constants.R / 2 > sector.verticalWalls.get(i + 1).get(j + 2).x) {
                    y -= v;
                    x -= v;
                }
                while (i < sector.verticalWalls.size() && j + 2 < sector.verticalWalls.get(i).size() && sector.verticalWalls.get(i).get(j + 2).flag && y + Constants.R / 2 > sector.verticalWalls.get(i).get(j + 2).y && x - Constants.R / 2 < sector.verticalWalls.get(i).get(j + 2).x) {
                    y -= v;
                    x += v;
                }
            }
        } else {
            if (flagDown) {
                y += v;
                while (y > labirint.getCell(indexSector, i, j).y + Constants.R) {
                    ++j;
                }
            }
            if (flagUp) {
                y -= v;
                while (y < labirint.getCell(indexSector, i, j).y - Constants.R) {
                    --j;
                }
            }
            if (flagLeft) {
                labirint.go(v);
                while (x < labirint.getCell(indexSector, i, j).x - Constants.R) {
                  --i;
                  if (i < 0) {
                      i = labirint.getSector(indexSector).cells.size() - 1;
                      --indexSector;
                      sector = labirint.sectors.get(indexSector);
                  }
                }
            }
            if (flagRight) {
                labirint.go(-v);
                if (x > labirint.getCell(indexSector, i, j).x + Constants.R) {
                    ++i;
                    if (i >= labirint.getSector(indexSector).cells.size()) {
                        i = 0;
                        ++indexSector;
                        sector = labirint.sectors.get(indexSector);
                    }
                }
            }

            if (!flagChit) {
                //ячейка в которой мы
                while (j + 1 < sector.parallelWalls.size() && i + 1 < sector.parallelWalls.get(j + 1).size() && y + Constants.R / 2 > sector.parallelWalls.get(j + 1).get(i + 1).y && sector.parallelWalls.get(j + 1).get(i + 1).flag) {
                    y -= v;
                }
                while (j < sector.parallelWalls.size() && i + 1 < sector.parallelWalls.get(j).size() && y - Constants.R / 2 < sector.parallelWalls.get(j).get(i + 1).y && sector.parallelWalls.get(j).get(i + 1).flag) {
                    y += v;
                }
                while (i < sector.verticalWalls.size() && j + 1 < sector.verticalWalls.get(i).size() && x - Constants.R / 2 < sector.verticalWalls.get(i).get(j + 1).x && sector.verticalWalls.get(i).get(j + 1).flag) {
                    labirint.go(-v);
                }
                while (i + 1 < sector.verticalWalls.size() && j + 1 < sector.verticalWalls.get(i + 1).size() && x + Constants.R / 2 > sector.verticalWalls.get(i + 1).get(j + 1).x && sector.verticalWalls.get(i + 1).get(j + 1).flag) {
                    labirint.go(v);
                }

                //ячейка слева
                while (j + 1 < sector.parallelWalls.size() && i < sector.parallelWalls.get(j + 1).size() && sector.parallelWalls.get(j + 1).get(i).flag && x - Constants.R / 2 < sector.parallelWalls.get(j + 1).get(i).x + sector.parallelWalls.get(j + 1).get(i).l && y + Constants.R / 2 > sector.parallelWalls.get(j + 1).get(i).y) {
                    labirint.go(-v);
                    y -= v;
                }
                while (j < sector.parallelWalls.size() && i < sector.parallelWalls.get(j).size() && sector.parallelWalls.get(j).get(i).flag && x - Constants.R / 2 < sector.parallelWalls.get(j).get(i).x + sector.parallelWalls.get(j).get(i).l && y - Constants.R / 2 < sector.parallelWalls.get(j).get(i).y) {
                    labirint.go(-v);
                    y += v;
                }

                //ячейка справа
                while (j + 1 < sector.parallelWalls.size() && i + 2 < sector.parallelWalls.get(j + 1).size() && sector.parallelWalls.get(j + 1).get(i + 2).flag && x + Constants.R / 2 > sector.parallelWalls.get(j + 1).get(i + 2).x && y + Constants.R / 2 > sector.parallelWalls.get(j + 1).get(i + 2).y) {
                    labirint.go(v);
                    y -= v;
                }
                while (j < sector.parallelWalls.size() && i + 2 < sector.parallelWalls.get(j).size() && sector.parallelWalls.get(j).get(i + 2).flag && x + Constants.R / 2 > sector.parallelWalls.get(j).get(i + 2).x && y - Constants.R / 2 < sector.parallelWalls.get(j).get(i + 2).y) {
                    labirint.go(v);
                    y += v;
                }


                //ячейка сверху
                while (i + 1 < sector.verticalWalls.size() && j < sector.verticalWalls.get(i + 1).size() && sector.verticalWalls.get(i + 1).get(j).flag && y - Constants.R / 2 < sector.verticalWalls.get(i + 1).get(j).y + sector.verticalWalls.get(i).get(j).l && x + Constants.R / 2 > sector.verticalWalls.get(i + 1).get(j).x) {
                    y += v;
                    labirint.go(v);
                }
                while (i < sector.verticalWalls.size() && j < sector.verticalWalls.get(i).size() && sector.verticalWalls.get(i).get(j).flag && y - Constants.R / 2 < sector.verticalWalls.get(i).get(j).y + sector.verticalWalls.get(i).get(j).l && x - Constants.R / 2 < sector.verticalWalls.get(i).get(j).x) {
                    y += v;
                    labirint.go(-v);
                }

                //ячейка снизу
                while (i + 1 < sector.verticalWalls.size() && j + 2 < sector.verticalWalls.get(i + 1).size() && sector.verticalWalls.get(i + 1).get(j + 2).flag && y + Constants.R / 2 > sector.verticalWalls.get(i + 1).get(j + 2).y && x + Constants.R / 2 > sector.verticalWalls.get(i + 1).get(j + 2).x) {
                    y -= v;
                    labirint.go(v);
                }
                while (i < sector.verticalWalls.size() && j + 2 < sector.verticalWalls.get(i).size() && sector.verticalWalls.get(i).get(j + 2).flag && y + Constants.R / 2 > sector.verticalWalls.get(i).get(j + 2).y && x - Constants.R / 2 < sector.verticalWalls.get(i).get(j + 2).x) {
                    y -= v;
                    labirint.go(-v);
                }
            }
        }

    }

    public void rotate(double angleInRadians) {
        double locationX = image.getWidth() / 2;
        double locationY = image.getHeight() / 2;
        tx = AffineTransform.getRotateInstance(angleInRadians, locationX, locationY);
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
    }

    public void rotateToAim(Point gMP){
        if (aim.flagPrint ){
            Vector vectorAim = new Vector(-(double)aim.bot.x+this.x, -(double)aim.bot.y + this.y);
            Vector vectorUp = new Vector(0.0, 1.0);
            this.rotate(Math.atan2(vectorUp.vectorComposition(vectorAim), vectorUp.scalarComposition(vectorAim)));
        }else{
            if (gMP != null){
                Vector vectorMouse = new Vector(-(double)gMP.x+this.x, -(double)gMP.y + this.y);
                Vector vectorUp = new Vector(0.0, 1.0);
                this.rotate(Math.atan2(vectorUp.vectorComposition(vectorMouse), vectorUp.scalarComposition(vectorMouse)));
                //System.out.println(vectorMouse.x + " " + vectorMouse.y);
            }
        }
    }

    public void update(Labirint labirint) {
        BaseSector sector = labirint.sectors.get(labirint.indexDied);
        if (x - sector.x - Constants.R / 2 < sector.rightBound) {
            Constants.USER_DIED = true;
        }
    }
}

