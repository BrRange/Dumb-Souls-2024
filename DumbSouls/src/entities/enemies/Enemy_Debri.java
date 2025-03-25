package entities.enemies;

import entities.types.Collider.ColliderCircle;
import graphics.Spritesheet;
import main.Game;
import world.World;

public class Enemy_Debri extends Enemy {
	public static Spritesheet sheet = new Spritesheet("res/spritesheets/Enemies/Enemy_Debri.png");

	public Enemy_Debri(int x, int y, boolean specialRare) {
		super(x, y, 16, 16, Game.sheet.getSprite(0, 0, 16, 16));
		this.specialRare = specialRare;
		if (specialRare) {
			specialMult = 3;
			hue = 0xFFFFFF;
		}
		getAnimation(0, 0, 16, 16, 3, sheet);
		maxLife = 8 * specialMult + (int) (0.08 * World.wave);
		life = maxLife;
		expValue = 45 * specialMult;
		damage = 6 * specialMult + 0.06 * World.wave;
		maxSpeed = 0.2 + (specialMult - 1) / 3;
		speed = maxSpeed;
		this.invulnerable = false;
		hitbox = new ColliderCircle(pos, 7);
		maxFrames = 6;
		weight = 3;
	}

	public void die() {
		Game.enemies.remove(this);
		Game.player.exp += expValue;
	}

	public void tick() {
		damagedAnimation();
		animate();

		if (calculateDistance(centerX(), centerY(), Game.player.centerX(), Game.player.centerY()) < 48) {
			maxSpeed = 1.5;
		}

		if (!isColiding(Game.player)) {
			movement();
		}

		else {
			giveCollisionDamage(Game.player, 15);
		}
		slownessEffect(0.95);

		shotDamage();
		if (life <= 0) {
			die();
		}
	}
}