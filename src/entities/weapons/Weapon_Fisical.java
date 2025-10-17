package entities.weapons;

import java.awt.image.BufferedImage;

import entities.AE.AE_Animation;
import entities.AE.AE_PunchRain;
import entities.AE.AE_Rupture;
import entities.shots.Shot;
import main.Game;
import world.Camera;

public class Weapon_Fisical extends Weapon {
	public static BufferedImage shotFace;
	private int powerMoveDmg = 10, powerMoveSize = 64, specialMoveDmg = 6, specialMoveTick, combo;
	public static int soulCost = 500;
	public static boolean unlocked = false;
	public static BufferedImage sprite = Game.sheet.getSprite(144, 16, 16, 16);

	public Weapon_Fisical() {
		shotFace = Game.sheet.getSprite(211, 19, 10, 10);
		setAttackTimer(3);
		Game.player.push = 10;
		attackTimer = 12;
		setOptionsNames(9);
		getAnimation(160, 16, 16, 16, 3);
		dashDuration = 30;
		shotDamage = 4;
		shotSpeed = 2.5;
	}

	private void setOptionsNames(int opt) {
		listNames = new String[opt];
		listNames[0] = "Life Boost";
		listNames[1] = "Speed Boost";
		listNames[2] = "Max Mana";
		listNames[3] = "Mana Recover";
		listNames[4] = "Punch Strength";
		listNames[5] = "Fisical Dash";
		listNames[6] = "Strength Wave";
		listNames[7] = "Punch Rain";
		listNames[8] = "Fisical Condition";
	}

	public void checkOptions(String opt) {
		switch (opt) {
			case "Life Boost":
				Game.player.maxLife += 40;
				break;
			case "Speed Boost":
				Game.player.maxSpeed += 0.3;
				Game.player.speed = Game.player.maxSpeed;
				break;
			case "Max Mana":
				Game.player.maxMana += 10;
				break;
			case "Mana Recover":
				Game.player.manaRec += 0.5;
				break;
			case "Punch Strength":
				shotDamage += 2;
				Game.player.push += 2;
				break;
			case "Fisical Dash":
				if (availableDash == false) {
					availableDash = true;
				} else {
					dashDuration += 20;
				}
				break;
			case "Strength Wave":
				if (availablePowerMove == false) {
					availablePowerMove = true;
				} else {
					powerMoveSize += 24;
					powerMoveDmg += 5;
				}
				break;
			case "Punch Rain":
				if (availableSpecialMove == false) {
					availableSpecialMove = true;
				} else {
					specialMoveDmg += 3;
				}
				break;
			case "Fisical Condition":
				Game.player.lifeRec += 0.004;
				break;
		}
	}

	public void Attack() {
		combo += 1;
		int ruptureScale = shotDamage * 8;
		double ang = Math.atan2(Game.my / Game.scale - Game.player.centerY() + Camera.getY(),
				Game.mx / Game.scale - Game.player.centerX() + Camera.getX());
		double dx = Math.cos(ang);
		double dy = Math.sin(ang);
		switch (combo) {
			case 1:
				Game.shots.add(new Shot(Game.player.centerX(), Game.player.centerY(), 10, 10, dx, dy, ang, shotSpeed,
						shotDamage, 20, shotFace));
				break;
			case 3:
				Game.shots.add(new Shot(Game.player.centerX(), Game.player.centerY(), 10, 10, dx, dy, ang, 3.5,
						shotDamage, 20, shotFace));
				Game.shots.add(new Shot(Game.player.centerX(), Game.player.centerY(), 10, 10, dx, dy, ang, 3.75,
						shotDamage + 2, 20, shotFace));
				break;
			case 5:
				Game.shots.add(new Shot(Game.player.centerX(), Game.player.centerY(), 10, 10, dx, dy, ang, 3.5,
						shotDamage, 20, shotFace));
				Game.shots.add(new Shot(Game.player.centerX(), Game.player.centerY(), 10, 10, dx, dy, ang, 4,
						shotDamage + 4, 20, shotFace));
				Game.entities.add(new AE_Rupture(Game.player.centerX(),
						Game.player.centerY(), ruptureScale, 60,
						shotDamage * 2));
				combo = 0;
				break;
			default:
				Game.shots.add(new Shot(Game.player.centerX(), Game.player.centerY(), 3, 3, dx, dy, ang,
						2.5 + combo / 10.0, shotDamage + combo, 20, shotFace));
				break;
		}
	}

	public void Dash() {
		int manaCost = 4;

		if (availableDash && Game.player.mana >= manaCost && !useDash) {
			useDash = true;
			Game.player.mana -= manaCost;
		}
		if (useDash) {
			dashTick += 1;
			Game.entities.add(new AE_Animation(Game.player.pos.getX() + Game.rand.nextInt(16),
					Game.player.pos.getY() + Game.rand.nextInt(16), 16, 16, 20, 192, 128));
			Game.player.speedBoost *= 3;
			if (dashTick >= dashDuration) {
				dashTick = 0;
				useDash = false;
			}
		}
	}

	public void powerMove() {
		int manaCost = 30;
		if (availablePowerMove && Game.player.mana >= manaCost && !usePowerMove) {
			usePowerMove = true;
			Game.player.mana -= manaCost;
		}
		if (usePowerMove) {
			Game.entities.add(new AE_Rupture(Game.player.centerX(), Game.player.centerY(), powerMoveSize, 80, powerMoveDmg));
			usePowerMove = false;
		}
	}

	public void specialMove() {
		int manaCost = 50;
		if (availableSpecialMove && Game.player.mana >= manaCost && !useSpecialMove) {
			useSpecialMove = true;
			Game.player.mana -= manaCost;
		}
		if (useSpecialMove) {
			specialMoveTick++;
			int off = Game.rand.nextInt(20);
			double deltaX = Game.mx / Game.scale + off - Game.player.centerX() + Camera.getX();
			double deltaY = Game.my / Game.scale + off - Game.player.centerY() + Camera.getY();
			double mag = Math.hypot(deltaX, deltaY);
			if (specialMoveTick % 2 == 0) {
				Game.entities.add(new AE_PunchRain(Game.player.centerX(), Game.player.centerY(), deltaX / mag,
						deltaY / mag, specialMoveDmg));
			}
			if (specialMoveTick >= 40) {
				specialMoveTick = 0;
				useSpecialMove = false;
			}
		}
	}
}
