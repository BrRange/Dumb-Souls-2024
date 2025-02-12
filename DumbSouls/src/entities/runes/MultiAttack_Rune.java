package entities.runes;

import java.awt.image.BufferedImage;
import main.Game;

public class MultiAttack_Rune extends Rune{
	public static BufferedImage sprite = Game.sheet.getSprite(48, 256, 16, 16);
	
	public MultiAttack_Rune() {
		super("Vulneris Rune", "Boosts attack power", 3, sprite);
	}
	
	public void setup(){
		Game.player.playerWeapon.shotDamage *= 2;
		Game.player.playerWeapon.attackTimer = Game.player.playerWeapon.attackTimer / 2 + 1;
	}

	public void tick() {}
}
