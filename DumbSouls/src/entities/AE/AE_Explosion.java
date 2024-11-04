package entities.AE;

import java.awt.image.BufferedImage;
import world.Camera;
import main.Game;
import entities.enemies.Enemy;

public class AE_Explosion extends Attack_Entity{
	
	private int damage, maxFrames, maxIndex, frames, index, time;
	
	public AE_Explosion(int x, int y, int width, int height, BufferedImage sprite, int time, int dmg, int receiveKnockback,
			int maxFrames, int maxIndex, int xSprite,int ySprite, int wSprite, int hSprite) {
		
		super(x, y, width, height, sprite, time);
		this.getAnimation(xSprite, ySprite, wSprite, hSprite, maxIndex);
		this.setMask(0, 0, width, height);
		this.damage = dmg;
		this.push = receiveKnockback;
		this.maxFrames = maxFrames;
		this.maxIndex = maxIndex;
	}
	
	private void animate() {
		if (index < maxIndex) {
			frames++;
			if (frames == maxFrames) {
				index++;
				frames = 0;
			}
		}
	}
	
	private void Collision() {
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy ene = Game.enemies.get(i);
			if(isColiding(ene)) {
				ene.life -= damage;
				ene.receiveKnockback(this);
			}
		}	
	}
	
	public void tick() {
		time++;
		animate();
		
		if (index == maxIndex) {
			Collision();
		}
		
		if (time == this.timeLife) {
			this.die();
		}
	}
	
	public void render() {
		Game.gameGraphics.drawImage(this.animation[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), this.width, this.height, null);
	}
}
