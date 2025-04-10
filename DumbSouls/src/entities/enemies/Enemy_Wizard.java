package entities.enemies;

import entities.Entity;
import entities.Player;
import entities.orbs.EXP_Orb;
import entities.shots.Shot;
import graphics.Spritesheet;
import java.awt.image.BufferedImage;
import main.Game;
import world.World;

public class Enemy_Wizard extends Enemy{
    private static Spritesheet sheet = new Spritesheet("res/spriteSheets/Enemies/Enemy_Wizard.png");
    private double range;
   private static BufferedImage shotSprite = sheet.getSprite(0, 64, 16, 16);

	public Enemy_Wizard(int x, int y) {
		super(x, y, 16, 32, null);
		if (specialRare) {
			specialMult = 2;
			hue = 0xFFFFFF;
		}
		getAnimation(0, 0, 16, 32, 3, sheet, 2);
		expValue = 42 * specialMult;
		soulValue = 18 * specialMult;
		range += 70f + 0.035f * World.wave;
		maxLife = 50 * specialMult + (int) (0.5 * World.wave);
		life = maxLife;
		damage = 38 * specialMult + 0.38 * World.wave;
		maxSpeed = 1.2 + (specialMult - 1) / 3;
		speed = maxSpeed;
		setMask(7, 2, 23, 30);
		timeSpawn = 270;
		weight = 3;
	}

	private class Shot_WizardFocus extends Shot{
		public Shot_WizardFocus(int x, int y, double dx, double dy, double dmg, BufferedImage sprite){
			super(x, y, 16, 16, dx, dy, 0, 0, dmg, 300, sprite);
		}
	
		private void narrowAngle(){
			double elapse = maxLife - life;
			speed =  elapse * elapse / 6400 - elapse / 80;
			dir.set(1000 * dir.x + Game.player.centerX() - centerX(), 1000 * dir.y + Game.player.centerY() - centerY());
			dir.normalize();
		}
	
		public void tick() {
			narrowAngle();
			pos.move(dir.x * speed, dir.y * speed);
			life--;
			if (life == 0) die(null);
		}
	}

    protected void animate() {
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
		Game.entities.add(new EXP_Orb(centerX(), centerY(), expValue, hue));
		Player.souls += soulValue;
	}

	private void attack() {
		if (attackTimer >= 450) {
			Vector delta = new Vector(Game.player.centerX() - centerX(), Game.player.centerY() - centerY()).normalize();
			Game.eShots.add(new Shot_WizardFocus(centerX(), centerY(), delta.x, delta.y, damage, shotSprite));
			attackTimer = 0;
		}
	}

	public void tick() {
		damagedAnimation();
		animate();
		if (spawning) {
			spawnAnimation(timeSpawn / 3);
			return;
		}
		if (Entity.calculateDistance(centerX(), centerY(), Game.player.centerX(), Game.player.centerY()) >= range) {
			movement();
		} else attack();
        
		state = attackTimer >= 450 ? 1 : 0;
		attackTimer++;
		shotDamage();

		slownessEffect(0.8);

		if (life <= 0)
			die();
	}
}
