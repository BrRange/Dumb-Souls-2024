package entities.runes;

import java.awt.image.BufferedImage;

import main.Game;

public class Mana_Rune extends Rune {

	public static BufferedImage sprite = Game.sheet.getSprite(16, 256, 16, 16);

	public Mana_Rune() {
		super("Magae Rune", "Boosts mana efficiency", 1, sprite);
		super("Magae Rune", "Boosts mana efficiency", 1, sprite);
	}

	public void setup() {
		Game.player.maxMana += 50;
		Game.player.manaRec += 2;
	}

	public void tick() {}

}
