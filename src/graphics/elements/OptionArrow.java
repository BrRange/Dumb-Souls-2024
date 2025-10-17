package graphics.elements;

import java.awt.Color;

import entities.types.Vector;

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
        origin = new Vector(x, y);
        target = new Vector(x, y);
        pos = new Vector(x, y);
        text = new Text(fontName, fontStyle, fontSize, txt, color);
        fx = func;
        this.frames = frames;
    }
    public void tick(){
        if(time < 1.0){
            double t = fx.midPoint(time);
            pos = Vector.interpolate(origin, target, t);
            time += 1.0 / frames;
        }
        else pos = target;
    }
    public void setTarget(double x, double y){
        if(x == target.x && y == target.y) return;
        time = 0.0;
        origin.set(pos.x, pos.y);
        target.set(x, y);
    }
    public void render(){
        text.render(pos.getX(), pos.getY());
    }
}
