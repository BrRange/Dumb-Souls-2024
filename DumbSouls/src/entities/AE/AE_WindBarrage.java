package entities.AE;

import entities.enemies.Enemy;
import java.util.function.Function;
import main.Game;
import world.Camera; 

public class AE_WindBarrage extends AE_Attack_Entity{

	private Vector dir = new Vector(0, 0);
	
	public AE_WindBarrage(int x, int y, int width, int height, double spd, double dx, double dy, double dmg, int time) {
		super(x, y, width, height, null, time);
		speed = spd;
		dir.set(dx, dy);
		damage = dmg;
		push = -8;
		depth = 2;
		getAnimation(128, 112, 16, 16, 1);
		setMask(0, 0, 32, 32);
	}
	
	public void tick() {
		pos.move(dir.x * speed, dir.y * speed);
		tickTimer++;
		if (tickTimer == life) {
			die();
		}
		
		collisionEnemy(true, 5, attackCollision);
	}

	Function<Enemy, Void> attackCollision = (target) -> { 
		target.life -= damage;
		target.receiveKnockback(this, push);
		return null;
	};
	
	public void render() {
		Game.gameGraphics.drawImage(animation[0], pos.getX() - Camera.getX(), pos.getY() - Camera.getY(), 32, 32, null);
	}
}
