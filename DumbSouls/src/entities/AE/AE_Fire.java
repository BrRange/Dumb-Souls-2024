package entities.AE;

import entities.enemies.Enemy;
import entities.types.Collider.ColliderSquare;

import java.util.function.Function;
import main.Game;
import world.Camera;

public class AE_Fire extends AE_Attack_Entity {
	
	public AE_Fire(int x, int y, int life) {
		super(x + 1, y + 6, 14, 10, life);
		hitbox = new ColliderSquare(pos, 1, 0, 14, 8);
		getAnimation(0, 118, 16, 10, maxIndex);
		depth = 2;
	}

	Function<Enemy, Void> attackCollision = (ene) -> { 
		ene.takeDamage(2);
        return null;
	};
	
	public void tick() {
		frames ++;
		tickTimer ++;
		if (frames == maxFrames) {
			index++;
			frames = 0;
			if (index == maxIndex) {
				index = 0;
			}
		}
		if (tickTimer >= life) {
			die();
		}
		collisionEnemy(true, 15, attackCollision);
	}
	
	public void render() {
		Game.gameGraphics.drawImage(animation[index], pos.getX() - Camera.getX(), pos.getY() - Camera.getY(), null);
	}
}
