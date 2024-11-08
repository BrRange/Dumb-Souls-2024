package entities.AE;

import java.awt.image.BufferedImage;
import world.Camera;
import entities.enemies.Enemy;
import main.Game;

public class AE_Hurricane extends Attack_Entity{
	
	private double speed, damage;
	private int frames, maxFrames = 10, index, maxIndex = 2, time;
	
	public AE_Hurricane(int x, int y, int width, int height, BufferedImage sprite, int time, double spd, int dmg) {
		super(x, y, width, height, sprite, time);
		this.speed = spd;
		this.damage = dmg;
		this.timeLife = time;
		this.push = -2;
		this.depth = 2;
		this.getAnimation(16, 128, 16, 16, maxIndex);
		this.setMask(6, 0, 52, 32);
	}
	
	public void tick() {
		frames ++;
		time ++;
		
		double destX = Game.mx / Game.scale;
		double destY = Game.my / Game.scale;
		double startX = this.x + 28 - Camera.getX();
		double startY = this.y + 18 - Camera.getY();

		
		if (calculateDistance((int)destX, (int)destY, (int)startX, (int)startY) > 1){
			double deltaX = destX - startX;
			double deltaY = destY - startY;
			double mag = Math.sqrt(deltaX * deltaX + deltaY * deltaY) + 10;
			if(mag == 0) mag = 1;
			this.x += deltaX / mag * (speed + mag / 50);
			this.y += deltaY / mag * (speed + mag / 50);
		}
		
		if (frames == maxFrames) {
			frames = 0;
			index++;
			if (index == maxIndex) {
				index = 0;
			}
		}
		
		if (time == this.timeLife) {
			this.die();
		}
		
		enemyCollision();
		refreshTick();
	}
	
	public void enemyCollision() {
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy ene = Game.enemies.get(i);
			if (isColiding(ene)) {
				ene.life -= this.damage;
				ene.receiveKnockback(this);
			}
		}
	}
	
	public void render() {
		Game.gameGraphics.drawImage(this.animation[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), 64, 32, null);
	}
	
}
