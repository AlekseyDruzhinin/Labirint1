import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public abstract class BaseSector {
    protected int x, y; // координаты верхнего левого угла
    protected int width;
    protected int height;
    Random random = new Random();
    ArrayList<ArrayList<BaseCell>> cells = new ArrayList<>();
    ArrayList<ArrayList<StandardVerticalWall>> verticalWalls = new ArrayList<>();
    ArrayList<ArrayList<StandardParallelWall>> parallelWalls = new ArrayList<>();

    public BaseSector(int x, int y, MyFrame frame) {
        this.x = x;
        this.y = y;
        this.width = frame.getWidth();
        this.height = frame.getHeight();

        Random random = new Random(654654);

        StandardCell cell = new StandardCell(0,0);
        for (int in = 0, i = 40; i + 2*cell.r < frame.getWidth(); i+=2*cell.r, ++in){
            cells.add(new ArrayList<BaseCell>());
            for (int jn = 0, j = 40; j + 2*cell.r < frame.getHeight(); j+=2*cell.r, ++jn){
                cells.get(in).add(new StandardCell(i, j));
            }
        }

        int size_wight = cells.size();
        int size_height = cells.get(0).size();

        for (int i = 0; i <= size_wight; ++i){
            verticalWalls.add(new ArrayList<StandardVerticalWall>());
            for (int j = 0; j <= size_height; ++j){
                verticalWalls.get(i).add(new StandardVerticalWall(0, 0, 0, false));
            }
        }

        for (int i = 0; i <= size_height; ++i){
            parallelWalls.add(new ArrayList<StandardParallelWall>());
            for (int j = 0; j <= size_wight; ++j){
                parallelWalls.get(i).add(new StandardParallelWall(0, 0, 0, false));
            }
        }



    }

    public void randomGenerate(int x, int y, int height, int width, Graphics g) throws InterruptedException {
        if (height <= 1 || width <= 1){
            return;
        }

        int number = random.nextInt(0, 4);

        int iy = random.nextInt(height-1)+y+1;
        int ix = random.nextInt(width-1)+x+1;

        for (int j = x+1; j < x+width+1; ++j){
            //verticalWalls.get(ix).get(j) = new StandardVerticalWall(cells.get(ix-1).get(j-1).x+Constants.R, cells.get(ix-1).get(j-1).y - Constants.R, 2*Constants.R);
            parallelWalls.get(iy).get(j).x = cells.get(j-1).get(iy-1).x - Constants.R;
            parallelWalls.get(iy).get(j).y = cells.get(j-1).get(iy-1).y - Constants.R;
            parallelWalls.get(iy).get(j).l = 2*Constants.R;
            parallelWalls.get(iy).get(j).flag = true;
        }


        int j2;
        if (ix-x-1<=1){
            j2 = x;
        }else {
            j2 = random.nextInt(1,ix-x)+x;
        }
        if (number!=0){
            parallelWalls.get(iy).get(j2).flag = false;
        }
        number--;
        if (width-1-ix+x <= 1){
            j2 = ix -x;
        }else{
            j2 = random.nextInt(ix-x,width-1)+x;
        }
        if (number!=0){
            parallelWalls.get(iy).get(j2).flag = false;
        }
        number--;



        /*if (ix >= verticalWalls.size()){
            return;
        }*/

        for (int j = y+1; j < y+height+1; ++j){
            //verticalWalls.get(ix).get(j) = new StandardVerticalWall(cells.get(ix-1).get(j-1).x+Constants.R, cells.get(ix-1).get(j-1).y - Constants.R, 2*Constants.R);
            verticalWalls.get(ix).get(j).x = cells.get(ix-1).get(j-1).x+Constants.R;
            verticalWalls.get(ix).get(j).y = cells.get(ix-1).get(j-1).y - Constants.R;
            verticalWalls.get(ix).get(j).l = 2*Constants.R;
            verticalWalls.get(ix).get(j).flag = true;
        }

        int j1;
        if (iy-y-1<=1){
            j1 = y;
        }else {
            j1 = random.nextInt(1,iy-y)+y;
        }
        if (number!=0){
            verticalWalls.get(ix).get(j1).flag = false;
        }
        number--;
        if (height-1-iy+y <= 1){
            j1 = iy -y;
        }else{
            j1 = random.nextInt(iy-y,height-1)+y;
        }
        if (number!=0){
            verticalWalls.get(ix).get(j1).flag = false;
        }
        //number--;

        /*if (iy >= parallelWalls.size()){
            return;
        }*/

        paint(g);
        Thread.sleep(3000);

        randomGenerate(x, y, iy - y, ix - x, g );
        randomGenerate(ix, y, iy - y,width-(ix-x), g);
        randomGenerate(x, iy, height-(iy-y),ix - x, g);
        randomGenerate(ix, iy, height-(iy-y), width-(ix-x), g);
    }

    // не учитывая положения сектора на плоскости
    public void paint(Graphics g){
        for (ArrayList<BaseCell> partCells : cells){
            for (BaseCell cell : partCells){
                cell.paint(g);
            }
        }

        for (ArrayList<StandardVerticalWall> partVerticalWalls : verticalWalls){
            for (StandardVerticalWall verticalWall : partVerticalWalls){
                if (verticalWall.flag){
                    verticalWall.paint(g);
                }
            }
        }

        for (ArrayList<StandardParallelWall> partParallelWalls : parallelWalls){
            for (StandardParallelWall parallelWall : partParallelWalls){
                if (parallelWall.flag){
                    parallelWall.paint(g);
                }
            }
        }
    }
}
