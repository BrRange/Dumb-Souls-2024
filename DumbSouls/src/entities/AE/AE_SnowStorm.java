package entities.AE;

import java.awt.image.BufferedImage;
import entities.enemies.Enemy;
import main.Game;
import world.Camera;

public class AE_SnowStorm extends Attack_Entity {
	
	private double speed, damage;
	private int frames, maxFrames = 10, index, maxIndex = 2, time;
	
	public AE_SnowStorm(int x, int y, int width, int height, BufferedImage sprite, int time, double spd, int dmg) {
		super(x, y, width, height, sprite, time);
		this.speed = spd;
		this.damage = dmg;
		this.timeLife = time;
		this.depth = 3;
		this.getAnimation(96, 112, 16, 16, maxIndex);
		this.setMask(0, 24, 64, 40);
	}
	
	public void tick() {
		frames ++;
		time ++;
		
		double destX = Game.mx / Game.scale;
		double destY = Game.my / Game.scale;
		double startX = this.x + 26 - Camera.getX();
		double startY = this.y + 16 - Camera.getY();

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
			if (isColiding(ene) && TickTimer(20)) {
				ene.life -= this.damage;
				ene.frost += 2;
			}
		}
	}
	
	public void render() {
		Game.gameGraphics.drawImage(this.animation[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), 64, 64, null);
	}
	
}
