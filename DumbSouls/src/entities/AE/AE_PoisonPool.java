package entities.AE;

import entities.enemies.Enemy;
import entities.types.Collider.ColliderCircle;

import java.util.function.Function;
import main.Game;
import world.Camera;

public class AE_PoisonPool extends AE_Attack_Entity {

	public AE_PoisonPool(int x, int y, int size, int time, int dmg) {
		super(x, y, size, size, time);
		hitbox = new ColliderCircle(pos, size / 2);
		getAnimation(128, 128, 16, 16, 2);
		damage = dmg;
	}
	
	public void tick() {
		life--;
		frames++;
		if (frames == maxFrames) {
			index++;
			frames = 0;
			if (index == maxIndex) {
				index = 0;
			}
		}

		if (life % 12 == 0) {
			collisionEnemy(false, 0, attackCollision);
		}
		
		if (life == 0) {
			die();
		}
	}

	Function<Enemy, Void> attackCollision = (ene) -> { 
		ene.applySlowness(5);
		ene.takeDamage(damage);
		return null;
	};
	
	public void render() {
		Game.gameGraphics.drawImage(animation[index], pos.getX() - Camera.getX(), pos.getY() - Camera.getY(), width, height, null);
	}
}
