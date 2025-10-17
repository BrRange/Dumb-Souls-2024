package entities.enemies;

import entities.Player;
import entities.orbs.EXP_Orb;
import entities.shots.Shot_MortarShell;
import entities.types.Vector;
import entities.types.Collider.ColliderCircle;
import entities.types.Collider.ColliderSquare;
import graphics.Spritesheet;
import main.Game;
import world.World;

public class Enemy_Mortar extends Enemy {
	private float range;
	private double distPlayer;

	public static Spritesheet sheet = new Spritesheet("res/spritesheets/Enemies/Enemy_Mortar.png");

	public Enemy_Mortar(int x, int y) {
		super(x, y, 32, 32, null);
		if (specialRare) {
			specialMult = 3;
			hue = 0xFFFFFF;
		}
		getAnimation(0, 0, 32, 32, 2, sheet);
		expValue = 35 * specialMult;
		soulValue = 7 * specialMult;
		maxLife = 50 * specialMult + (int) (0.2 * World.wave);
		life = maxLife;
		damage = 40 * specialMult + 0.4 * World.wave;
		range += 160f + 0.8 * World.wave;
		maxSpeed = 0.3 + (specialMult - 1) / 3;
		speed = maxSpeed;
		hitbox = new ColliderSquare(pos, 2, 5, 24, 27);
		timeSpawn = 210;
		maxIndex = 2;
		maxFrames = 30;
	}

	Enemy_Target target = null;

	private class Enemy_Target extends Enemy {
		Enemy_Mortar owner;

		Enemy_Target(Enemy_Mortar owner, int originX, int originY) {
			super(originX, originY, 16, 16, Game.sheet.getSprite(16, 160, 16, 16));
			getAnimation(16, 160, 16, 16, 1);
			life = 200;
			this.owner = owner;
			speed = owner.maxSpeed * 10;
			hitbox = new ColliderCircle(pos, 0);
			weight = 4;
		}

		public void tick() {
			if (calculateDistance(centerX(), centerY(), Game.player.centerX(), Game.player.centerY()) > 2)
				movement();
			else die(true);
			if (life <= 0)
				die(true);
			life -= 1;
		}

		private void die(boolean activeOwner) {
			if (activeOwner)
				owner.shoot(centerX(), centerY());
			Game.entities.remove(this);
			owner.target = null;
		}
	}

	private void die() {
		if (target != null)
			target.die(false);
		Game.entities.add(new EXP_Orb(centerX(), centerY(), expValue, hue));
		Player.souls += soulValue;
		Game.enemies.remove(this);
	}

	private void attack() {
		if (attackTimer < 300)
			return;
		if (target == null) {
			target = new Enemy_Target(this, centerX(), centerY());
			Game.entities.add(target);
		}
		attackTimer = 0;
	}

	private void shoot(double dx, double dy) {
		dx -= centerX();
		dy -= centerY();
		double mag = Vector.getMagnitude(dx, dy);
		Game.eShots.add(new Shot_MortarShell(centerX(), centerY(), dx / mag, dy / mag, mag / 90, damage));
	}

	public void tick() {
		animate();
		damagedAnimation();
		if (spawning) {
			spawnAnimation(timeSpawn / 3);
			return;
		}
		distPlayer = calculateDistance(centerX(), centerY(), Game.player.centerX(), Game.player.centerY());
		if (distPlayer > range)
			movement();
		else if (distPlayer < 3 * range / 4)
			reverseMovement();
		clampBounds(outOfBounds());
		attack();
		attackTimer++;

		slownessEffect(0.99);

		shotDamage();
		if (life <= 0)
			die();
	}
}
