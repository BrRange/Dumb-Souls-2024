package entities.AE;

import entities.enemies.Enemy;
import java.util.function.Function;

public class AE_PunchRain extends AE_Attack_Entity {

	private Vector dir = new Vector(0, 0);
	
	public AE_PunchRain(int x, int y, double dirx, double diry, int dmg) {
		super(x, y, 16, 16, 20);
		speed = 3;
		dir.set(dirx, diry);
		damage = dmg;
		push = 0.5;
		getAnimation(176, 112, 16, 16, 1);
		setMask(2, 6, 3, 3);
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
		ene.takeDamage(damage);
		ene.receiveKnockback(this, push);
		die();
		return null;
	};
}
