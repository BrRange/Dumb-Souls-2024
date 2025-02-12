package entities.AE;

import entities.enemies.Enemy;
import java.util.function.Function;

public class AE_IceSpike extends AE_Attack_Entity {
	
	public AE_IceSpike(int x, int y, int damage) {
		super(x, y, 6, 16, null, 60);
		this.damage = damage;
		getAnimation(48, 128, 16, 16, 1);
		setMask(2, 4, 12, 12);
	}

	Function<Enemy, Void> attackCollision = (ene) -> { 
		ene.takeDamage(damage);
		ene.applySlowness(damage);
		return null;
	};
	
	public void tick() {
		tickTimer++;
		if (tickTimer == life) {
			die();
		}
		collisionEnemy(true, 5, attackCollision);
	}
}
