package entities.weapons;

import java.awt.image.BufferedImage;
import main.Game;
import world.Camera;
import sounds.SoundPlayer;
import entities.Player;
import entities.AE.AE_Hurricane;
import entities.AE.AE_WindBarrage;
import entities.AE.AE_WindDS;
import entities.shots.Shot;

public class Weapon_Wind extends Weapon {
	
	public static BufferedImage shotFace;
	public static BufferedImage sprite = Game.sheet.getSprite(64, 32, 16, 16);
	private int hrcDamage = 1, ablt3Dmg = 1;
	private double hrcSpeed = 0.8, ablt3Spd = 5;
	public static int soulCost = 100;
	public static boolean unlocked = false;
	private SoundPlayer sound1, sound2;
	
	public Weapon_Wind() {
		shotFace = Game.sheet.getSprite(128, 32, 16, 16);
		setAttackTimer(2);
		Game.player.push = 5;
		sound1 = new SoundPlayer("res/sounds/wind_atk.wav");
		sound2 = new SoundPlayer("res/sounds/wind_ablt2.wav");
		setOptionsNames(9);
		getAnimation(80, 32, 16, 16, 3);
		shotDamage = 3;
		shotSpeed = 6;
		dashDuration = 30;
	}
	
	private void setOptionsNames(int opt) {
		listNames = new String[opt];
		listNames[0] = "Life Boost";
		listNames[1] = "Speed Boost";
		listNames[2] = "Max Mana";
		listNames[3] = "Mana Recover";
		listNames[4] = "Wind Strength";
		listNames[5] = "Wind Speed";
		listNames[6] = "Wind Dash";
		listNames[7] = "Hurricane";
		listNames[8] = "Wind Barrage";
	}
	
	public void checkOptions(String opt) {
		switch (opt) {
		case "Life Boost":
			Game.player.maxLife += 20;
			break;
		case "Speed Boost":
			Game.player.maxSpeed += 0.4;
			Game.player.speed = Game.player.maxSpeed;
			break;
		case "Wind Strength":
			shotDamage += 1;
			Game.player.push += 0.4;
			break;
		case "Max Mana":
			Game.player.maxMana += 10;
			break;
		case "Mana Recover":
			Game.player.manaRec += 0.5;
			break;
		case "Wind Speed":
			shotSpeed += 2;
			Game.player.push += 0.2;
			break;
		case "Wind Dash":
			if (availableDash == false) {
				availableDash = true;
			}
			else {
				dashDuration += 5;
			}
			break;
		case "Hurricane":
			if (availablePowerMove == false) {
				availablePowerMove = true;
			}
			else {
				hrcSpeed += 0.1;
				hrcDamage += 1;
			}
			break;
		case "Wind Barrage":
			if (availableSpecialMove == false) {
				availableSpecialMove = true;
			}
			else {
				ablt3Dmg += 1;
				ablt3Spd += 0.3;
			}
			break;
		default:
			Player.souls += 200;
		}
	}
	
	public void Attack() {
		sound1.PlaySound();
		double ang = Math.atan2(Game.my / Game.scale - Game.player.centerY() + Camera.getY(), Game.mx / Game.scale  - Game.player.centerX() + Camera.getX());
		double dx = Math.cos(ang);
		double dy =  Math.sin(ang);
		
		Game.shots.add(new Shot(Game.player.centerX(), Game.player.centerY(), 12, 12, dx, dy, ang, shotSpeed, shotDamage, 35, shotFace));
	}
	
	public void Dash() {
		int manaCost = 20;
		if (availableDash && Game.player.mana >= manaCost && !useDash) {
			useDash = true;
			Game.player.mana -= manaCost;
		}
		if(useDash) {
			Game.entities.add(new AE_WindDS(Game.player.pos.getX(), Game.player.pos.getY(), dashDuration));
			dashTick ++;
			if (dashTick < dashDuration) {
				Game.player.speedBoost *= 4;
			}
			else {
				dashTick = 0;
				useDash = false;
			}
		}
	}
	
	public void powerMove() {
		int manaCost = 64;
		if (availablePowerMove && Game.player.mana >= manaCost && !usePowerMove) {
			usePowerMove = true;
			Game.player.mana -= manaCost;
		}
		if (usePowerMove) {
			usePowerMove = false;
			Game.entities.add(new AE_Hurricane(Game.player.centerX(), Game.player.centerY(), hrcSpeed, hrcDamage));
		}
	}
	
	public void specialMove() {
		int manaCost = 36;
		if (availableSpecialMove && Game.player.mana >= manaCost && !useSpecialMove) {
			sound2.PlaySound();
			useSpecialMove = true;
			Game.player.mana -= manaCost;
		}
		if (useSpecialMove) {
			double deltaX = Game.mx / Game.scale - Game.player.centerX() + Camera.getX();
			double deltaY = Game.my / Game.scale - Game.player.centerY() + Camera.getY();
			double mag = Math.hypot(deltaX, deltaY);
			if(mag == 0) mag = 1;
			Game.entities.add(new AE_WindBarrage(Game.player.centerX(), Game.player.centerY(), 32, 32, ablt3Spd, deltaX / mag, deltaY / mag, ablt3Dmg, 30));
			useSpecialMove = false;
		}
	}
}
