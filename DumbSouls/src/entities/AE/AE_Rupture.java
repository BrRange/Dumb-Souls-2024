package entities.AE;

import entities.enemies.Enemy;
import java.util.function.Function;
import main.Game;
import world.Camera;

public class AE_Rupture extends AE_Attack_Entity {
	
	public AE_Rupture(int x, int y, int size, int time, int dmg) {
		super(x, y, size, size, null, time);
		setMask(0, 0, width, height);
		getAnimation(80, 128, 16, 16, 3);
		damage = dmg;
		depth = 0;
		maxIndex = 3;
		maxFrames = 10;
	}
	
	public void tick() {
		tickTimer ++;
		if (frames <= 40) {
			frames++;
			if (frames == maxFrames) {
				index++;
			}
		}
		else {
			index = 2;
		}
		if (tickTimer <= 6 && tickTimer % 2 == 0) {
			collisionEnemy(false, 0, attackCollision);
		}
		if (tickTimer == life) {
			die();
		}
	}

	Function<Enemy, Void> attackCollision = (target) -> { 
		target.life -= damage;
		target.receiveKnockback(this, damage);
		return null;
	};
	
	public void render() {
		Game.gameGraphics.drawImage(animation[index], pos.getX() - Camera.getX(), pos.getY() - Camera.getY(), width, height, null);
	}
}
