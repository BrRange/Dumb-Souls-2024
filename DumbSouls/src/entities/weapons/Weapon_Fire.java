package entities.weapons;

import java.awt.image.BufferedImage;
import main.Game;
import world.Camera;
import entities.AE.AE_Fire;
import entities.AE.AE_Fire2;
import entities.AE.AE_HellFlame;
import entities.shots.Shot;
import sounds.SoundPlayer;

public class Weapon_Fire extends Weapon {
	
	public static BufferedImage shotFace;
	public static BufferedImage sprite = Game.sheet.getSprite(64, 16, 16, 16);
	private int powerMoveDmg = 1, powerMoveTick, powerMoveDuration = 60, specialMoveDmg = 16;
	private double specialMoveSpd = 0.8;
	public static int soulCost = 100;
	public static boolean block = true;
	private SoundPlayer sound1, sound2, sound3;
	
	public Weapon_Fire() {
		shotFace = Game.sheet.getSprite(128, 16, 16, 16);
		setAttackTimer(3);
		sound1 = new SoundPlayer("res/sounds/fire_atk.wav");
		sound2 = new SoundPlayer("res/sounds/fire_ablt1.wav");
		sound3 = new SoundPlayer("res/sounds/fire_ablt2.wav");
		setOptionsNames(9);
		getAnimation(80, 16, 16, 16, 3);
		dashDuration = 30;
		shotDamage = 5;
		shotSpeed = 3;
	}
	
	private void setOptionsNames(int opt) {
		listNames = new String[opt];
		listNames[0] = "Life Boost";
		listNames[1] = "Speed Boost";
		listNames[2] = "Max Mana";
		listNames[3] = "Mana Recover";
		listNames[4] = "FireBall Strength";
		listNames[5] = "FireBall Speed";
		listNames[6] = "Fire Dash";
		listNames[7] = "Blaze";
		listNames[8] = "Hell Flame";
	}
	
	public void checkOptions(String opt) {
		switch(opt){
			case "Life Boost":
				Game.player.maxLife += 20;
				break;
			case "Speed Boost":
				Game.player.maxSpeed += 0.2;
				Game.player.speed = Game.player.maxSpeed;
				break;
			case "Max Mana":
				Game.player.maxMana += 20;
				break;
			case "Mana Recover":
				Game.player.manaRec += 1;
				break;
			case "Fireball Strength":
				shotDamage += 2;
				break;
			case "FireBall Speed":
				shotSpeed += 1;
				break;
			case "Fire Dash":
				if (availableDash == false) {
					availableDash = true;
				}
				else {
					dashDuration += 15;
				}
				break;
			case "Blaze":
				if (availablePowerMove == false) {
					availablePowerMove = true;
				}
				else {
					powerMoveDmg += 1;
					powerMoveDuration += 5;
				}
				break;
			case "Hell Flame":
				if (availableSpecialMove == false) {
					availableSpecialMove = true;
				}
				else {
					specialMoveDmg += 3;
					specialMoveSpd += 0.2;
				}
				break;
			}
		}
	
	public void Attack() {
		if(!sound1.clip.isActive())
			sound1.PlaySound();
		double ang = Math.atan2(Game.my / Game.scale - Game.player.centerY() + Camera.getY(), Game.mx / Game.scale  - Game.player.centerX() + Camera.getX());
		double dx = Math.cos(ang);
		double dy =  Math.sin(ang);
		Game.shots.add(new Shot(Game.player.centerX(), Game.player.centerY(), 12, 12, dx, dy, ang, shotSpeed, shotDamage, 35, shotFace));
	}
	
	public void Dash() {
		int manaCost = 12;
		
		if (availableDash && Game.player.mana >= manaCost && !useDash) {
			useDash = true;
			Game.player.mana -= manaCost;
			sound3.PlaySound();
		}
		if (useDash) {
			Game.player.speedBoost *= 2.5;
			
			dashTick += 1;
			if (dashTick % 2 == 0) {
				Game.entities.add(new AE_Fire(Game.player.centerX(), Game.player.centerY(), 125));
			}
			if (dashTick >= dashDuration) {
				useDash = false;
				dashTick = 0;
				Game.player.speed = Game.player.maxSpeed;
			}
		}
	}
	
	public void powerMove() {
		int manaCost = 34;
		if (availablePowerMove && Game.player.mana >= manaCost && !usePowerMove) {
			sound2.PlaySound();
			usePowerMove = true;
			Game.player.mana -= manaCost;
		}
		if (usePowerMove) {
			powerMoveTick++;
			double deltaX = Game.mx / Game.scale - Game.player.centerX() + Camera.getX();
			double deltaY = Game.my / Game.scale - Game.player.centerY() + Camera.getY();
			double mag = getMagnitude(deltaX, deltaY);
			if (powerMoveTick % 4 == 0) {
				Game.entities.add(new AE_Fire2(Game.player.centerX(), Game.player.centerY(), deltaX / mag, deltaY / mag,powerMoveDmg));
			}
			if (powerMoveTick >= powerMoveDuration) {
				powerMoveTick = 0;
				usePowerMove = false;
				sound2.StopSound();
			}
		}
	}
	
	public void specialMove() {
		int manaCost = 68;
		
		if (availableSpecialMove && Game.player.mana >= manaCost && !useSpecialMove) {
			sound3.PlaySound();
			useSpecialMove = true;
			Game.player.mana -= manaCost;
		}
		if(useSpecialMove){
			double deltaX = Game.mx / Game.scale - Game.player.centerX() + Camera.getX();
			double deltaY = Game.my / Game.scale - Game.player.centerY() + Camera.getY();
			double mag = Math.hypot(deltaX, deltaY);
			Game.entities.add(new AE_HellFlame(Game.player.centerX(), Game.player.centerY(), specialMoveSpd, deltaX / mag, deltaY / mag, specialMoveDmg));
			useSpecialMove = false;
		}	
	}
}
