package entities.weapons;

import java.awt.image.BufferedImage;
import main.Game;
import world.Camera;
import entities.shots.Shot_PlayerIce;
import entities.Player;
import entities.AE.AE_IceDs;
import entities.AE.AE_IceSpike;
import entities.AE.AE_SnowStorm;

public class Weapon_Ice extends Weapon{
	
	public static BufferedImage sprite = Game.sheet.getSprite(64, 48, 16, 16);
	private int powerMoveDmg = 4, powerMoveAmount = 3, specialMoveDmg = 5;
	private double specialMoveSpd = 0.5;
	private static double frost = 5;
	public static int soulCost = 400;
	 public static boolean unlocked = false;
	
	public Weapon_Ice() {
		setAttackTimer(6);
		setOptionsNames(9);
		getAnimation(80, 48, 16, 16, 3);
		dashDuration = 150;
		shotDamage = 3;
		shotSpeed = 2;
	}
	
	private void setOptionsNames(int opt) {
		listNames = new String[opt];
		listNames[0] = "Life Boost";
		listNames[1] = "Speed Boost";
		listNames[2] = "Max Mana";
		listNames[3] = "Mana Recover";
		listNames[4] = "Cold Strength";
		listNames[5] = "Cold Speed";
		listNames[6] = "Ice Dash";
		listNames[7] = "Ice Spike";
		listNames[8] = "Snow Storm";
	}
	
	public void checkOptions(String opt) {
		switch(opt){
			case "Life Boost":
				Game.player.maxLife += 30;
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
			case "Cold Strength":
				shotDamage += 1;
				frost += 1;
				break;
			case "Cold Speed":
				shotSpeed += 1;
				frost += 0.5;
				break;
			case "Ice Dash":
				if (availableDash == false) {
					availableDash = true;
				}
				else {
					dashDuration += 20;
				}
				break;
			case "Ice Spike":
				if (availablePowerMove == false) {
					availablePowerMove = true;
				}
				else {
					powerMoveAmount += 1;
					powerMoveDmg += 2;
				}
				break;
			case "Snow Storm":
				if (availableSpecialMove == false) {
					availableSpecialMove = true;
				}
				else {
					specialMoveDmg += 2;
					specialMoveSpd += 0.3;
				}
				break;
			default:
				Player.souls += 200;
		}
	}
	
	public void Attack() {
		double ang = Math.atan2(Game.my / Game.scale - (Game.player.centerY() - Camera.getY()) , Game.mx / Game.scale - Game.player.centerX() + Camera.getX());
		double dx = Math.cos(ang);
		double dy =  Math.sin(ang);
		
		Game.shots.add(new Shot_PlayerIce(Game.player.centerX(), Game.player.centerY(), dx, dy, shotSpeed, shotDamage, frost));
	}
	
	public void Dash() {
		int manaCost = 25;
		if (availableDash && Game.player.mana >= manaCost && !useDash) {
			useDash = true;
			Game.player.mana -= manaCost;
		}
		if (useDash) {
			dashTick ++;
			double dashSpeed = Game.player.speed / 3;
			if (dashTick < dashDuration) {
				Game.player.speedBoost += dashSpeed;
				if(dashTick % 4 == 0) {
					Game.entities.add(new AE_IceDs(Game.player.centerX(), Game.player.centerY()));
				}
			}
			else {
				useDash = false;
				dashTick = 0;
			}
		}
	}

	public void powerMove() {
		int manaCost = 50;
		if (availablePowerMove && Game.player.mana >= manaCost && !usePowerMove) {
			usePowerMove = true;
			Game.player.mana -= manaCost;
		}
		if (usePowerMove) {
			for (int c = 0; c < 8; c++) {
				for (int i = 1; i <= powerMoveAmount; i++) {
					Game.entities.add(new AE_IceSpike((int)(Game.player.pos.getX() + i * 14 * Math.cos(c * Math.PI / 4)), (int)(Game.player.centerY() + i * 14 * Math.sin(c * Math.PI / 4)), powerMoveDmg));
				}
			}
			usePowerMove = false;
		}
	}
	
	public void specialMove() {
		int manaCost = 60;
		if (availableSpecialMove && Game.player.mana >= manaCost && !useSpecialMove) {
			useSpecialMove = true;
			Game.player.mana -= manaCost;
		}
		if (useSpecialMove) {
			Game.entities.add(new AE_SnowStorm(Game.player.centerX(), Game.player.centerY(), specialMoveSpd, specialMoveDmg));
			useSpecialMove = false;
		}
	}
}
