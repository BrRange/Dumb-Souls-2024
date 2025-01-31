package entities.runes;

import java.awt.image.BufferedImage;
import main.Game;

public class MultiAttack_Rune extends Rune{
	private boolean effectsApplied = false;
	public static BufferedImage sprite = Game.sheet.getSprite(48, 256, 16, 16);
	
	public MultiAttack_Rune() {
		super("Vulneris Rune", "Boosts attack power", 3, sprite);
	}
	
	public void tick() {
		if(!effectsApplied){
			Game.player.damage *= 2;
			//Game.player.playerWeapon.
			effectsApplied = true;
		}
	}
}
