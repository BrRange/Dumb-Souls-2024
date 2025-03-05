package graphics.elements;
import java.awt.geom.Rectangle2D;

import main.Game;

import java.awt.Color;

public class TextBox {
    private Rectangle2D box;
    private int x, y;
    private boolean hovered = false;
    private Text text;
    
    public TextBox(String fontName, int fontStyle, int fontSize, String txt, int textX, int textY, Color color){
        text = new Text(fontName, fontStyle, fontSize, txt, color);
        x = textX;
        y = textY;
        Game.gameGraphics.setFont(text.font);
        box = Game.gameGraphics.getFontMetrics().getStringBounds(text.content, Game.gameGraphics);
        box.setRect(textX, textY - Game.gameGraphics.getFontMetrics().getAscent(), box.getWidth(), box.getHeight());
    }

    public void render(){
        text.render(x, y);
    }

    public void updateText(String txt){
        text.content = txt;
        box = Game.gameGraphics.getFontMetrics().getStringBounds(txt, Game.gameGraphics);
        box.setRect(x, y - Game.gameGraphics.getFontMetrics().getAscent(), box.getWidth(), box.getHeight());
    }

    public void updateColor(Color color){
        text.c = color;
    }

    public boolean hover(){
        int deltaX = Game.mx / Game.scale, deltaY = Game.my / Game.scale;
        boolean temp = box.contains(deltaX, deltaY) ^ hovered;
        hovered = box.contains(deltaX, deltaY);
        return temp;
    }

    public boolean click(){
        return Game.clickController.contains(1) && hovered;
    }

    public boolean isColliding(int targetX, int targetY){
        return box.contains(targetX, targetY);
    }

    public int getWidth(){
        return (int)box.getWidth();
    }

    public int getHeight(){
        return (int)box.getHeight();
    }

    public String getText(){
        return text.content;
    }
}
