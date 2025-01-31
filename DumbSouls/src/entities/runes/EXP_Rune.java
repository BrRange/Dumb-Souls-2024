package entities.runes;

import java.awt.image.BufferedImage;

import main.Game;

public class EXP_Rune extends Rune {
	private int timer;
	public static BufferedImage sprite = Game.sheet.getSprite(64, 256, 16, 16);
	
	public EXP_Rune() {
		super("Cognitionis Rune", "Grants a constant flow of experience", 4, sprite);
		super.sprite = sprite;
	}
	
	public void tick() {
		timer++;
		if(timer == 120) {
			timer = 0;
			Game.player.exp += 10 + (Game.player.level / 2);
		}
	}
}
