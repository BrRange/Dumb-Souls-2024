package entities.enemies;

import java.awt.image.BufferedImage;
import main.Game;
import world.World;
import entities.*;
import entities.orbs.*;
import world.Camera;

public class Trapper_Enemy extends Enemy{
	private int xP, yP, index = 1, maxIndex = 3, frames, maxFrames = 10, timer = 0, cont = 120;
	private boolean stage2 = true;
	
	public Trapper_Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		if(this.specialRare){
			specialMult = 2;
			hue = 0xFFFFFF;
		}
		this.getAnimation(240, 80, 16, 16, 3);
		this.expValue = 37 * specialMult;
		this.soulValue = 5 * specialMult;
		this.maxLife = 40 + (int)(0.4 * World.wave);
		this.life = maxLife;
		this.maxSpeed = 1 + (specialMult - 1)/3;
		this.frost = 0;
		this.speed = this.maxSpeed;
		this.spawning = true;
		this.timeSpawn = 600;
	}
	
	private void animate() {
		frames++;
		if (frames == maxFrames) {
			frames = 0;
			index++;
			if (index == maxIndex) {
				index = 1;
			}
		}
	}
	
	private void attack() {
		Game.player.life -= 48 * specialMult + 0.48 * World.wave;
		timer = 0;
	}
	
	private void die() {
		Game.enemies.remove(this);
		Game.entities.add(new EXP_Orb(this.getX(), this.getY(), 16, 16, Enemy.baseSprite, this.expValue, this.hue));
		Player.souls += this.soulValue;
	}
	private void stage2() {
		this.shotDamage();
		this.setMask(2, 0, 14, 32);
		animate();
		cont++;
		if (isColiding(this, Game.player)) {
			if (timer % 30 == 0) {
				attack();
			}
			timer += 1;
		}
		if (cont >= 120) {
			stage2 = false;
			cont = 0;
			this.xP = Game.player.getX();
			this.yP = Game.player.getY();
		}
	}
	
	public void tick() {
		if (!spawning) {
			if (this.getX() != xP && this.getY() != yP && !Entity.isColiding(this, Game.player)) {
				objectiveMovement(xP, yP);
				this.setMask(0, 0, 0, 0);
			}
			else {
				stage2 = true;
				stage2();
			}
			
			this.frostEffect(0.995);
			
			if (life <= 0) {
				die();
			}
		}
		else {
			this.spawnAnimation(timeSpawn / 3);
		}
	}
	
	public void render() {
		if (stage2) {
			Game.gameGraphics.drawImage(animation[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), 16, 32, null);
		}
		else {
			Game.gameGraphics.drawImage(animation[0], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
		}
	}
}
