package entities.AE;

import entities.enemies.Enemy;
import java.util.function.Function;
import main.Game;
import world.Camera;

public class AE_Hurricane extends AE_Attack_Entity{
	
	public AE_Hurricane(int x, int y, double spd, int dmg) {
		super(x, y, 32, 32, null, 240);
		speed = spd;
		damage = dmg;
		push = -2;
		depth = 2;
		getAnimation(16, 128, 16, 16, maxIndex);
		setMask(6, 0, 52, 32);
		maxFrames = 10;
	}

	Function<Enemy, Void> attackCollision = (target) -> { 
		target.life -= damage;
		target.receiveKnockback(this);
        return null;
	};
	
	public void tick() {
		frames ++;
		tickTimer ++;
		
		double destX = Game.mx / Game.scale;
		double destY = Game.my / Game.scale;
		double startX = x + 28 - Camera.getX();
		double startY = y + 18 - Camera.getY();

		
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
		
		collisionEnemy(false, 0, attackCollision);
	}
	
	public void render() {
		Game.gameGraphics.drawImage(animation[index], getX() - Camera.getX(), getY() - Camera.getY(), 64, 32, null);
	}
	
}
