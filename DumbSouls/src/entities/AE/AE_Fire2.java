package entities.AE;

import entities.enemies.Enemy;
import entities.types.Vector;
import entities.types.Collider.ColliderCircle;

import java.util.function.Function;

public class AE_Fire2 extends AE_Attack_Entity{
	
	private Vector dir = new Vector(0, 0);
	
	public AE_Fire2(int x, int y, double dirx, double diry, int dmg) {
		super(x + 2, y, 11, 13, 60);
		speed = 2;
		dir.set(dirx, diry);
		damage = dmg;
		getAnimation(2, 130, 11, 13, 1);
		hitbox = new ColliderCircle(pos, 6);
		depth = 2;
	}

	Function<Enemy, Void> attackCollision = (ene) -> { 
		ene.takeDamage(damage);
        return null;
	};
	
	public void tick() {
		pos.move(dir.x * speed, dir.y * speed);
		tickTimer++;
		if (tickTimer == life) {
			die();
		}
		
		collisionEnemy(false, 0, attackCollision);
	}
}
