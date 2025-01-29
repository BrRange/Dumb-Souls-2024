package entities.AE;

import entities.enemies.Enemy;
import java.util.function.Function;

public class AE_Fire2 extends AE_Attack_Entity{
	
	private Vector dir = new Vector(0, 0);
	
	public AE_Fire2(int x, int y, double dirx, double diry, int dmg) {
		super(x + 2, y, 11, 13, null, 60);
		speed = 2;
		dir.set(dirx, diry);
		damage = dmg;
		getAnimation(2, 130, 11, 13, 1);
		setMask(2, 0, 11, 13);
		depth = 2;
	}

	Function<Enemy, Void> attackCollision = (target) -> { 
		target.life -= damage;
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
