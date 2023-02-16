public class Line {
    double a, b, c;

    public Line(Segment segment) {
        if (segment.getX1() == segment.getX2()){
            this.a = 0;
            this.b = (segment.getX2()-segment.getX1())/(segment.getY1() - segment.getY2());
            this.c = -b* segment.getY1();
        }else if (segment.getY1() == segment.getY2()){
            this.b = 0;
            this.a = (segment.getY2()-segment.getY1())/(segment.getX1() - segment.getX2());
            this.c = -a*segment.getX1();
        }else{
            this.a = 1.0;
            this.b = (segment.getX2()-segment.getX1())/(segment.getY1() - segment.getY2());
            this.c = -a*segment.getX1()-b* segment.getY1();
        }
    }

    public MyPoint getIntersection (Line line){
        MyPoint ans = new MyPoint(0, 0);
        ans.x = (line.c*b - c*line.b)/(a*line.b - line.a*b);
        ans.y = (line.c*a - c*line.a)/(b*line.a - line.b*a);
        return ans;
    }
}
