package entities.AE;

import java.awt.image.BufferedImage;
import entities.enemies.Enemy;
import main.Game;
import world.Camera;

public class AE_SnowStorm extends AE_Attack_Entity {
	
	private double speed, damage;
	private int frames, maxFrames = 10, index, maxIndex = 2, time;
	
	public AE_SnowStorm(int x, int y, int width, int height, BufferedImage sprite, int time, double spd, int dmg) {
		super(x, y, width, height, sprite, time);
		speed = spd;
		damage = dmg;
		life = time;
		depth = 3;
		getAnimation(96, 112, 16, 16, maxIndex);
		setMask(0, 24, 64, 40);
	}
	
	public void tick() {
		frames ++;
		time ++;
		
		double destX = Game.mx / Game.scale;
		double destY = Game.my / Game.scale;
		double startX = x + 26 - Camera.getX();
		double startY = y + 16 - Camera.getY();

		if (calculateDistance((int)destX, (int)destY, (int)startX, (int)startY) > 1){
			double deltaX = destX - startX;
			double deltaY = destY - startY;
			double mag = Math.hypot(deltaX, deltaY) + 10;
			if(mag == 0) mag = 1;
			x += deltaX / mag * (speed + mag / 50);
			y += deltaY / mag * (speed + mag / 50);
		}
		
		if (frames == maxFrames) {
			frames = 0;
			index++;
			if (index == maxIndex) {
				index = 0;
			}
		}
		
		if (time == life) {
			die();
		}
		
		enemyCollision();
		refreshTick();
	}
	
	public void enemyCollision() {
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy ene = Game.enemies.get(i);
			if (isColiding(ene) && TickTimer(20)) {
				ene.life -= damage;
				ene.slowness += 2;
			}
		}
	}
	
	public void render() {
		Game.gameGraphics.drawImage(animation[index], getX() - Camera.getX(), getY() - Camera.getY(), 64, 64, null);
	}
	
}
