package entities.AE;

import entities.enemies.Enemy;
import entities.types.Vector;
import entities.types.Collider.ColliderCircle;

import java.util.function.Function;

public class AE_VenomGas extends AE_Attack_Entity {

	private Vector dir = new Vector(0, 0);
	
	public AE_VenomGas(int x, int y, double dirx, double diry, double dmg) {
		super(x, y, 32, 32, 80);
		speed = 1.3;
		dir.set(dirx, diry);
		damage = dmg;
		getAnimation(192, 112, 16, 16, 1);
		hitbox = new ColliderCircle(pos, 4);
		depth = 2;
	}
	
	public void tick() {
		pos.move(dir.x * speed, dir.y * speed);
		tickTimer++;
		if (tickTimer == life) {
			die();
		}
		collisionEnemy(false, 0, attackCollision);
	}

	Function<Enemy, Void> attackCollision = (ene) -> { 
		ene.applySlowness(3);
		ene.takeDamage(damage);
		return null;
	};
}
