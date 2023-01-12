import java.awt.*;

public class Segment {
    double startX, startY, endX, endY;

    public Segment(double startX, double startY, double endX, double endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public Segment(StandardVerticalWall wall) {
        this.startX = wall.x;
        this.startY = wall.y;
        this.endX = wall.x;
        this.endY = wall.y + wall.l;
    }

    public Segment(StandardParallelWall wall) {
        this.startX = wall.x;
        this.startY = wall.y;
        this.endX = wall.x + wall.l;
        this.endY = wall.y;
    }

    public MyPoint pointLineIntersection(Segment segment){
        double thisA = this.startY - this.endY;
        double thisB = this.endX - this.startX;
        double thisC = -thisA * this.startX - thisB * this.startY;

        double sA = segment.startY - segment.endY;
        double sB = segment.endX - segment.startX;
        double sC = -sA * segment.startX - sB * segment.startY;

        MyPoint ans = new MyPoint((sC * thisB - thisC * sB)/(thisA * sB - sA * thisB), (sC * thisA - thisC * sA)/(sA * thisB - thisA * sB));

        return ans;
    }
}
