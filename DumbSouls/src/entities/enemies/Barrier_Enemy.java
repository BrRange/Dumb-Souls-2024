package entities.enemies;

import java.awt.image.BufferedImage;

import world.Camera;
import world.World;
import main.*;
import entities.*;
import entities.orbs.EXP_Orb;

public class Barrier_Enemy extends Enemy{
	
	private int index, maxIndex = 2, frames, maxFrames = 20, timer = 0;
	
	public Barrier_Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		if (specialRare){
			specialMult = 2;
			hue = 0xFFFFFF;
		}
		this.getAnimation(144, 80, 48, 32, 2);
		this.expValue = 30 * specialMult;
		this.soulValue = 12 * specialMult;
		this.maxLife = 250 * specialMult + (int)(2.5 * World.wave);
		this.life = maxLife;
		this.maxSpeed = 0.6 + (specialMult - 1)/3;
		this.speed = this.maxSpeed;
		this.frost = 0;
		this.setMask(1, 1, 46, 30);
		this.spawning = true;
		this.timeSpawn = 180;
	}
	
	private void animate() {
		frames++;
		if (frames == maxFrames) {
			frames = 0;
			index++;
			if (index == maxIndex) {
				index = 0;
			}
		}
	}
	
	private void attack() {
		Game.player.life -= 82 * specialMult + 0.82 * World.wave;
		timer = 0;
	}
	
	private void die() {
		Game.enemies.remove(this);
		Game.enemies.add(new Debri_Enemy(this.getX(), this.getY() - 16, 16, 16, Enemy.baseSprite, this.expValue, this.specialRare));
		Game.enemies.add(new Debri_Enemy(this.getX() - 16, this.getY() + 16, 16, 16, Enemy.baseSprite, this.expValue, this.specialRare));
		Game.enemies.add(new Debri_Enemy(this.getX() + 16, this.getY() + 16, 16, 16, Enemy.baseSprite, this.expValue, this.specialRare));
		Game.entities.add(new EXP_Orb(this.getX(), this.getY(), 16, 16, Enemy.baseSprite, this.expValue, this.hue));
		Player.souls += this.soulValue;
	}
	
	public void tick() {
		animate();
		if (!spawning) {
			if (!isColiding(this, Game.player)) {
				this.movement();
			}
			else {
				if (timer % 30 == 0) {
					attack();
				}
				timer += 1;
			}

			this.frostEffect(0.92);
			
			this.shotDamage();
			if (life <= 0) {
				die();
			}

		}
		else {
			this.spawnAnimation(timeSpawn / 3);
		}
	}

	public void receiveKnockback(Entity source){
		return;
	}
	
	public void render() {
		Game.gameGraphics.drawImage(this.animation[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
	}
}
