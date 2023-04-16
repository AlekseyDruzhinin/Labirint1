import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Boom {

    boolean flag = false;
    BufferedImage image;
    BufferedImage imageBoom;
    int i, j, indexSector;

    long timeBorn = 0;
    boolean flagDied = false;
    long timeDied;
    BaseCell cellDied;

    public Boom(Human userHuman) throws IOException {
        this.image = ImageIO.read(new File("data\\boom.gif"));
        this.imageBoom = ImageIO.read(new File("data\\bum.png"));
        this.i = userHuman.i;
        this.flag = true;
        this.j = userHuman.j;
        this.indexSector = userHuman.indexSector;
        this.timeBorn = System.currentTimeMillis();
    }

    public Boom(){}


    public void paint(Graphics g, Labirint labirint, MyFrame frame){
        BaseCell cell = labirint.getCell(indexSector, i, j);
        g.drawImage(image, (int)cell.x-Constants.R/2, (int)cell.y-Constants.R/2, Constants.R, Constants.R, null);
        g.setColor(new Color(255, 0, 0, 199));
        g.drawOval((int)cell.x - 6*Constants.R, (int)cell.y - 6*Constants.R, 12*Constants.R, 12*Constants.R);
        g.setColor(Color.RED);
    }

    public void paintDied(Graphics g, Labirint labirint){
        if (flagDied){
            g.setColor(Color.RED);
//            g.fillOval((int)(cellDied.x - 6*Constants.R), (int)(cellDied.y - 6*Constants.R), 12*Constants.R, 12*Constants.R);
            g.drawImage(imageBoom, (int)(cellDied.x - 6*Constants.R), (int)(cellDied.y - 6*Constants.R), 12*Constants.R, 12*Constants.R, null);
            if (System.currentTimeMillis() - timeDied >= 1000){
                flagDied = false;
            }
        }
    }

    public void buuxx(Labirint labirint, Graphics g, Human userHuman){
        if (this.flag && System.currentTimeMillis() - timeBorn >= Constants.TIME_DIED_BOOM){
            this.flag = false;
            BaseCell cell = labirint.getCell(indexSector, i, j);
            cellDied = cell;
            flagDied = true;

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

            if (Constants.MUST_PLAY_SOUND){
                new Thread(() -> {
                    Sound sound = new Sound(SoundFiles.buxx);
                    sound.setVolume((float) 0.75);
                    sound.play();
                    sound.join();
                }).start();
            }

            Constants.BOOM_IS_READY = false;

            if (Math.sqrt((userHuman.x-cell.x)*(userHuman.x-cell.x) + (userHuman.y-cell.y)*(userHuman.y-cell.y))< 6*Constants.R){
                Constants.USER_DIED = true;
            }

            for (BaseBot bot : labirint.bots){
                if (Math.sqrt((bot.x-cell.x)*(bot.x-cell.x) + (bot.y-cell.y)*(bot.y-cell.y))< 6*Constants.R){
                    bot.hp = 0.0;
                }
            }
            cell = labirint.getCell(indexSector, i, j);
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
            timeDied = System.currentTimeMillis();
            Constants.TIME_LAST_BUM = timeDied;
        }
    }
}
