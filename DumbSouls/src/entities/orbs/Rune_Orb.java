package entities.orbs;

import entities.enemies.Enemy;
import entities.runes.*;
import main.Game;
import entities.Player;
import java.awt.image.BufferedImage;

public class Rune_Orb extends Enemy {

	private int indexRunes;

	public Rune_Orb(int x, int y) {
		super(x, y, 16, 16, null);

		maxIndex = 1;
		indexRunes = Game.rand.nextInt(Rune.runesInGame);

		switch (indexRunes) {
			case 0:
				getAnimation(Life_Rune.sprite);
				break;
			case 1:
				getAnimation(Mana_Rune.sprite);
				break;
			case 2:
				getAnimation(Speed_Rune.sprite);
				break;
			case 3:
				getAnimation(MultiAttack_Rune.sprite);
				break;
			case 4:
				getAnimation(EXP_Rune.sprite);
				break;
		}
		speed = Game.player.maxSpeed * 1.2;
		setMask(0, 0, 16, 16);
		depth = 1;
	}

	protected void getAnimation(BufferedImage spr) {
		animation = new BufferedImage[1];
		animation[0] = spr;
	}

	public void tick() {
		movement();

		if (isColiding(Game.player)) {
			Game.enemies.remove(this);
			Player.runesInventory.get(indexRunes).collected = true;
		}
	}
}