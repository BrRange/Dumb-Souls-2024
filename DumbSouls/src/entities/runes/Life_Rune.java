package entities.runes;

import main.Game;
import java.awt.image.BufferedImage;

public class Life_Rune extends Rune {

	public static BufferedImage sprite = Game.sheet.getSprite(0, 256, 16, 16);

	public Life_Rune() {
		super("Vitae Rune", "Provides increased vitality", 0, sprite);
	}

	public void setup() {
		Game.player.maxLife += 50;
		Game.player.lifeRec += 0.001;
	}

	public void tick(){}
}
