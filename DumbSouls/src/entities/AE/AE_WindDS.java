package entities.AE;

import java.awt.image.BufferedImage;
import main.Game;
import entities.enemies.Enemy;

public class AE_WindDS extends Attack_Entity {
	
	private int time;
	
	public AE_WindDS(int x, int y, int width, int height, BufferedImage sprite, int time) {
		super(x, y, width, height, sprite, time);
		push = 10;
		setMask(0, 0, 16, 16);
		getAnimation(48, 112, 16, 16, 1);
		depth = 2;
	}
	
	public void tick() {
		time ++;
		x = Game.player.getX();
		y = Game.player.getY();
		if (time == life) {
			die();
		}
		Collision();
	}
	
	private void Collision() {
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy ene = Game.enemies.get(i);
			if(isColiding(ene)) {
				ene.life -= 0.2;
				ene.receiveKnockback(this);
			}
		}
	}
}
