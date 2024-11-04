package entities.AE;

import java.awt.image.BufferedImage;
import main.Game;
import entities.enemies.Enemy;
import world.Camera;

public class AE_Fire extends Attack_Entity {
	
	private int maxFrames = 20, frames = 0, index, maxIndex = 2, time;
	
	public AE_Fire(int x, int y, int width, int height, BufferedImage sprite, int time) {
		super(x, y, width, height, sprite, time);
		this.setMask(0, 8, 16, 8);
		this.getAnimation(0, 112, 16, 16, maxIndex);
		this.depth = 2;
	}
	
	public void tick() {
		frames ++;
		time ++;
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
		Collision();
		refreshTick();
	}
	
	private void Collision() {
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy ene = Game.enemies.get(i);
			if(TickTimer(15)) {
				if (isColiding(ene)){
					ene.life -= 2;
				}
			}
		}
	}
	
	public void render() {
		Game.gameGraphics.drawImage(this.animation[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
	}
}
