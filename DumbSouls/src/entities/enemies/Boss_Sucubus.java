package entities.enemies;

import entities.Entity;
import entities.Player;
import entities.orbs.Rune_Orb;
import entities.shots.Shot;
import entities.shots.Shot_SuccubusVampireBat;
import graphics.Shader;
import java.awt.image.BufferedImage;
import main.Game;
import world.Camera;
import world.World;

public class Boss_Sucubus extends Enemy {
	private boolean balance, showAura;
	private BufferedImage spriteAtk = Game.sheet.getSprite(64, 160, 16, 16);
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
		setMask(2, 0, 27, 32);
		weight = 2;
	}

	private void balanceStats() {
		invulnerable = false;
		maxLife = 300 * World.wave / 10;
		expValue = 1500 * World.wave / 10;
		soulValue = 20 * World.wave / 10;
		life = maxLife;
		balance = true;
	}

	private void die() {
		Game.enemies.remove(this);
		Game.player.exp += expValue;
		Player.souls += soulValue;
		World.bossTime = false;
		World.bossName = "";
		Game.enemies.add(new Rune_Orb(centerX(), centerY()));
	}

	private void attack1() {
		Vector delta = new Vector(Game.player.centerX() - centerX(), Game.player.centerY() - centerY()).normalize();
		Game.eShots.add(new Shot(centerX(), centerY(), 6, 3, delta.x, delta.y, 0, 5, damage, 30, spriteAtk));
	}

	private void attack2() {
		Vector delta = new Vector(Game.player.centerX() - centerX(), Game.player.centerY() - centerY()).normalize();
		Game.eShots.add(new Shot_SuccubusVampireBat(Game.player.centerX(), Game.player.centerY(), delta.x,
				delta.y, damage / 3, this));
	}

	private void renderAura() {
		if (attackTimer > 280) {
			showAura = true;
		} else {
			showAura = false;
		}
	}

	private void heal() {
		if (attackTimer % 200 == 0 && life < ((maxLife / 100) * 20)) {
			life += (maxLife / 100) * 8;
		}
	}

	private void tp() {
		if (attackTimer % 300 == 0) {
			int prop;

			if (Game.rand.nextInt(2) == 1) {
				prop = -1;
			} else {
				prop = 1;
			}

			pos.set(Game.player.centerX() + (48 * prop), Game.player.centerY());
			attackTimer = 0;
		}
	}

	public void tick() {
		if (!balance) {
			balanceStats();
		}
		attackTimer++;
		tp();
		renderAura();
		heal();
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
		} else if (Entity.calculateDistance(Game.player.centerX(), Game.player.centerY(), centerX(), centerY()) < 80) {
			reverseMovement();
		}
		damagedAnimation();
		animate();
		shotDamage();
		if (life <= 0) {
			die();
		}
	}

	public void render() {
		if (!damaged) {
			Game.gameGraphics.drawImage(animation[index], pos.getX() - Camera.getX(), pos.getY() - Camera.getY(), null);
		} else {
			Game.gameGraphics.drawImage(Shader.reColor(animation[index], damagedHue), pos.getX() - Camera.getX(),
					pos.getY() - Camera.getY(), null);
		}
		if (showAura) {
			Game.gameGraphics.drawImage(aura, pos.getX() - Camera.getX(), pos.getY() - Camera.getY(), 32, 32, null);
		}
	}
}
