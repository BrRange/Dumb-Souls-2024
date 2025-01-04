package entities.AE;

import java.awt.image.BufferedImage;
import main.Game;
import entities.enemies.Enemy;

public class AE_VenomGas extends AE_Attack_Entity {
	
	private double speed;
	private double dx, dy, damage;
	private int time;
	
	public AE_VenomGas(int x, int y, int height, int width, double spd, double dirx, double diry, double dmg, BufferedImage sprite, int time) {
		super(x, y, height, width, sprite, time);
		speed = spd;
		dx = dirx;
		dy = diry;
		damage = dmg;
		getAnimation(192, 112, 16, 16, 1);
		setMask(2, 2, 4, 8);
		depth = 2;
	}
	
	public void tick() {
		x += dx * speed;
		y += dy * speed;
		time++;
		if (time == life) {
			die();
		}
		Collision();
	}
	
	private void Collision() {
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy ene = Game.enemies.get(i);
			if(isColiding(ene)) {
				ene.slowness = Math.max(ene.slowness, 3);
				ene.life -= damage;
			}
		}
	}
}
