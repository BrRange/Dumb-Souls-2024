package entities.enemies;

import graphics.Spritesheet;
import main.Game;

public class Enemy_SpawnPod extends Enemy {
	private Enemy owner;
	public static Spritesheet sheet = new Spritesheet("res/spritesheets/Enemy_SpawnPod.png");
	public Enemy_SpawnPod(int x, int y, int w, int h, int time, Enemy own) {
		super(x, y, w, h, sheet.getSprite(0, 0, 32, 32));
		owner = own;
		life = time;
		getAnimation(0, 0, 32, 32, 3, sheet);
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
