package entities.enemies;

import main.Game;

public class Enemy_SpawnPod extends Enemy {
	private Enemy owner;
	public Enemy_SpawnPod(int x, int y, int w, int h, int time, Enemy own) {
		super(x, y, w, h, null);
		owner = own;
		life = time;
		getAnimation(112, 153, 32, 23, 3);
		maxFrames = time / 3;
		this.maxIndex = 3;
		depth = 2;
	}

	public void tick() {
		attackTimer++;
		animate();
		damagedAnimation();
		if (attackTimer == life) {
			owner.invulnerable = false;
			Game.enemies.remove(this);
		}

	}
}
