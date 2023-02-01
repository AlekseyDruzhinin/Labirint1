public class Vector {
    double x, y;
    double length;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
        this.length = Math.sqrt(x * x + y * y);
    }

   public void setLength(double lengthNew){
        x = x / length * lengthNew;
        y = y / length * lengthNew;
   }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getLength() {
        return length;
    }

    public double scalarComposition(Vector vector) {
        return this.x * vector.x + this.y * vector.y;
    }

    public double vectorComposition(Vector vector) {
        return this.x * vector.y - this.y * vector.x;
    }

    public void rotate(double agreeInRadians){
        this.x = Math.cos(agreeInRadians) - Math.sin(agreeInRadians);
        this.y = Math.sin(agreeInRadians) + Math.cos(agreeInRadians);
    }

    public Vector normVector(double size){
        return new Vector(x/length*size, y/length*size);
    }
}
