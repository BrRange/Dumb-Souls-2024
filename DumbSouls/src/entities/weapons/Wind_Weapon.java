package entities.weapons;

import java.awt.image.BufferedImage;
import main.Game;
import world.Camera;
import sounds.SoundPlayer;
import entities.shots.Shot;
import entities.AE.*;

public class Wind_Weapon extends Weapon {
	
	public static BufferedImage shotFace;
	public static BufferedImage sprite = Game.sheet.getSprite(64, 32, 16, 16);
	private int shotDamage = 3, shotSpeed = 6, hrcDamage = 1, ablt3Dmg = 6;
	private double hrcSpeed = 0.8, ablt3Spd = 5.0;
	private short di, dashDistance = 30;
	public static int soulCost = 100;
	public static boolean block = true;
	private SoundPlayer sound1, sound2;
	
	public Wind_Weapon() {
		super(sprite);
		shotFace = Game.sheet.getSprite(128, 32, 16, 16);
		super.setAttackTimer(2);
		Game.player.push = 5;
		sound1 = new SoundPlayer("res/sounds/wind_atk.wav");
		sound2 = new SoundPlayer("res/sounds/wind_ablt2.wav");
		setOptionsNames(9);
		this.getAnimation(80, 32, 16, 16, 3);
	}
	
	private void setOptionsNames(int opt) {
		this.listNames = new String[opt];
		this.listNames[0] = "Life Boost";
		this.listNames[1] = "Speed Boost";
		this.listNames[2] = "Max Mana";
		this.listNames[3] = "Mana Recover";
		this.listNames[4] = "Wind Strength";
		this.listNames[5] = "Wind Speed";
		this.listNames[6] = "Wind Dash";
		this.listNames[7] = "Hurricane";
		this.listNames[8] = "Wind Barrage";
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
			if (dashAva == false) {
				dashAva = true;
			}
			else {
				dashDistance += 3;
			}
			break;
		case "Hurricane":
			if (ablt2Ava == false) {
				ablt2Ava = true;
			}
			else {
				hrcSpeed += 0.1;
				hrcDamage += 1;
			}
			break;
		case "Wind Barrage":
			if (ablt3Ava == false) {
				ablt3Ava = true;
			}
			else {
				ablt3Dmg += 2;
				ablt3Spd += 0.3;
			}
			break;
		}
	}
	
	public void Attack() {
		sound1.PlaySound();
		double ang = Math.atan2(Game.my / Game.scale - (Game.player.getY() + 8 - Camera.getY()) , Game.mx / Game.scale  - (Game.player.getX() + 8 - Camera.getX()));
		double dx = Math.cos(ang);
		double dy =  Math.sin(ang);
		
		Game.shots.add(new Shot(Game.player.getX(), Game.player.getY(), 3, 6, shotFace, dx, dy, ang, shotDamage, shotSpeed, 35));
	}
	
	public void AttackRandom() {
		int xdir = Game.rand.nextInt(1);
		int ydir = Game.rand.nextInt(1);
		
		int xoff = Game.rand.nextInt(20);
		int yoff = Game.rand.nextInt(20);
		
		if (xdir == 1) {
			xoff *= -1;
		}
		
		if (ydir == 1) {
			yoff *= -1;
		}
		
		double ang = Math.atan2(Game.my / Game.scale  + yoff - (Game.player.getY() + 8 - Camera.getY()) , Game.mx / Game.scale  + xoff - (Game.player.getX() + 8 - Camera.getX()));
		double dx = Math.cos(ang);
		double dy =  Math.sin(ang);
		
		Game.shots.add(new Shot(Game.player.getX(), Game.player.getY(), 3, 3, shotFace, dx, dy, ang, shotDamage, shotSpeed, 35));
	}
	
	public void Dash() {
		int manaCost = 20;
		if (this.dashAva && Game.player.mana >= manaCost && !md1) {
			md1 = true;
			Game.player.mana -= manaCost;
		}
		if(md1) {
			Game.entities.add(new AE_WindDS(Game.player.getX(), Game.player.getY(), 16, 16, null, (int)(dashDistance / 4)));
			di += 1;
			if (di < dashDistance) {
				Game.player.speedBoost *= 4;
			}
			else {
				di = 0;
				md1 = false;
			}
		}
	}
	
	public void Ablt2() {
		int manaCost = 64;
		if (ablt2Ava && Game.player.mana >= manaCost && !md2) {
			md2 = true;
			Game.player.mana -= manaCost;
		}
		if (md2) {
			md2 = false;
			Game.entities.add(new AE_Hurricane(Game.player.getX() - 16, Game.player.getY() - 16, 32, 32, null, 240, hrcSpeed, hrcDamage));
		}
	}
	
	public void Ablt3() {
		int manaCost = 36;
		if (ablt3Ava && Game.player.mana >= manaCost && !md3) {
			sound2.PlaySound();
			md3 = true;
			Game.player.mana -= manaCost;
		}
		if (md3) {
			double ang = Math.atan2(Game.my / Game.scale - (Game.player.getY() + 8 - Camera.getY()) , Game.mx / Game.scale - (Game.player.getX() + 8 - Camera.getX()));
			double dx = Math.cos(ang);
			double dy =  Math.sin(ang);
			Game.entities.add(new AE_WindBarrage(Game.player.getX(), Game.player.getY(), 32, 32, ablt3Spd, dx, dy, ablt3Dmg, null, 30));
			md3 = false;
		}
	}
}
