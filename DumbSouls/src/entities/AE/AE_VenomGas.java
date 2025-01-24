package entities.AE;

import entities.enemies.Enemy;
import java.util.function.Function;

public class AE_VenomGas extends AE_Attack_Entity {

	private double dx, dy;
	
	public AE_VenomGas(int x, int y, double dirx, double diry, double dmg) {
		super(x, y, 32, 32, null, 80);
		speed = 1.3;
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
		tickTimer++;
		if (tickTimer == life) {
			die();
		}
		collisionEnemy(false, 0, attackCollision);
	}

	Function<Enemy, Void> attackCollision = (target) -> { 
		target.applySlowness(3);
		target.life -= damage;
		return null;
	};
}
