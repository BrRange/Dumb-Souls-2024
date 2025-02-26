package entities.AE;

import entities.Entity;
import entities.enemies.Enemy;
import entities.shots.Shot;
import java.awt.image.BufferedImage;
import java.util.function.Function;
import main.Game;
import world.Camera;

public abstract class AE_Attack_Entity extends Entity {
	protected int tickTimer = 0, maxFrames = 20, frames = 0;
	
	public AE_Attack_Entity(int x, int y, int width, int height, int life) {
		super(x, y, width, height);
		this.life = life;
		this.maxIndex = 2;
	}
	
	protected void getAnimation(int x, int y, int width, int height, int frames) {
		animation = new BufferedImage[frames];
		
		for(int i = 0; i < animation.length; i++ ) {
			animation[i] = Game.sheet.getSprite(x , y, width, height);
			x += width;
		}
	}
	
	protected void die() {
		Game.entities.remove(this);
	}

	protected void collisionEnemy(boolean hasTickTimer, int framesDamage, Function<Enemy, Void> attack) {
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy ene = Game.enemies.get(i);
			if(hasTickTimer){
				if(TickTimer(framesDamage)) {
					if (isColiding(ene)){
						attack.apply(ene);
						ene.damaged = true;
					}
				}
			}
			else {
				if (isColiding(ene)){
					attack.apply(ene);
					ene.damaged = true;
				}
			}
		}
	}

	protected void collisionEnemiesShots(boolean hasTickTimerEnemy, boolean hasTickTimerShot, int framesDamage, Function<Enemy, Void> enemies, Function<Shot, Void> shots) {
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy ene = Game.enemies.get(i);
			if(hasTickTimerEnemy){
				if(TickTimer(framesDamage)) {
					if (isColiding(ene)){
						enemies.apply(ene);
						ene.damaged = true;
					}
				}
			}
			else {
				if (isColiding(ene)){
					enemies.apply(ene);
					ene.damaged = true;
				}
			}
		}
		for (int i = 0; i < Game.eShots.size(); i++) {
			Shot eSh = Game.eShots.get(i);
			if(hasTickTimerShot){
				if(TickTimer(framesDamage)) {
					if (isColiding(eSh)){
						shots.apply(eSh);
					}
				}
			}
			else {
				if (isColiding(eSh)){
					shots.apply(eSh);
				}
			}
		}
	}

	protected boolean TickTimer(int frames){
		if (tickTimer % frames == 0){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void render() {
		Game.gameGraphics.drawImage(animation[0], pos.getX() - Camera.getX(), pos.getY() - Camera.getY(), null);
	}
}
