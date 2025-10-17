package graphics.elements;

import java.awt.Color;
import java.awt.Font;

import main.Game;

public class Text {
    public String content;
    public Font font;
    public Color c;
    public Text(String fontName, int fontStyle, int fontSize, String text, Color color){
        font = new Font(fontName, fontStyle, fontSize);
        content = text;
        c = color;
    }

    public void render(int x, int y){
        Game.gameGraphics.setColor(c);
        Game.gameGraphics.setFont(font);
        Game.gameGraphics.drawString(content, x, y);
    }
}
