package entities.AE;

import java.awt.image.BufferedImage;
import main.Game;
import world.Camera;
import entities.enemies.Enemy;

public class AE_HellFlame extends Attack_Entity {
	
	private double speed;
	private double dx, dy, damage;
	private int time, spawntime;
	private int maxFrames = 20, frames = 0, index, maxIndex = 2;
	
	public AE_HellFlame(int x, int y, int width, int height, double spd, double dx, double dy, int dmg, BufferedImage sprite, int time) {
		super(x, y, height, width, sprite, time);
		this.speed = spd;
		this.dx = dx;
		this.dy = dy;
		this.damage = dmg;
		this.getAnimation(144, 112, 16, 16, 2);
		this.setMask(0, 0, 48, 48);
		this.depth = 3;
	}
	
	public void tick() {
		x += dx * speed;
		y += dy * speed;
		time++;
		frames ++;
		if (frames == maxFrames) {
			index++;
			frames = 0;
			if (index == maxIndex) {
				index = 0;
			}
		}
		if (time == this.timeLife) {
			this.die();
		}
		
		colidingEnemy();
		refreshTick();
		spawnFire();
	}
	
	private void spawnFire() {
		spawntime++;
		if (spawntime == 4) {
			spawntime = 0;
			Game.entities.add(new AE_Fire(this.getX(), this.getY() + 32, 16, 16, null, 40));
			Game.entities.add(new AE_Fire(this.getX() + 32, this.getY() + 32, 16, 16, null, 40));
		}
	}
	
	private void colidingEnemy() {
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy ene = Game.enemies.get(i);
			if(isColiding(ene) && TickTimer(15)) {
				ene.life -= damage;
			}
		}
	}
	
	public void render() {
		Game.gameGraphics.drawImage(this.animation[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), 48, 48, null);
	}
}
