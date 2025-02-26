package entities.AE;

import entities.enemies.Enemy;
import entities.shots.Shot;
import java.util.function.Function;
import main.Game;
import world.Camera;

public class AE_PoisonDs extends AE_Attack_Entity{
	
	public AE_PoisonDs(int x, int y, int size, int time, int dmg) {
		super(x, y, size, size, time);
		damage = dmg;
		setMask(0, 0, width, height);
		getAnimation(64, 112, 16, 16, 2);
		depth = 2;
		maxFrames = 10;
	}
	
	public void tick() {
		tickTimer ++;
		frames++;
		if (frames == maxFrames) {
			frames = 0;
			index++;
			if (index == maxIndex) {
				index = 0;
			}
		}
		pos.set(Game.player.centerX() - width / 2, Game.player.centerY() - height / 2);
		if (tickTimer == life) {
			die();
		}
		collisionEnemiesShots(false, false, 0, enemyCollision, shotCollision);
	}

	Function<Enemy, Void> enemyCollision = (ene) -> { 
		if (tickTimer % 5 == 0) {
			ene.applySlowness(damage);
			ene.takeDamage(damage);
		}
		return null;
	};

	Function<Shot, Void> shotCollision = (target) -> { 
		Game.eShots.remove(target);
		return null;
	};
	
	public void render() {
		Game.gameGraphics.drawImage(animation[index], pos.getX() - Camera.getX(), pos.getY() - Camera.getY(), width, height, null);
	}
}
