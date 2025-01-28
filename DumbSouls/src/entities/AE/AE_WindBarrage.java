package entities.AE;

import entities.enemies.Enemy;
import java.util.function.Function;
import main.Game;
import world.Camera; 

public class AE_WindBarrage extends AE_Attack_Entity{

	private double dirX, dirY;
	
	public AE_WindBarrage(int x, int y, int width, int height, double spd, double dx, double dy, double dmg, int time) {
		super(x, y, width, height, null, time);
		speed = spd;
		dirX = dx;
		dirY = dy;
		damage = dmg;
		push = -10;
		depth = 2;
		getAnimation(128, 112, 16, 16, 1);
		setMask(0, 0, 32, 32);
	}
	
	public void tick() {
		x += dirX * speed;
		y += dirY * speed;
		tickTimer++;
		if (tickTimer == life) {
			die();
		}
		
		collisionEnemy(true, 5, attackCollision);
	}

	Function<Enemy, Void> attackCollision = (target) -> { 
		target.life -= damage;
		target.receiveKnockback(this);
		return null;
	};
	
	public void render() {
		Game.gameGraphics.drawImage(animation[0], getX() - Camera.getX(), getY() - Camera.getY(), 32, 32, null);
	}
}
