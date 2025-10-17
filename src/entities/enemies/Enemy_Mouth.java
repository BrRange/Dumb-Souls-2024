package entities.enemies;

import entities.Player;
import entities.orbs.EXP_Orb;
import entities.types.Collider.ColliderSquare;
import graphics.Spritesheet;
import main.Game;
import world.World;

public class Enemy_Mouth extends Enemy {
	public static Spritesheet sheet = new Spritesheet("res/spritesheets/Enemies/Enemy_Mouth.png");

	public Enemy_Mouth(int x, int y) {
		super(x, y, 16, 16, sheet.getSprite(0, 0, 16, 16));
		if (specialRare) {
			specialMult = 2;
			hue = 0xFFFFFF;
		}
		getAnimation(0, 0, 16, 16, 3, sheet);
		expValue = 20 * specialMult;
		soulValue = 2 * specialMult;
		maxLife = 12 * specialMult + (int) (0.12 * World.wave);
		life = maxLife;
		damage = 26 * specialMult + 0.26 * World.wave;
		maxSpeed = 1.5 + (specialMult - 1) / 3;
		speed = maxSpeed;
		timeSpawn = 240;
		hitbox = new ColliderSquare(pos, 1, 3, 14, 13);
		weight = 0.6f;
	}

	private void die() {
		Game.enemies.remove(this);
		Game.entities.add(new EXP_Orb(centerX(), centerY(), expValue, hue));
		Player.souls += soulValue;
	}

	public void tick() {
		damagedAnimation();
		animate();
		if (!spawning) {
			movement();

			if (isColiding(Game.player)) {
				this.giveCollisionDamage(Game.player, 30);
			}

			slownessEffect(0.85);

			shotDamage();
			if (life <= 0) {
				die();
			}
		} else {
			spawnAnimation(timeSpawn / 3);
		}
	}
}
