package entities.enemies;

import entities.Entity;
import entities.Player;
import entities.AE.BAE_Spike;
import entities.orbs.Rune_Orb;
import entities.shots.Shot;
import entities.shots.Shot_DualityBlackHand;
import graphics.Shader;
import java.awt.image.BufferedImage;
import main.Game;
import world.Camera;
import world.World;

public class Boss_Duality extends Enemy {

	private int shieldLife;
	private boolean balance, shieldActive;
	private BufferedImage aura = Game.sheet.getSprite(16, 160, 16, 16);
	private BufferedImage shield = Game.sheet.getSprite(16, 176, 16, 16);
	private BufferedImage spriteAtk = Game.sheet.getSprite(32, 176, 16, 16);

	public Boss_Duality(int x, int y) {
		super(x, y, 32, 32, Game.sheet.getSprite(11, 195, 10, 10));
		getAnimation(0, 192, 32, 32, 3);
		expValue = 1500;
		soulValue = 20;
		maxLife = 800;
		life = maxLife;
		speed = 0.8;
		maxFrames = 40;
		setMask(9, 3, 14, 21);
		weight = 6;
	}

	private void balanceStats() {
		maxLife = 800 * World.wave / 10;
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
		Game.enemies.add(new Rune_Orb(centerX(), centerY(), 16, 16));
	}

	private void closeAtk() {
		if (Entity.calculateDistance(Game.player.centerX(), Game.player.centerY(), centerX(), centerY()) <= 60) {
			Vector delta = new Vector(Game.player.centerX() - centerX(), Game.player.centerY() - centerY()).normalize();
			Game.eShots.add(new Shot_DualityBlackHand(centerX(), centerY(), delta.x, delta.y, this));
			Game.player.mana *= 0.75;
		}
	}

	private void shieldColision() {
		for (int i = 0; i < Game.shots.size(); i++) {
			Shot sh = Game.shots.get(i);
			if (isColiding(sh)) {
				sh.die(null);
			}
		}

		if (life <= shieldLife) {
			life = shieldLife;
		}
	}

	private void addShield() {
		if (!shieldActive) {
			shieldActive = true;
			shieldLife = (int) life;
			setMask(-13, -8, 64, 40);
		} else {
			shieldActive = false;
			setMask(9, 3, 14, 21);
		}
	}

	private void rangeAtk() {
		attackTimer++;
		if (attackTimer % 40 == 0) {
			Game.entities.add(new BAE_Spike(Game.player.centerX(), Game.player.centerY(), 16, 16, null, 60, 60));
		}
		if (attackTimer % 60 == 0) {
			Vector delta = new Vector(Game.player.centerX() - centerX(), Game.player.centerY() - centerY()).normalize();
			Game.eShots.add(new Shot_DualityBlackHand(centerX(), centerY(), delta.x, delta.y, this));
		}
		if (attackTimer % 80 == 0) {
			Vector delta = new Vector(Game.player.centerX() - centerX(), Game.player.centerY() - centerY()).normalize();
			Game.eShots.add(new Shot(centerX(), centerY(), 5, 5, delta.x, delta.y, Math.atan2(delta.y, delta.x),
					3, 32, 50, spriteAtk));
		}
		if (attackTimer % 160 == 0) {
			addShield();
			attackTimer = 0;
		}
	}

	public void tick() {
		if (!balance) {
			balanceStats();
		}

		if (Entity.calculateDistance(Game.player.centerX(), Game.player.centerY(), centerX(), centerY()) > 40) {
			movement();
		}
		damagedAnimation();
		animate();
		closeAtk();
		rangeAtk();

		if (shieldActive) {
			shieldColision();
		} else {
			shotDamage();
		}

		if (life <= 0) {
			die();
		}

	}

	public void render() {
		Game.gameGraphics.drawImage(aura, pos.getX() - Camera.getX() - 32, pos.getY() - Camera.getY() - 32, 98, 98, null);
		if (!damaged) {
			Game.gameGraphics.drawImage(animation[index], pos.getX() - Camera.getX(), pos.getY() - Camera.getY(), null);
		} else {
			Game.gameGraphics.drawImage(Shader.reColor(animation[index], damagedHue), pos.getX() - Camera.getX(),
			pos.getY() - Camera.getY(), null);
		}

		if (shieldActive) {
			Game.gameGraphics.drawImage(shield, centerX() - Camera.getX() - 32, centerY() - Camera.getY() - 48, 64, 64,
					null);
		}
	}
}