package entities.AE;

import entities.enemies.Enemy;
import entities.types.Collider.ColliderCircle;

import java.util.function.Function;
import main.Game;

public class AE_WindDS extends AE_Attack_Entity {
	
	public AE_WindDS(int x, int y, int time) {
		super(x, y, 16, 16, time);
		push = 10;
		hitbox = new ColliderCircle(pos, 8);
		getAnimation(48, 112, 16, 16, 1);
		depth = 2;
	}
	
	public void tick() {
		tickTimer ++;
		pos.set(Game.player.pos.getX(), Game.player.pos.getY());
		if (tickTimer == life) {
			die();
		}
		collisionEnemy(false, 0, attackCollision);
	}

	Function<Enemy, Void> attackCollision = (ene) -> { 
		ene.takeDamage(0.2);
		ene.receiveKnockback(this, push);
		return null;
	};
}
