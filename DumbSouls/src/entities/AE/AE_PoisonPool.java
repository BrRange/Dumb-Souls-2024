package entities.AE;

import java.awt.image.BufferedImage;
import world.Camera;
import entities.*;
import entities.enemies.Enemy;
import main.Game;

public class AE_PoisonPool extends Attack_Entity {
	
	public int time, frames, index, maxFrames = 20, maxIndex = 2;
	public double dmg;
	
	public AE_PoisonPool(int x, int y, int width, int height, BufferedImage sprite, int time, int dmg) {
		super(x, y, width, height, sprite, time);
		this.setMask(0, 0, width, height);
		this.getAnimation(128, 128, 16, 16, 2);
		this.dmg = dmg;
	}
	
	public void tick() {
		Collision();
		time ++;
		frames++;
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
	}
	
	public void Collision() {
		if (time % 12 == 0) {
			for (int i = 0; i < Game.enemies.size(); i++) {
				Enemy ene = Game.enemies.get(i);
				if(Entity.isColiding(ene, this)) {
					ene.frost = Math.max(ene.frost, 5);
					ene.life -= dmg;
				}
			}	
		}
	}
	
	public void render() {
		Game.gameGraphics.drawImage(this.animation[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), this.width, this.height, null);
	}
}
