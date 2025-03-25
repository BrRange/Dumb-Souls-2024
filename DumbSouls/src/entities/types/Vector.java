package entities.types;

public class Vector {
    public double x, y;
    
    public Vector(double setx, double sety){
        x = setx;
        y = sety;
    }
    
    public static double getMagnitude(double dx, double dy){
        final double mag = Math.hypot(dx, dy);
        return mag == 0 ? 1 : mag;
    }
    
    public static Vector interpolate(Vector a, Vector b, double s){
        double dx = a.x == b.x ? a.x : a.x * (1 - s) + b.x * s;
        double dy = a.y == b.y ? a.y : a.y * (1 - s) + b.y * s;
        return new Vector(dx, dy);
    }
    
    public Vector normal(){
        final double mag = getMagnitude(x, y);
        return new Vector(x / mag, y / mag);
    }
    
    public Vector normalize(){
        final double mag = getMagnitude(x, y);
        x /= mag;
        y /= mag;
        return this;
    }
    
    public int getX(){
        return (int) x;
    }
    
    public int getY(){
        return (int) y;
    }
    
    public void set(double setx, double sety){
        x = setx;
        y = sety;
    }
    
    public void move(double dx, double dy){
        x += dx;
        y += dy;
    }

    public Vector offset(double ox, double oy){
        return new Vector(x + ox, y + oy);
    }
    
    public Vector scale(double s){
        return new Vector(x * s, y * s);
    }

    public static double squareDist(Vector a, Vector b){
        double deltaX = a.x - b.x;
        double deltaY = a.y - b.y;
        return deltaX * deltaX + deltaY * deltaY;
    }
}
