package entities.enemies;

import entities.Player;
import entities.orbs.EXP_Orb;
import graphics.Spritesheet;
import main.Game;
import world.World;

public class Enemy_Barrier extends Enemy {
	public static Spritesheet sheet = new Spritesheet("res/spritesheets/Enemies/Enemy_Barrier.png");

	public Enemy_Barrier(int x, int y) {
		super(x, y, 48, 32, sheet.getSprite(0, 0, 48, 32));
		if (specialRare) {
			specialMult = 2;
			hue = 0xFFFFFF;
		}
		getAnimation(0, 0, 48, 32, 2, sheet);
		expValue = 120 * specialMult;
		soulValue = 12 * specialMult;
		maxLife = 250 * specialMult + (int) (2.5 * World.wave);
		life = maxLife;
		damage = 82 * specialMult + 0.82 * World.wave;
		maxSpeed = 0.6 + (specialMult - 1) / 3;
		speed = maxSpeed;
		setMask(1, 1, 46, 30);
		timeSpawn = 180;
		maxIndex = 2;
		maxFrames = 20;
		weight = 0;
	}

	private void die() {
		Game.enemies.remove(this);
		Game.enemies.add(new Enemy_Debri(centerX(), centerY() - 16, specialRare));
		Game.enemies.add(new Enemy_Debri(centerX() - 16, centerY() + 16, specialRare));
		Game.enemies.add(new Enemy_Debri(centerX() + 16, centerY() + 16, specialRare));
		Game.entities.add(new EXP_Orb(centerX(), centerY(), expValue, hue));
		Player.souls += soulValue;
	}

	public void tick() {
		damagedAnimation();
		animate();
		if (!spawning) {
			if (!isColiding(Game.player)) {
				movement();
			} else {
				giveCollisionDamage(Game.player, 30);
			}

			slownessEffect(0.92);

			shotDamage();
			if (life <= 0) {
				die();
			}

		} else {
			spawnAnimation(timeSpawn / 3);
		}
	}
}
