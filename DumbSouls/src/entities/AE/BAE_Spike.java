package entities.AE;

import java.awt.image.BufferedImage;
import world.Camera;
import main.Game;

public class BAE_Spike extends AE_Attack_Entity{
	
	public BAE_Spike(int x, int y, int width, int height, BufferedImage sprite, int life, double dmg) {
		super(x, y, width, height, sprite, life);
		depth = 0;
		damage = dmg;
		getAnimation(16, 160, 16, 16, 3);
		setMask(2, 0, 8, 16);
	}
	
	public void tick() {
		life--;
		if (life <= 30) {
			index = 2;
			if (isColiding(Game.player) && tickTimer % 60 == 0) {
				attack();
				tickTimer++;
			}
		}
		if (life == 0) {
			die();
		}
	}
	
	private void attack() {
		tickTimer = 0;
		Game.player.life -= damage;
	}
	
	public void render() {
		Game.gameGraphics.drawImage(animation[index], pos.getX() - Camera.getX(), pos.getY() - Camera.getY(), null);
	}
}
