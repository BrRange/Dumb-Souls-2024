package entities.AE;

import java.awt.image.BufferedImage;
import world.Camera;
import main.Game;

public class BAE_Spike extends Attack_Entity{
	
	private int life = 0, index, damage, timer;
	
	public BAE_Spike(int x, int y, int width, int height, BufferedImage sprite, int timeLife, int dmg) {
		super(x, y, width, height, sprite, timeLife);
		this.depth = 0;
		this.damage = dmg;
		this.getAnimation(16, 160, 16, 16, 3);
		this.setMask(2, 0, 8, 16);
	}
	
	public void tick() {
		life++;
		if (life > 30) {
			index = 2;
			if (isColiding(Game.player) && timer % 60 == 0) {
				attack();
				timer++;
			}
		}
		if (this.life == this.timeLife) {
			this.die();
		}
	}
	
	private void attack() {
		timer = 0;
		Game.player.life -= damage;
	}
	
	public void render() {
		Game.gameGraphics.drawImage(this.animation[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
	}
}
