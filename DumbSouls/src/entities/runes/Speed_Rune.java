package entities.runes;

import java.awt.image.BufferedImage;

import main.Game;

public class Speed_Rune extends Rune {
	
	public static BufferedImage sprite = Game.sheet.getSprite(32, 256, 16, 16);
	
	public Speed_Rune() {
		super("Celeritatis Rune", "Boosts movement speed", 2, sprite);
	}
	
	public void tick() {
		Game.player.speedBoost *= 1.5;
	}
}
