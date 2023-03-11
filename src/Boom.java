import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Boom {

    boolean flag = false;
    BufferedImage image;
    int i, j, indexSector;

    long timeBorn = 0;

    public Boom(Human userHuman) throws IOException {
        this.image = ImageIO.read(new File("data\\boom.gif"));
        this.i = userHuman.i;
        this.flag = true;
        this.j = userHuman.j;
        this.indexSector = userHuman.indexSector;
        this.timeBorn = System.currentTimeMillis();
    }

    public Boom(){}


    public void paint(Graphics g, Labirint labirint){
        BaseCell cell = labirint.getCell(indexSector, i, j);
        g.drawImage(image, (int)cell.x-Constants.R/2, (int)cell.y-Constants.R/2, Constants.R, Constants.R, null);
        g.setColor(new Color(255, 0, 0, 199));
        g.drawOval((int)cell.x - 6*Constants.R, (int)cell.y - 6*Constants.R, 12*Constants.R, 12*Constants.R);
        g.setColor(Color.RED);
    }

    public void buuxx(Labirint labirint){
        if (System.currentTimeMillis() - timeBorn >= Constants.TIME_DIED_BOOM){
            this.flag = false;
//            ArrayList<BaseCell> cells = new ArrayList<>();
//            for (int i1 = i-2; i1 <= i+2; i1++){
//                cells.add(labirint.getCell(indexSector, i1, j-1));
//                cells.add(labirint.getCell(indexSector, i1, j));
//                cells.add(labirint.getCell(indexSector, i1, j+1));
//            }
//            for (int i1 = i-1; i1 <= i+2; i1++){
//                cells.add(labirint.getCell(indexSector, i1, j-1));
//                cells.add(labirint.getCell(indexSector, i1, j-2));
//                cells.add(labirint.getCell(indexSector, i1, j+1));
//                cells.add(labirint.getCell(indexSector, i1, j+2));
//            }
//            for (BaseCell cell : cells){
//                if (cell != null){
//
//                }
//            }

            Sound buux = new Sound(new File("data\\music\\buxx.wav"));
            buux.setVolume((float)0.5);
            buux.play();

            BaseCell cell = labirint.getCell(indexSector, i, j);
            for (BaseSector sector : labirint.sectors){
                for (ArrayList<StandardParallelWall> walls : sector.parallelWalls){
                    for (StandardParallelWall wallp : walls){
                        Segment segment = new Segment(wallp.x +Constants.R, wallp.y, cell.x, cell.y);
                        if (segment.length() <= 6*Constants.R){
                            wallp.flag = false;
                        }

                    }
                }
                for (ArrayList<StandardVerticalWall> walls : sector.verticalWalls){
                    for (StandardVerticalWall wallv : walls){
                        Segment segment = new Segment(wallv.x, wallv.y + Constants.R, cell.x, cell.y);
                        if (segment.length() <= 6*Constants.R){
                            wallv.flag = false;
                        }

                    }
                }

                ArrayList<ArrayList<StandardParallelWall>> parallelWalls = sector.parallelWalls;
                for (int j = 1; j < parallelWalls.get(0).size(); ++j){
                    parallelWalls.get(0).get(j).flag = true;
                }

                for (int j = 1; j < parallelWalls.get(parallelWalls.size()-1).size(); ++j) {
                    parallelWalls.get(parallelWalls.size() - 1).get(j).flag = true;
                }
            }
        }
    }
}
