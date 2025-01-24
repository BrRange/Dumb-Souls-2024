package entities.orbs;

import entities.enemies.Enemy;
import entities.runes.EXP_Rune;
import entities.runes.Life_Rune;
import entities.runes.Mana_Rune;
import entities.runes.MultiAttack_Rune;
import entities.runes.Speed_Rune;
import main.Game;
import entities.Player;
import java.awt.image.BufferedImage;

public class Rune_Orb extends Enemy {

	private String[] runes = { "Life", "Mana", "Speed", "Double Attack", "EXP" };
	private int indexRunes;

	public Rune_Orb(int x, int y, int width, int height) {
		super(x, y, width, height, null);

		maxIndex = 1;
		indexRunes = Game.rand.nextInt(runes.length - 1);

		switch (runes[indexRunes]) {
			case "Life":
				getAnimation(Life_Rune.sprite);
				break;
			case "Mana":
				getAnimation(Mana_Rune.sprite);
				break;
			case "Speed":
				getAnimation(Speed_Rune.sprite);
				break;
			case "Double Attack":
				getAnimation(MultiAttack_Rune.sprite);
				break;
			case "EXP":
				getAnimation(EXP_Rune.sprite);
				break;
		}
		speed = Game.player.maxSpeed * 1.1;
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

			switch (runes[indexRunes]) {
				case "Life":
					Player.runesInventory.add(new Life_Rune());
					break;
				case "Mana":
					Player.runesInventory.add(new Mana_Rune());
					break;
				case "Speed":
					Player.runesInventory.add(new Speed_Rune());
					break;
				case "Double Attack":
					Player.runesInventory.add(new MultiAttack_Rune());
					break;
				case "EXP":
					Player.runesInventory.add(new EXP_Rune());
					break;
			}
		}
	}
}
