package entities.enemies;

import graphics.Shader;
import main.Game;

import java.awt.image.BufferedImage;

import entities.Entity;
import entities.shots.Shot;
import world.Camera;

public class Enemy extends Entity {

	protected BufferedImage[] animation;
	public int expValue, soulValue;
	protected boolean spawning = true, specialRare;
	protected int attackTimer = 0, timeSpawn = 0, contTS, specialMult = 1, hue = 0, frames, maxFrames = 10, index,
			maxIndex = 3, state = 0;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		isSpecial();
	}

	void isSpecial() {
		if (Game.rand.nextInt(256) == 0)
			specialRare = true;
	}

	protected void getAnimation(int x, int y, int width, int height, int frames) {
		animation = new BufferedImage[frames];

		for (int i = 0; i < animation.length; i++) {
			animation[i] = Game.sheet.getSprite(x, y, width, height);
			animation[i] = Shader.reColor(animation[i], hue);
			x += width;
		}
	}

	protected void animate() {
		frames++;
		if (frames == maxFrames) {
			frames = 0;
			index++;
			if (index == maxIndex) {
				index = 0;
			}
		}
	}

	protected void slownessEffect(double thaw) {
		speed = maxSpeed / (1 + slowness);
		slowness = slowness < 0.01 ? 0 : slowness * thaw;
	}

	protected void shotDamage() {
		for (int i = 0; i < Game.shots.size(); i++) {
			Shot sh = Game.shots.get(i);
			if (isColiding(sh)) {
				life -= sh.damage;
				receiveKnockback(Game.player);
				this.damaged = true;
				sh.die(this);
			}
		}
	}

	protected void movement() {
		double deltaX = Game.player.centerX() - centerX();
		double deltaY = Game.player.centerY() - centerY();
		double mag = getMagnitude(deltaX, deltaY);
		x += deltaX * speed / mag;
		y += deltaY * speed / mag;
	}

	protected void reverseMovement() {
		double deltaX = Game.player.centerX() - centerX();
		double deltaY = Game.player.centerY() - centerY();
		double mag = getMagnitude(deltaX, deltaY);
		x += deltaX * -speed / mag;
		y += deltaY * -speed / mag;
	}

	protected void objectiveMovement(int xObjct, int yObjct) {
		double deltaX = xObjct - centerX();
		double deltaY = yObjct - centerY();
		double mag = getMagnitude(deltaX, deltaY);
		x += deltaX * speed / mag;
		y += deltaY * speed / mag;
	}

	protected void spawnAnimation(int frames) {
		if (contTS == 0) {
			Game.enemies.add(new Enemy_SpawnPod(centerX(), centerY(), (int) (width * 1.5), (int) (height * 1.5),
					timeSpawn, specialRare));
		}
		contTS++;
		if (contTS >= timeSpawn) {
			spawning = false;
		}
	}

	public void render() {
		if (!damaged) {
			Game.gameGraphics.drawImage(animation[index + state * maxIndex], getX() - Camera.getX(), getY() - Camera.getY(), width,
					height, null);
		} else {
			Game.gameGraphics.drawImage(Shader.reColor(animation[index], damagedHue), getX() - Camera.getX(),
					getY() - Camera.getY(), width, height, null);
		}
	}

	protected void giveCollisionDamage(Entity target, int attackTimerLimit) {
		if (this.attackTimer % attackTimerLimit == 0) {
			target.life -= this.damage;
			target.damaged = true;
			this.attackTimer = 0;
		}
		this.attackTimer ++;
	}
}
