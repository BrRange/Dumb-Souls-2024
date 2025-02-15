package entities.AE;

import entities.enemies.Enemy;
import java.util.function.Function;
import main.Game;
import world.Camera;

public class AE_HellFlame extends AE_Attack_Entity {
	
	private int spawntime;
	private Vector dir = new Vector(0, 0);
	
	public AE_HellFlame(int x, int y, double spd, double dx, double dy, int dmg) {
		super(x, y, 32, 32, 80);
		speed = spd;
		dir.set(dx, dy);
		damage = dmg;
		getAnimation(144, 112, 16, 16, 2);
		setMask(0, 0, 48, 48);
		depth = 3;
	}
	
	public void tick() {
		pos.move(dir.x * speed, dir.y * speed);
		tickTimer++;
		frames ++;
		if (frames == maxFrames) {
			index++;
			frames = 0;
			if (index == maxIndex) {
				index = 0;
			}
		}
		if (tickTimer == life) {
			die();
		}
		
		collisionEnemy(true, 15, attackCollision);
		spawnFire();
	}

	Function<Enemy, Void> attackCollision = (ene) -> { 
		ene.takeDamage(damage);
        return null;
	};
	
	private void spawnFire() {
		spawntime++;
		if (spawntime == 4) {
			spawntime = 0;
			Game.entities.add(new AE_Fire(centerX() - 16, centerY() + 16, 40));
			Game.entities.add(new AE_Fire(centerX() + 16, centerY() + 16, 40));
		}
	}
	
	public void render() {
		Game.gameGraphics.drawImage(animation[index], pos.getX() - Camera.getX(), pos.getY() - Camera.getY(), 48, 48, null);
	}
}
