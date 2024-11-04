package entities.AE;

import java.awt.image.BufferedImage;
import entities.enemies.Enemy;
import main.Game;
import world.Camera;

public class AE_Rupture extends Attack_Entity {
	
	public int time, frames, index, maxFrames = 10, maxIndex = 3;
	public double dmg;
	
	public AE_Rupture(int x, int y, int width, int height, BufferedImage sprite, int time, int dmg) {
		super(x, y, width, height, sprite, time);
		this.setMask(0, 0, width, height);
		this.getAnimation(80, 128, 16, 16, 3);
		this.dmg = dmg;
		this.depth = 0;
	}
	
	public void tick() {
		time ++;
		if (frames <= 40) {
			frames++;
			if (frames == maxFrames) {
				index++;
			}
		}
		else {
			index = 2;
		}
		if (time == this.timeLife) {
			this.die();
		}
		Collision();
	}
	
	private void Collision() {
		if (time <= 6 && time % 2 == 0) {
			for (int i = 0; i < Game.enemies.size(); i++) {
				Enemy ene = Game.enemies.get(i);
				if(isColiding(ene)) {
					ene.life -= dmg;
					ene.receiveKnockback(Game.player);
				}
			}	
		}
	}
	
	public void render() {
		Game.gameGraphics.drawImage(this.animation[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), width, height, null);
	}
}
