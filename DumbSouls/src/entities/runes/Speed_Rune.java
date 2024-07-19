package entities.runes;

import java.awt.image.BufferedImage;

import main.Game;

public class Speed_Rune extends Rune {
	
	private boolean effectsAplied;
	public static BufferedImage sprite = Game.sheet.getSprite(32, 256, 16, 16);
	
	public Speed_Rune() {
		super(sprite);
		this.name = "Rune of Speed";
		index = 3;
		this.description = "Speed +0.5";
	}
	
	public void tick() {
		if (!effectsAplied) {
			Game.player.maxSpeed += 0.5;
			Game.player.speed = Game.player.maxSpeed;
			effectsAplied = true;
		}
	}
}
