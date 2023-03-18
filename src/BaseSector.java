import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public abstract class BaseSector {
    protected double x, y; // координаты верхнего левого угла
    AffineTransform tx;
    AffineTransformOp op;

    double angleInRadians = 0.0; // Угол поворота в градусах
    int cnt = 0;
    protected int width;
    protected int height;
    int widthCnt;
    int heightCnt;

    BufferedImage image;

    boolean iAmDied = false;
    Random random = new Random();
    ArrayList<ArrayList<BaseCell>> cells = new ArrayList<>();
    ArrayList<ArrayList<StandardVerticalWall>> verticalWalls = new ArrayList<>(); // нумерация второго ряда с 1
    ArrayList<ArrayList<StandardParallelWall>> parallelWalls = new ArrayList<>();
    double rightBound = 0.0;

    public BaseSector(double x, double y, MyFrame frame) throws IOException {
        image = ImageIO.read(new File("data\\pesok.png"));

        tx = AffineTransform.getScaleInstance((double) Constants.R * 2 / (double) image.getHeight(), (double) Constants.R * 2 / (double) image.getHeight());
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        image = op.filter(image, null);

        this.rotate(0.0);

        this.x = x;
        this.y = y;
        this.width = frame.getWidth();
        this.height = frame.getHeight();

        StandardCell cell = new StandardCell(0, 0);
        {
            double i = this.x;
            for (int in = 0; i + 2 * cell.r < frame.getWidth() + x; i += Constants.R * 2.0, ++in) {
                cells.add(new ArrayList<BaseCell>());
                double j = this.y;
                for (int jn = 0; j + 2 * cell.r < frame.getHeight(); j += Constants.R * 2.0, ++jn) {
                    cells.get(in).add(new StandardCell(i, j));
                }
            }
        }


        this.widthCnt = cells.size();
        this.heightCnt = cells.get(0).size();

        int size_wight = cells.size();
        int size_height = cells.get(0).size();

        for (int i = 0; i <= size_wight; ++i) {
            verticalWalls.add(new ArrayList<StandardVerticalWall>());
            for (int j = 0; j <= size_height; ++j) {
                verticalWalls.get(i).add(new StandardVerticalWall(0, 0, 0, false));
            }
        }

        for (int i = 0; i <= size_height; ++i) {
            parallelWalls.add(new ArrayList<StandardParallelWall>());
            for (int j = 0; j <= size_wight; ++j) {
                parallelWalls.get(i).add(new StandardParallelWall(0, 0, 0, false));
            }
        }

        randomGenerate(0, 0, size_height, size_wight);

        for (int j = 1; j < parallelWalls.get(0).size(); ++j) {
            parallelWalls.get(0).get(j).flag = true;
            parallelWalls.get(0).get(j).y = y;
            parallelWalls.get(0).get(j).x = x + (j - 1) * 2 * Constants.R;
            parallelWalls.get(0).get(j).l = Constants.R * 2;
        }

        for (int j = 1; j < parallelWalls.get(parallelWalls.size() - 1).size(); ++j) {
            parallelWalls.get(parallelWalls.size() - 1).get(j).flag = true;
            parallelWalls.get(parallelWalls.size() - 1).get(j).y = cells.get(0).get(cells.get(0).size() - 1).y + Constants.R;
            parallelWalls.get(parallelWalls.size() - 1).get(j).x = x + (j - 1) * 2 * Constants.R;
            parallelWalls.get(parallelWalls.size() - 1).get(j).l = Constants.R * 2;
        }

        for (int j = 1; j < verticalWalls.get(0).size(); ++j) {
            verticalWalls.get(0).get(j).flag = true;
            verticalWalls.get(0).get(j).x = x;
            verticalWalls.get(0).get(j).y = y + (j - 1) * 2 * Constants.R;
            verticalWalls.get(0).get(j).l = Constants.R * 2;
        }

        for (int j = 1; j < verticalWalls.get(verticalWalls.size() - 1).size(); ++j) {
            verticalWalls.get(verticalWalls.size() - 1).get(j).flag = true;
            verticalWalls.get(verticalWalls.size() - 1).get(j).x = cells.get(cells.size() - 1).get(0).x + Constants.R;
            verticalWalls.get(verticalWalls.size() - 1).get(j).y = y + (j - 1) * 2 * Constants.R;
            verticalWalls.get(verticalWalls.size() - 1).get(j).l = Constants.R * 2;
        }
    }

    public void randomGenerate(int x, int y, int height, int width) {
        //int randomNumber = random.nextInt(4);
        int randomNumber = 1;

        if (height <= 1 || width <= 1) {
            return;
        }
        cnt++;
        int number = random.nextInt(0, 4);

        int iy = random.nextInt(height - 1) + y + 1;
        int ix = random.nextInt(width - 1) + x + 1;

        //System.out.println(height-1);

        for (int j = x + 1; j < x + width + 1; ++j) {
            //verticalWalls.get(ix).get(j) = new StandardVerticalWall(cells.get(ix-1).get(j-1).x+Constants.R, cells.get(ix-1).get(j-1).y - Constants.R, 2*Constants.R);
            parallelWalls.get(iy).get(j).x = cells.get(j - 1).get(iy - 1).x - Constants.R;
            parallelWalls.get(iy).get(j).y = cells.get(j - 1).get(iy - 1).y + Constants.R;
            parallelWalls.get(iy).get(j).l = 2 * Constants.R;
            parallelWalls.get(iy).get(j).flag = true;
        }


        int j1;
        if (randomNumber != 0) {
            j1 = random.nextInt(ix - x) + x + 1;
            parallelWalls.get(iy).get(j1).flag = false;
        }

        if (randomNumber != 0) {
            j1 = random.nextInt(ix - x, width) + x + 1;
            parallelWalls.get(iy).get(j1).flag = false;
        }


        for (int j = y + 1; j < y + height + 1; ++j) {
            //verticalWalls.get(ix).get(j) = new StandardVerticalWall(cells.get(ix-1).get(j-1).x+Constants.R, cells.get(ix-1).get(j-1).y - Constants.R, 2*Constants.R);
            verticalWalls.get(ix).get(j).x = cells.get(ix - 1).get(j - 1).x + Constants.R;
            verticalWalls.get(ix).get(j).y = cells.get(ix - 1).get(j - 1).y - Constants.R;
            verticalWalls.get(ix).get(j).l = 2 * Constants.R;
            verticalWalls.get(ix).get(j).flag = true;
        }

        if (randomNumber != 0) {
            j1 = random.nextInt(iy - y) + y + 1;
            verticalWalls.get(ix).get(j1).flag = false;
        }

        if (randomNumber != 0) {
            j1 = random.nextInt(iy - y, height) + y + 1;
            verticalWalls.get(ix).get(j1).flag = false;
        }


        randomGenerate(x, y, iy - y, ix - x);
        randomGenerate(ix, y, iy - y, width - (ix - x));
        randomGenerate(x, iy, height - (iy - y), ix - x);
        randomGenerate(ix, iy, height - (iy - y), width - (ix - x));
    }

    public void paint(Graphics g, BufferedImage imagePesok, BufferedImage imageDiedPesok, BufferedImage imageParallelWall, BufferedImage imageVerticalWall) {

//        g.drawImage(image, (int)x, (int)y, width, height, null);


        if (this.rightBound >= cells.size() * Constants.R * 2) {
            for (ArrayList<BaseCell> partCells : cells) {
                for (BaseCell cell : partCells) {
                    cell.paint(g, imagePesok, imageDiedPesok, 1.0);
                }
            }
        } else {
            for (ArrayList<BaseCell> partCells : cells) {
                for (BaseCell cell : partCells) {
                    if (cell.x - Constants.R <= this.x + this.rightBound - Constants.R) {
                        cell.iAmDied = true;
                    }
                    if (cell.x - Constants.R <= this.x + this.rightBound - Constants.R && cell.x + Constants.R >= this.x + this.rightBound - Constants.R) {
                        cell.paint(g, imagePesok, imageDiedPesok, (this.x + this.rightBound - cell.x) / (2 * Constants.R));
                    } else {
                        cell.paint(g, imagePesok, imageDiedPesok, 1.0);
                    }
                }
            }
        }


        for (ArrayList<StandardVerticalWall> partVerticalWalls : verticalWalls) {
            for (StandardVerticalWall verticalWall : partVerticalWalls) {
                if (verticalWall.flag) {
                    if (verticalWall.x <= this.x + this.rightBound - Constants.R / 2) {
                        verticalWall.flag = false;
                    } else {
                        verticalWall.paint(g, imageVerticalWall);
                    }
                }
            }
        }

        for (ArrayList<StandardParallelWall> partParallelWalls : parallelWalls) {
            for (StandardParallelWall parallelWall : partParallelWalls) {
                if (parallelWall.flag) {
                    if (parallelWall.x + Constants.R * 2 < this.x + this.rightBound - Constants.R / 2) {
                        parallelWall.flag = false;
                    } else {
                        parallelWall.paint(g, imageParallelWall);
                    }
                }
            }
        }

//        g.setColor(new Color(5, 5, 5));
//        g.fillRect((int)this.x, (int)this.y, (int)this.rightBound, cells.get(0).size()*Constants.R*2);
//        g.setColor(Color.RED);
        if (this.rightBound < cells.size() * Constants.R * 2) {
            if (this.rightBound > 0.0) {
                rotate((System.currentTimeMillis() - Constants.TIME_START_PROGRAM) * Constants.V_POVOROT_PESOK);
                BufferedImage newImage = op.filter(image, null);
                for (int ind = 0; ind < cells.get(0).size(); ind++) {
                    g.drawImage(newImage, (int) (this.x + this.rightBound - 2 * Constants.R), (int) (cells.get(0).get(ind).y - Constants.R), null);

                }
            }
        }


    }

    public void go(double v) {
        x += v;
        for (ArrayList<BaseCell> partCells : cells) {
            for (BaseCell cell : partCells) {
                cell.x += v;
            }
        }

        for (ArrayList<StandardVerticalWall> partVerticalWalls : verticalWalls) {
            for (StandardVerticalWall verticalWall : partVerticalWalls) {
                verticalWall.x += v;
            }
        }

        for (ArrayList<StandardParallelWall> partParallelWalls : parallelWalls) {
            for (StandardParallelWall parallelWall : partParallelWalls) {
                parallelWall.x += v;
            }
        }
    }

    public boolean update(double v) {
        rightBound += v;
        if (rightBound >= width) {
            iAmDied = true;
        }
        return iAmDied;
    }

    public void rotate(double angleInRadians) {
        double locationX = image.getWidth() / 2;
        double locationY = image.getHeight() / 2;
        tx = AffineTransform.getRotateInstance(angleInRadians, locationX, locationY);
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
    }
}
