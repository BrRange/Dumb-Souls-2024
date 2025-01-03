package entities.enemies;

import java.awt.image.BufferedImage;
import main.Game;
import world.Camera;
import entities.*;
import entities.orbs.Rune_Orb;
import entities.shots.*;
import world.World;

public class Boss_Sucubus extends Enemy {
	private boolean balance, showAura;
	private BufferedImage spriteAtk = Game.sheet.getSprite(64, 160, 16, 16);
	private BufferedImage spriteAtk2 = Game.sheet.getSprite(96, 160, 16, 16);
	private BufferedImage aura = Game.sheet.getSprite(80, 160, 16, 16);
	
	public Boss_Sucubus(int x, int y) {
		super(x, y, 32, 32, Game.sheet.getSprite(105, 192, 12, 10));
		getAnimation(96, 192, 32, 32, 2);
		expValue = 1500;
		soulValue = 20;
		maxLife = 300;
		life = maxLife;
		damage = 30;
		maxSpeed = 0.6;
		speed = maxSpeed;
		maxIndex = 2;
		maxFrames = 40;
		setMask(2, 0, 30, 32);
	}
	
	private void balanceStatus() {
		maxLife =  300 * World.wave / 10;
		expValue = 1500 * World.wave / 10;
		soulValue = 20 * World.wave / 10; 
		life = maxLife;
		balance = true;
	}
	
	private void die() {
		Game.enemies.remove(this);
		Game.player.exp += expValue;
		Player.souls +=  soulValue;
		World.bossTime = false;
		World.bossName = "";
		Game.enemies.add(new Rune_Orb(centerX(), centerY(), 16, 16));
	}
	
	private void attack1() {
		double deltaX = Game.player.centerX() - centerX();
		double deltaY = Game.player.centerY() - centerY();
		double mag = Math.hypot(deltaX, deltaY);
		if(mag == 0) mag = 1;

		Game.eShots.add(new Shot_SuccubusBat(centerX(), centerY(), 6, 3, spriteAtk, deltaX / mag, deltaY / mag, damage, 5, 30, "straight"));
	}
	
	private void attack2() {
		int prob;
		int prob2;
		
		if (Game.rand.nextInt(2) == 1) {
			prob = -1;
		}
		else {
			prob = 1;
		}
		
		if (Game.rand.nextInt(2) == 1) {
			prob2 = -1;
		}
		else  {
			prob2 = 1;
		}
		
		int distance = 100 * prob, distance2 = 80 * prob2 ;
		
		double deltaX = Game.player.centerX() - centerX();
		double deltaY = Game.player.centerY() - centerY();
		double mag = Math.hypot(deltaX, deltaY);
		if(mag == 0) mag = 1;
		
		Game.eShots.add(new Shot_SuccubusVampireBat(Game.player.centerX() + distance, Game.player.centerY() + distance2, 6, 3, spriteAtk2, deltaX / mag, deltaY / mag, damage / 3, 7, 50, "straight"));
	}
	
	private void renderAura() {
		if (attackTimer > 280) {
			showAura = true;
		}
		else {
			showAura = false;
		}
	}
	
	private void cure() {
		if (attackTimer % 200 == 0 && life < ((maxLife / 100) * 20)) {
			life += (maxLife / 100) * 8;
		}
	}
	
	private void tp() {
		if (attackTimer % 300 == 0) {
			int prop;
			
			if (Game.rand.nextInt(2) == 1) {
				prop = -1;
			}
			else {
				prop = 1;
			}
			
			x = Game.player.centerX() + (48 * prop);
			y = Game.player.centerY();
			attackTimer = 0;
		}
	}
	
	public void tick() {
		if (!balance) {
			balanceStatus();
		}
		attackTimer++;
		tp();
		renderAura();
		cure();
		if (Entity.calculateDistance(Game.player.centerX(), Game.player.centerY(), centerX(), centerY()) <= 140) {
			if (attackTimer % 20 == 0) {
				attack1();
			}
			if (attackTimer % 100 == 0) {
				attack2();
			}
		}		
		
		if (Entity.calculateDistance(Game.player.centerX(), Game.player.centerY(), centerX(), centerY()) > 120) {
			movement();
		}
		else if (Entity.calculateDistance(Game.player.centerX(), Game.player.centerY(), centerX(), centerY()) < 80) {
			reverseMovement();
		}
		animate();
		shotDamage();
		if (life <= 0) {
			die();
		}
	}
	
	public void render() {
		Game.gameGraphics.drawImage(animation[index], getX() - Camera.getX(), getY() - Camera.getY(), null);
		if (showAura) {
			Game.gameGraphics.drawImage(aura, getX() - Camera.getX(),  getY() - Camera.getY(), 32, 32, null);
		}
	}
}
