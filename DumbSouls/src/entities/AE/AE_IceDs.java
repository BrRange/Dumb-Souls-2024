package entities.AE;

import java.awt.image.BufferedImage;
import entities.enemies.Enemy;
import main.Game;

public class AE_IceDs extends Attack_Entity {
	
	public int damage;
	private int time = 0;
	
	public AE_IceDs(int x, int y, int width, int height, BufferedImage sprite, int time) {
		super(x, y, width, height, sprite, time);
		setMask(0, 0, 16, 16);
		getAnimation(32, 112, 16, 16, 1);
		depth = 0;
	}
	
	public void tick() {
		time ++;
		if (time == life) {
			die();
		}
		Collision();
		refreshTick();
	}
	
	private void Collision() {
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy ene = Game.enemies.get(i);
			if(isColiding(ene) && TickTimer(5)) {
				ene.slowness = Math.max(ene.slowness, 4);
			}
		}
	}
}
