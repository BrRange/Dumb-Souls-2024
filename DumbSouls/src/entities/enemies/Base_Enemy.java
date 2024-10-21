package entities.enemies;

import java.awt.image.BufferedImage;

import world.Camera;
import world.World;
import main.*;
import entities.*;
import entities.orbs.*;

public class Base_Enemy extends Enemy{
	
	private int index, maxIndex = 3, frames, maxFrames = 10, timer = 0;
	
	public Base_Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		if (specialRare){
			this.specialMult = 3;
			this.hue = 0xFFFFFF;
		}
		this.getAnimation(0, 80, 16, 16, 3);
		this.expValue = 10 * specialMult;
		this.soulValue = 1 * specialMult;
		this.maxLife = 10 * specialMult + (int)(0.1 * World.wave);
		this.life = maxLife;
		this.maxSpeed = 1 + (specialMult-1)/3;
		this.frost = 0;
		this.speed = this.maxSpeed;
		this.setMask(3, 2, 8, 14);
		this.timeSpawn = 150;
		spawning = true;
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
		Game.player.life -= 8 * specialMult + 0.08 * World.wave;
		timer = 0;
	}
	
	private void die() {
		Game.enemies.remove(this);
		Game.entities.add(new EXP_Orb(this.getX(), this.getY(), 16, 16, Enemy.baseSprite, this.expValue, this.hue));
		Player.souls += this.soulValue;
	}
	
	public void tick() {
		animate();
		if (spawning == false) {
			if (!isColiding(this, Game.player)) {
				this.movement();
			}
			else {
				if (timer % 15 == 0) {
					attack();
				}
				timer += 1;
			}

			this.frostEffect(0.99);
			
			this.shotDamage();
			if (life <= 0) {
				die();
			}
		}
		else {
			this.spawnAnimation(timeSpawn / 3);
		}
	}
	
	public void render() {
		Game.gameGraphics.drawImage(this.animation[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
	}
}
