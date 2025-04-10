package entities.enemies;

import entities.Player;
import entities.orbs.EXP_Orb;
import entities.types.Collider.ColliderSquare;
import graphics.Spritesheet;
import main.Game;
import world.World;

public class Enemy_Stain extends Enemy {
	public static Spritesheet sheet = new Spritesheet("res/spritesheets/Enemies/Enemy_Stain.png");

	public Enemy_Stain(int x, int y) {
		super(x, y, 16, 16, sheet.getSprite(0, 0, 16, 16));
		if (specialRare) {
			specialMult = 3;
			hue = 0xFFFFFF;
		}
		getAnimation(0, 0, 16, 16, 3, sheet);
		expValue = 10 * specialMult;
		soulValue = 1 * specialMult;
		maxLife = 10 * specialMult + (int) (0.1 * World.wave);
		life = maxLife;
		damage = 8 * specialMult + 0.08 * World.wave;
		maxSpeed = 1 + (specialMult - 1) / 3;
		speed = maxSpeed;
		hitbox = new ColliderSquare(pos, 2, 2, 12, 13);
		timeSpawn = 150;
	}

	private void die() {
		Game.enemies.remove(this);
		Game.entities.add(new EXP_Orb(centerX(), centerY(), expValue, hue));
		Player.souls += soulValue;
	}

	public void tick() {
		animate();
		damagedAnimation();
		if (spawning) {
			spawnAnimation(timeSpawn / 3);
			return;
		} 
		if (!isColiding(Game.player)) {
			movement();
		} else {
			this.giveCollisionDamage(Game.player, 15);
		}

		slownessEffect(0.99);

		shotDamage();

		if (life <= 0) {
			die();
		}
	}
}
