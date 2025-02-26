package entities.enemies;

import entities.AE.BAE_Spike;
import entities.Entity;
import entities.Player;
import entities.orbs.Rune_Orb;
import entities.shots.Shot;
import graphics.Shader;
import graphics.Spritesheet;
import java.awt.image.BufferedImage;
import main.Game;
import world.Camera;
import world.World;

public class Boss_Duality extends Enemy {

	public static Spritesheet sheet = new Spritesheet("res/spritesheets/Bosses/Boss_Duality.png");

	private boolean balance, shieldActive;
	private BufferedImage aura = sheet.getSprite(0, 64, 16, 16);
	private BufferedImage shield = sheet.getSprite(32, 64, 16, 16);
	private BufferedImage spriteAtk = sheet.getSprite(48, 64, 16, 16);


	public Boss_Duality(int x, int y) {
		super(x, y, 32, 32, sheet.getSprite(11, 3, 10, 10));
		getAnimation(0, 0, 32, 32, 3, sheet);
		expValue = 1500;
		soulValue = 20;
		maxLife = 800;
		life = maxLife;
		speed = 0.8;
		maxFrames = 40;
		setMask(9, 3, 14, 21);
		weight = 6;
	}

	public class Shot_DualityBlackHand extends Shot {
		private Boss_Duality owner;
		public Shot_DualityBlackHand(int x, int y, double dx, double dy, Boss_Duality own){
			super(x, y, 13, 14, dx, dy, 0, 5, 40, 35, sheet.getSprite(16, 64, 16, 16));
			owner = own;
		}
	
		public void die(Entity target) {
			if(target == Game.player && World.bossTime){
				Game.player.mana = Math.max(Game.player.mana - damage, 0);
				owner.life = Math.min(owner.maxLife, owner.life + damage);
			}
			Game.eShots.remove(this);
		}
	}

	private void balanceStats() {
		invulnerable = false;
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
		Game.enemies.add(new Rune_Orb(centerX(), centerY()));
	}

	private void closeAtk() {
		if (Entity.calculateDistance(Game.player.centerX(), Game.player.centerY(), centerX(), centerY()) <= 60) {
			Vector delta = new Vector(Game.player.centerX() - centerX(), Game.player.centerY() - centerY()).normalize();
			Game.eShots.add(new Shot_DualityBlackHand(centerX(), centerY(), delta.x, delta.y, this));
			Game.player.mana *= 0.75;
		}
	}

	private void addShield() {
		if (!shieldActive) {
			invulnerable = true;
			weight = 0;
			shieldActive = true;
		} else {
			invulnerable = false;
			weight = 6;
			shieldActive = false;
		}
	}

	private void rangeAtk() {
		attackTimer++;
		if (attackTimer % 40 == 0) {
			Game.entities.add(new BAE_Spike(Game.player.centerX(), Game.player.centerY(), 16, 16, 60, 60));
		}
		if (attackTimer % 60 == 0) {
			Vector delta = new Vector(Game.player.centerX() - centerX(), Game.player.centerY() - centerY()).normalize();
			Game.eShots.add(new Shot_DualityBlackHand(centerX(), centerY(), delta.x, delta.y, this));
		}
		if (attackTimer % 80 == 0) {
			Vector delta = new Vector(Game.player.centerX() - centerX(), Game.player.centerY() - centerY()).normalize();
			Game.eShots.add(new Shot(centerX(), centerY(), 5, 5, delta.x, delta.y, Math.atan2(centerY() - Game.player.centerY(), centerX() - Game.player.centerX()),
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
		shotDamage();

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