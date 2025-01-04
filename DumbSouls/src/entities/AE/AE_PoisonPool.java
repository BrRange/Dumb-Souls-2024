package entities.AE;

import java.awt.image.BufferedImage;
import world.Camera;
import entities.enemies.Enemy;
import main.Game;

public class AE_PoisonPool extends AE_Attack_Entity {
	
	public int frames, index, maxFrames = 20, maxIndex = 2;
	public double dmg;
	
	public AE_PoisonPool(int x, int y, int width, int height, BufferedImage sprite, int time, int dmg) {
		super(x, y, width, height, sprite, time);
		setMask(0, 0, width, height);
		getAnimation(128, 128, 16, 16, 2);
		this.dmg = dmg;
	}
	
	public void tick() {
		Collision();
		life--;
		frames++;
		if (frames == maxFrames) {
			index++;
			frames = 0;
			if (index == maxIndex) {
				index = 0;
			}
		}
		
		if (life == 0) {
			die();
		}
	}
	
	public void Collision() {
		if (life % 12 == 0) {
			for (int i = 0; i < Game.enemies.size(); i++) {
				Enemy ene = Game.enemies.get(i);
				if(isColiding(ene)) {
					ene.slowness = Math.max(ene.slowness, 5);
					ene.life -= dmg;
				}
			}	
		}
	}
	
	public void render() {
		Game.gameGraphics.drawImage(animation[index], getX() - Camera.getX(), getY() - Camera.getY(), width, height, null);
	}
}
