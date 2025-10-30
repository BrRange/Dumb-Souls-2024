package entities.runes;

import main.Game;
import main.Menu_Level;

import java.awt.image.BufferedImage;

public class Choice_Rune extends Rune {

	public static BufferedImage sprite = Game.sheet.getSprite(0, 256, 16, 16);

	public Choice_Rune() {
		super("Electio Rune", "A new option is given when leveling up", 5, sprite);
	}

	public void setup() {
		Game.levelUpMenu = new Menu_Level(4);
	}

	public void tick(){}
}
