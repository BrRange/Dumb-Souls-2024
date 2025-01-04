package entities.AE;

import java.awt.image.BufferedImage;
import main.Game;
import entities.enemies.Enemy;

public class AE_IceSpike extends AE_Attack_Entity {
	private int time;
	
	public AE_IceSpike(int x, int y, int width, int height, BufferedImage sprite, int time, int damage) {
		super(x, y, width, height, sprite, time);
		this.damage = damage;
		getAnimation(48, 128, 16, 16, 1);
		setMask(2, 4, 12, 12);
	}
	
	public void tick() {
		time++;
		if (time == life) {
			die();
		}
		Collision();
		refreshTick();
	}
	
	public void Collision() {
		for(int i = 0; i < Game.enemies.size(); i++ ) {
			Enemy ene = Game.enemies.get(i);
			if (isColiding(ene) && TickTimer(5)) {
				ene.life -= damage;
				ene.slowness = Math.max(ene.slowness, damage);
			}
		}
	}
}
