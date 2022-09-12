package entities;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import world.Camera;

import main.Game;

public class Mouth_Enemy extends Enemy {
	private int frames, maxFrames = 10, index, maxIndex = 3;
	
	public Mouth_Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		this.getAnimation(96, 80, 16, 16, 3);
		this.expValue = 20;
		this.soulValue = 2;
		this.life = 8;
		this.maxSpeed = 1.3;
		this.frost = 0;
		this.speed = this.maxSpeed;
		this.setMask(0, 2, 16, 14);
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
	
	private void die() {
		Game.enemies.remove(this);
		Game.player.exp +=  this.expValue;
		Game.player.souls += this.soulValue;
	}
	
	private void atack() {
		if (Entity.isColiding(this, Game.player)) {
			Game.player.life -= 2;
		}
	}
	
	public void tick() {
		animate();
		
		if (Entity.calculateDistance(this.getX(), this.getY(), Game.player.getX(), Game.player.getY()) >= 100) {
			if (this.getX() < Game.player.getX()) {
				this.x += this.speed;
			}
			else if (this.getX() > Game.player.getX()) {
				this.x -= this.speed;
			}
			
			if (this.getY() < Game.player.getY()) {
				this.y += this.speed;
			}
			else if (this.getY() > Game.player.getY()) {
				this.y -= this.speed;
			}
		}
		else {
			if (this.getX() < Game.player.getX()) {
				this.x += this.speed + 0.5;
			}
			else if (this.getX() > Game.player.getX()) {
				this.x -= this.speed + 0.5;
			}
			
			if (this.getY() < Game.player.getY()) {
				this.y += this.speed + 0.5;
			}
			else if (this.getY() > Game.player.getY()) {
				this.y -= this.speed + 0.5;
			}
		}
		atack();
		shootDamage();

		this.frostEffect(0.8);
		
		if (this.life <= 0) {
			this.die();
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(this.animation[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
}