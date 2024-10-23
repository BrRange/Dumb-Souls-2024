package entities.AE;

import java.awt.image.BufferedImage;
import entities.enemies.Enemy;
import main.Game;
import world.Camera;

public class AE_IceDs extends Attack_Entity {
	
	public int damage;
	private int time = 0;
	
	public AE_IceDs(int x, int y, int width, int height, BufferedImage sprite, int time) {
		super(x, y, width, height, sprite, time);
		this.setMask(0, 0, 16, 16);
		this.getAnimation(32, 112, 16, 16, 1);
		this.depth = 0;
	}
	
	public void tick() {
		time ++;
		if (time == this.timeLife) {
			this.die();
		}
		Collision();
		refreshTick();
	}
	
	private void Collision() {
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy ene = Game.enemies.get(i);
			if(isColiding(ene, this) && TickTimer(5)) {
				ene.frost = Math.max(ene.frost, 4);
			}
		}
	}
	
	public void render() {
		Game.gameGraphics.drawImage(this.animation[0], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
	}
}
