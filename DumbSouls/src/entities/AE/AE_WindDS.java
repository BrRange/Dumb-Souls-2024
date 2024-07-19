package entities.AE;

import java.awt.image.BufferedImage;
import main.Game;
import entities.*;
import entities.enemies.Enemy;
import world.Camera;

public class AE_WindDS extends Attack_Entity {
	
	private int time;
	
	public AE_WindDS(int x, int y, int width, int height, BufferedImage sprite, int time) {
		super(x, y, width, height, sprite, time);
		this.push = 10;
		this.setMask(0, 0, 16, 16);
		this.getAnimation(48, 112, 16, 16, 1);
		this.depth = 2;
	}
	
	public void tick() {
		time ++;
		x = Game.player.getX();
		y = Game.player.getY();
		if (time == this.timeLife) {
			this.die();
		}
		Collision();
	}
	
	private void Collision() {
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if(Entity.isColiding(e, this)) {
				e.life -= 0.2;
				knockBack(this, e);
			}
		}
	}
	
	public void render() {
		Game.gameGraphics.drawImage(this.animation[0], this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
}
