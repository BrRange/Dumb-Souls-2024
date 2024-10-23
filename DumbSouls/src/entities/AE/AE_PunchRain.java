package entities.AE;

import java.awt.image.BufferedImage;
import entities.*;
import entities.enemies.Enemy;
import main.Game;
import world.Camera;

public class AE_PunchRain extends Attack_Entity {
	
	private double speed;
	private double dx, dy, damage;
	private int time;
	
	public AE_PunchRain(int x, int y, int height, int width, double spd, double dirx, double diry, int dmg, BufferedImage sprite, int time) {
		super(x, y, height, width, sprite, time);
		this.speed = spd;
		this.dx = dirx;
		this.dy = diry;
		this.damage = dmg;
		this.push = 0.5;
		this.getAnimation(176, 112, 16, 16, 1);
		this.setMask(2, 6, 3, 3);
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
			if(Entity.isColiding(this, ene)) {
				ene.life -= damage;
				ene.receiveKnockback(this);
				this.die();
			}
		}
	}
	
	public void render() {
		Game.gameGraphics.drawImage(animation[0], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
	}
}
