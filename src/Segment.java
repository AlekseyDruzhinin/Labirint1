public class Segment {
    private double x1, y1;
    private double x2, y2;

    public Segment(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public Segment(StandardParallelWall wall){
        this.x1 = wall.x;
        this.y1 = wall.y;
        this.x2 = wall.x + wall.l;
        this.y2 = wall.y;
    }

    public Segment(StandardVerticalWall wall){
        this.x1 = wall.x;
        this.y1 = wall.y;
        this.x2 = wall.x;
        this.y2 = wall.y + wall.l;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public void setY2(double y2) {
        this.y2 = y2;
    }

    public double getX1() {
        return x1;
    }

    public double getY1() {
        return y1;
    }

    public double getX2() {
        return x2;
    }

    public double getY2() {
        return y2;
    }

    public boolean isIntersection(Segment segment1){
        //boolean ans = true;
        Vector vectorThis = new Vector(this);
        if ((vectorThis.vectorComposition(new Vector(getX1(), getY1(), segment1.getX1(), segment1.getY1())) > 0) == (vectorThis.vectorComposition(new Vector(getX1(), getY1(), segment1.getX2(), segment1.getY2())) > 0)){
            return false;
        }
        vectorThis = new Vector(segment1);
        if ((vectorThis.vectorComposition(new Vector(segment1.getX1(), segment1.getY1(), this.getX1(), this.getY1())) > 0) == (vectorThis.vectorComposition(new Vector(segment1.getX1(), segment1.getY1(), this.getX2(), this.getY2())) > 0)){
            return false;
        }
        return true;
    }

    public MyPoint getIntersection(Segment segment1){
        Line line1 = new Line(this);
        Line line2 = new Line(segment1);
        return line1.getIntersection(line2);
    }
}
