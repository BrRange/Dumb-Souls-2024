package graphics.elements;

import entities.Entity.Vector;
import java.awt.Color;

public class OptionArrow {
    public static interface Curve{
        double midPoint(double x);
    }
    private double time = 1.0;
    private int frames;
    private Text text;
    private Vector pos, origin, target;
    Curve fx;
    public OptionArrow(String fontName, int fontStyle, int fontSize, String txt, Color color, double x, double y, Curve func, int frames){
        origin = target = pos = new Vector(x, y);
        text = new Text(fontName, fontStyle, fontSize, txt, color);
        fx = func;
        this.frames = frames;
    }
    public void tick(){
        if(time < 1.0){
            double t = fx.midPoint(time);
            pos = Vector.offset(origin.scale(1 - t), target.scale(t));
            time += 1.0 / frames;
            if(time == 1.0) pos = target;
        }
    }
    public void setTarget(int x, int y){
        if(x == target.getX() && y == target.getY()) return;
        origin = pos;
        time = 0.0;
        target = new Vector(x, y);
    }
    public void render(){
        text.render(pos.getX(), pos.getY());
    }
}
