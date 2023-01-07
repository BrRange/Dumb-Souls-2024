package entities.enemies;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import world.Camera;
import world.World;
import entities.*;
import entities.shots.*;
import entities.orbs.*;
import main.Game;

public class Eye_Enemy extends Enemy{
	private int frames, maxFrames = 15, index, maxIndex = 3, timeAtk = 0;
	private BufferedImage spriteAtk = Game.sheet.getSprite(0, 160, 16, 16);
	public Enemy_Shot atk;
	
	public Eye_Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		this.getAnimation(48, 80, 16, 16, 3);
		this.expValue = 25;
		this.soulValue = 3;
		this.maxLife = 15 + (int)(15 * 0.01 * World.wave);
		this.life = maxLife;
		this.maxSpeed = 0.8;
		this.frost = 0;
		this.speed = this.maxSpeed;
		this.setMask(3, 3, 12, 11);
		this.timeSpawn = 180;
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
	
	private void die() {
		Game.enemies.remove(this);
		Game.entities.add(new EXP_Orb((int)this.x, (int)this.y, 16, 16, Enemy.baseSprite, this.expValue));
		Player.souls += this.soulValue;
	}
	
	private void attack() {
		timeAtk++;
		if (timeAtk == 60) {
			double ang = Math.atan2((Game.player.getY() - Camera.y) - (this.getY() - Camera.y) ,(Game.player.getX() - Camera.x) - (this.getX() - Camera.x));
			double dx = Math.cos(ang);
			double dy =  Math.sin(ang);
			
			atk = new Enemy_Shot(this.getX(), this.getY(), 6, 3, spriteAtk, dx, dy, 15, 3, 35, "straight");
			Game.eShots.add(atk);
			timeAtk = 0;
		}
	}
	
	
	public void tick() {
		animate();
		if (!spawning) {
			if (Entity.calculateDistance(this.getX(), this.getY(), Game.player.getX(), Game.player.getY()) >= 80) {
				this.movement();
			}
			else {
				attack();
			}
			this.shotDamage();

			this.frostEffect(0.995);

			if (this.life <= 0) {
				die();
			}
		}
		else {
			this.spawnAnimation(60);
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(animation[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
}