package entities.AE;

import java.awt.image.BufferedImage;
import world.Camera;
import main.Game;
import entities.*;
import entities.enemies.Enemy;

public class AE_IceSpike extends Attack_Entity {
	
	private double damage;
	private int time;
	
	public AE_IceSpike(int x, int y, int width, int height, BufferedImage sprite, int time, int damage) {
		super(x, y, width, height, sprite, time);
		this.damage = damage;
		this.getAnimation(48, 128, 16, 16, 1);
		this.setMask(2, 4, 12, 12);
	}
	
	public void tick() {
		time++;
		if (time == this.timeLife) {
			this.die();
		}
		Collision();
		refreshTick();
	}
	
	public void Collision() {
		for(int i = 0; i < Game.enemies.size(); i++ ) {
			Enemy e = Game.enemies.get(i);
			if (Entity.isColiding(this, e) && TickTimer(5)) {
				e.life -= damage;
				e.frost = Math.max(e.frost, damage);
			}
		}
	}
	
	public void render() {
		Game.gameGraphics.drawImage(this.animation[0], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
	}
	
}
