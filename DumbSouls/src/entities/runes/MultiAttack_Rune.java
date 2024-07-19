package entities.runes;

import java.awt.image.BufferedImage;
import main.Game;

public class MultiAttack_Rune extends Rune{
	public static BufferedImage sprite = Game.sheet.getSprite(48, 256, 16, 16);
	
	public MultiAttack_Rune() {
		super(sprite);
		this.name = "Double Attack Rune";
		index = 5;
		this.description = "Adds 1 aditional normal attack.";
	}
	
	public void tick() {
		if (Game.player.playerWeapon.attackTimer == 0) {
			Game.player.playerWeapon.AttackRandom();
		}
	}
}
