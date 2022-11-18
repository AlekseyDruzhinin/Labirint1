import java.awt.*;
import java.util.ArrayList;

public abstract class BaseSector {
    protected int x, y; // координаты верхнего левого угла
    protected int width;
    protected int height;

    ArrayList<ArrayList<BaseCell>> cells = new ArrayList<>();
    ArrayList<ArrayList<BaseWall>> verticalWalls = new ArrayList<>();
    ArrayList<ArrayList<BaseWall>> parallelWalls = new ArrayList<>();

    public BaseSector(int x, int y, MyFrame frame) {
        this.x = x;
        this.y = y;
        this.width = frame.getWidth();
        this.height = frame.getHeight();

        StandardCell cell = new StandardCell(0,0);
        for (int in = 0, i = 40; i + 2*cell.r < width; i+=2*cell.r, ++in){
            cells.add(new ArrayList<BaseCell>());
            for (int jn = 0, j = 40; j + 2*cell.r < height; j+=2*cell.r, ++jn){
                cells.get(in).add(new StandardCell(i, j));
            }
        }

        for (int in = 0, i = 40; in <= cells.size(); ++in, i+=2*cell.r){
            verticalWalls.add(new ArrayList<BaseWall>());
            for (int jn = 0, j = 40; jn <= cells.get(in).size(); ++jn, j+=2*cell.r){
                verticalWalls.get(in).add(new StandardVerticalWall(i, j, 2*cell.r));
            }
        }

        for (int in = 0, i = 40; in <= cells.size(); ++in, i+=2*cell.r){
            verticalWalls.add(new ArrayList<BaseWall>());
            for (int jn = 0, j = 40; jn <= cells.get(in).size(); ++jn, j+=2*cell.r){
                verticalWalls.get(in).add(new StandardVerticalWall(i, j, 2*cell.r));
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
    }
}
