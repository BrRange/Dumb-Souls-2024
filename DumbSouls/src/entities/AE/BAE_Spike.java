package entities.AE;

import entities.types.Collider.ColliderSquare;
import main.Game;
import world.Camera;

public class BAE_Spike extends AE_Attack_Entity{
	
	public BAE_Spike(int x, int y, int width, int height, int life, double dmg) {
		super(x, y, width, height, life);
		depth = 0;
		damage = dmg;
		getAnimation(16, 160, 16, 16, 3);
		hitbox = new ColliderSquare(pos, 2, 0, 8, 16);
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
