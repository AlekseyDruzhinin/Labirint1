import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public abstract class BaseSector {
    protected int x, y; // координаты верхнего левого угла
    protected int width;
    protected int height;

    ArrayList<ArrayList<BaseCell>> cells = new ArrayList<>();
    ArrayList<ArrayList<StandardVerticalWall>> verticalWalls = new ArrayList<>();
    ArrayList<ArrayList<StandardParallelWall>> parallelWalls = new ArrayList<>();

    public BaseSector(int x, int y, MyFrame frame) {
        this.x = x;
        this.y = y;
        this.width = frame.getWidth();
        this.height = frame.getHeight();

        Random random = new Random();

        StandardCell cell = new StandardCell(0,0);
        for (int in = 0, i = 40; i + 2*cell.r < Constants.LEFT_END; i+=2*cell.r, ++in){
            cells.add(new ArrayList<BaseCell>());
            for (int jn = 0, j = 40; j + 2*cell.r < Constants.DOWN_END; j+=2*cell.r, ++jn){
                cells.get(in).add(new StandardCell(i, j));
            }
        }

        for (int in = 0, i = 40; in <= cells.size(); ++in, i+=2*cell.r){
            verticalWalls.add(new ArrayList<StandardVerticalWall>());
            for (int jn = 0, j = 40; jn < cells.get(0).size(); ++jn, j+=2*cell.r){
                if (random.nextInt(4) == 1) {
                    verticalWalls.get(in).add(new StandardVerticalWall(i, j, 2*cell.r));
                }else{
                    verticalWalls.get(in).add(null);
                }
            }
        }

        for (int in = 0, i = 40; in < cells.size(); ++in, i+=2*cell.r){
            parallelWalls.add(new ArrayList<StandardParallelWall>());
            for (int jn = 0, j = 40; jn <= cells.get(0).size(); ++jn, j+=2*cell.r){
                if (random.nextInt(4) == 1) {
                    parallelWalls.get(in).add(new StandardParallelWall(i, j, 2*cell.r));
                }else{
                    parallelWalls.get(in).add(null);
                }
            }
        }
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
                if (verticalWall != null){
                    verticalWall.paint(g);
                }
            }
        }

        for (ArrayList<StandardParallelWall> partParallelWalls : parallelWalls){
            for (StandardParallelWall parallelWall : partParallelWalls){
                if (parallelWall != null){
                    parallelWall.paint(g);
                }
            }
        }
    }
}
