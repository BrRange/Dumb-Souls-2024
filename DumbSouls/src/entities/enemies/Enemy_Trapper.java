package entities.enemies;

import entities.Player;
import entities.orbs.EXP_Orb;
import graphics.Shader;
import main.Game;
import world.Camera;
import world.World;

public class Enemy_Trapper extends Enemy {
	private int xP = Game.player.centerX(), yP = Game.player.centerY(), cont = 120;

	public Enemy_Trapper(int x, int y) {
		super(x, y, 16, 32, null);
		if (specialRare) {
			specialMult = 2;
			hue = 0xFFFFFF;
		}
		getAnimation(240, 80, 16, 16, 3);
		expValue = 37 * specialMult;
		soulValue = 5 * specialMult;
		maxLife = 40 + (int) (0.4 * World.wave);
		life = maxLife;
		damage = 48 * specialMult + 0.48 * World.wave;
		maxSpeed = 1 + (specialMult - 1) / 3;
		speed = maxSpeed;
		timeSpawn = 600;
		maxIndex = 3;
		maxFrames = 10;
		weight = 0;
	}

	private void die() {
		Game.enemies.remove(this);
		Game.entities.add(new EXP_Orb(centerX(), centerY(), expValue, hue));
		Player.souls += soulValue;
	}

	private void stage2() {
		shotDamage();
		setMask(1, 0, 14, 32);
		animate();
		cont++;
		if (isColiding(Game.player)) {
			giveCollisionDamage(Game.player, 30);
		}
		if (cont >= 120) {
			cont = 0;
			xP = Game.player.centerX();
			yP = Game.player.centerY();
		}
	}

	protected void animate() {
		frames++;
		if (frames == maxFrames) {
			frames = 0;
			index++;
			if (index == maxIndex) {
				index = 1;
			}
		}
	}

	public void tick() {
		damagedAnimation();
		if (!spawning) {
			if (centerX() != xP && centerY() != yP && !isColiding(Game.player) && cont == 0) {
				invulnerable = true;
				objectiveMovement(xP, yP);
				setMask(0, 16, 16, 16);
			} else {
				invulnerable = false;
				stage2();
			}

			slownessEffect(0.995);

			if (life <= 0) {
				die();
			}
		} else {
			spawnAnimation(timeSpawn / 3);
		}
	}

	public void render() {
		if (cont > 0) {
			if (!damaged) {
				Game.gameGraphics.drawImage(animation[index], pos.getX() - Camera.getX(), pos.getY() - Camera.getY(), 16, 32,
						null);
			} else {
				Game.gameGraphics.drawImage(Shader.reColor(animation[index], damagedHue), pos.getX() - Camera.getX(),
						pos.getY() - Camera.getY(), 16, 32, null);
			}
		} else {
			Game.gameGraphics.drawImage(animation[0], pos.getX() - Camera.getX(), pos.getY() - Camera.getY(), 16, 32, null);
		}
	}
}
