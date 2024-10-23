package entities.AE;

import java.awt.image.BufferedImage;
import entities.enemies.Enemy;
import main.Game;
import world.Camera;

public class AE_Fire2 extends Attack_Entity{
	
	private double speed;
	private double dx, dy, damage;
	private int time;
	
	public AE_Fire2(int x, int y, int height, int width, double spd, double dirx, double diry, int dmg, BufferedImage sprite, int time) {
		super(x, y, height, width, sprite, time);
		this.speed = spd;
		this.dx = dirx;
		this.dy = diry;
		this.damage = dmg;
		this.getAnimation(0, 128, 16, 16, 1);
		this.setMask(2, 2, 3, 3);
		this.depth = 2;
	}
	
	public void tick() {
		x += dx * speed;
		y += dy * speed;
		time++;
		if (time == this.timeLife) {
			this.die();
		}
		
		colidingEnemy();
	}
	
	private void colidingEnemy() {
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy ene = Game.enemies.get(i);
			if(isColiding(this, ene)) {
				ene.life -= damage;
			}
		}
	}
	
	public void render() {
		Game.gameGraphics.drawImage(animation[0], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
	}
}
