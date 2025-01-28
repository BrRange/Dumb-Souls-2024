package entities.AE;

import entities.enemies.Enemy;
import java.util.function.Function;
import main.Game;
import world.Camera;

public class AE_SnowStorm extends AE_Attack_Entity {
	
	public AE_SnowStorm(int x, int y, double spd, int dmg) {
		super(x, y, 32, 32, null, 240);
		speed = spd;
		damage = dmg;
		depth = 3;
		getAnimation(96, 112, 16, 16, maxIndex);
		setMask(0, 24, 64, 40);
		maxFrames = 10;
	}
	
	public void tick() {
		frames ++;
		tickTimer ++;
		
		int destX = Game.mx / Game.scale;
		int destY = Game.my / Game.scale;
		double startX = x + 26 - Camera.getX();
		double startY = y + 16 - Camera.getY();

		if (calculateDistance((int)destX, (int)destY, (int)startX, (int)startY) > 1){
			double deltaX = destX - startX;
			double deltaY = destY - startY;
			double mag = Math.hypot(deltaX, deltaY) + 10;
			if(mag == 0) mag = 1;
			x += deltaX / mag * (speed + mag / 50);
			y += deltaY / mag * (speed + mag / 50);
		}
		
		if (frames == maxFrames) {
			frames = 0;
			index++;
			if (index == maxIndex) {
				index = 0;
			}
		}
		
		if (tickTimer == life) {
			die();
		}
		
		collisionEnemy(true, 20, attackCollision);
	}

	Function<Enemy, Void> attackCollision = (target) -> { 
		target.life -= damage;
		target.slowness += 2;
		return null;
	};
	
	public void render() {
		Game.gameGraphics.drawImage(animation[index], getX() - Camera.getX(), getY() - Camera.getY(), 64, 64, null);
	}
	
}
