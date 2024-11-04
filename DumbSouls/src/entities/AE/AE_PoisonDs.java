package entities.AE;

import java.awt.image.BufferedImage;
import main.Game;
import entities.enemies.Enemy;
import entities.shots.Enemy_Shot;
import world.Camera;

public class AE_PoisonDs extends Attack_Entity{
	
	private int time, frames, maxFrames = 10, index, maxIndex = 2, dmg;
	
	public AE_PoisonDs(int x, int y, int width, int height, BufferedImage sprite, int time, int dmg) {
		super(x, y, width, height, sprite, time);
		this.dmg = dmg;
		this.setMask(0, 0, width, height);
		this.getAnimation(64, 112, 16, 16, 2);
		this.depth = 2;
	}
	
	public void tick() {
		time ++;
		frames++;
		if (frames == maxFrames) {
			frames = 0;
			index++;
			if (index == maxIndex) {
				index = 0;
			}
		}
		x = Game.player.getX() - width / 2 + 8;
		y = Game.player.getY() - height / 2 + 8;
		if (time == this.timeLife) {
			this.die();
		}
		Collision();
	}
	
	private void Collision() {
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy ene = Game.enemies.get(i);
			if(isColiding(ene)) {
				if (time % 5 == 0) {
					ene.frost = Math.max(ene.frost, dmg);
					ene.life -= dmg;
				}
			}
		}
		for (int i = 0; i < Game.eShots.size(); i++) {
			Enemy_Shot eSh = Game.eShots.get(i);
			if(isColiding(eSh)) {
				Game.eShots.remove(eSh);
			}
		}
	}
	
	public void render() {
		Game.gameGraphics.drawImage(this.animation[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), this.width, this.height, null);
	}
}
