package entities.AE;

import entities.enemies.Enemy;
import entities.types.Collider.ColliderCircle;

import java.util.function.Function;

public class AE_IceDs extends AE_Attack_Entity {
	
	public AE_IceDs(int x, int y) {
		super(x, y + 4, 16, 16, 60);
		hitbox = new ColliderCircle(pos, 8);
		getAnimation(32, 112, 16, 16, 1);
		depth = 0;
	}

	Function<Enemy, Void> attackCollision = (target) -> { 
		target.applySlowness(4);
		return null;
	};
	
	public void tick() {
		tickTimer ++;
		if (tickTimer == life) {
			die();
		}
		collisionEnemy(true, 5, attackCollision);
	}
}
