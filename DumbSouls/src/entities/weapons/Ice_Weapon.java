package entities.weapons;

import java.awt.image.BufferedImage;
import main.Game;
import world.Camera;
import entities.shots.Shot;
import entities.AE.*;
import entities.enemies.*;
import entities.*;

public class Ice_Weapon extends Weapon{
	
	public BufferedImage shotFace;
	public static BufferedImage sprite = Game.sheet.getSprite(64, 48, 16, 16);
	private int shotDamage = 3, shotSpeed = 2, ablt2Dmg = 4, ablt2Quant = 3, ablt3Dmg = 5, dashTime = 300, dt = 0;
	private double ablt3Spd = 0.5;
	private static double frost = 5;
	public static int soulCost = 400;
	 public static boolean block = true;
	
	public Ice_Weapon() {
		super(sprite);
		shotFace = Game.sheet.getSprite(128, 48, 16, 16);
		super.setAttackTimer(6);

		setOptionsNames(9);
		this.getAnimation(80, 48, 16, 16, 3);
	}
	
	private void setOptionsNames(int opt) {
		this.listNames = new String[opt];
		this.listNames[0] = "Life Boost";
		this.listNames[1] = "Speed Boost";
		this.listNames[2] = "Max Mana";
		this.listNames[3] = "Mana Recover";
		this.listNames[4] = "Cold Strength";
		this.listNames[5] = "Cold Speed";
		this.listNames[6] = "Ice Dash";
		this.listNames[7] = "Ice Spike";
		this.listNames[8] = "Snow Storm";
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
				if (dashAva == false) {
					dashAva = true;
				}
				else {
					dashTime += 20;
				}
				break;
			case "Ice Spike":
				if (ablt2Ava == false) {
					ablt2Ava = true;
				}
				else {
					ablt2Quant += 1;
					ablt2Dmg += 2;
				}
				break;
			case "Snow Storm":
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
		double ang = Math.atan2(Game.my / Game.scale  - (Game.player.getY() + 8 - Camera.getY()) , Game.mx / Game.scale  - (Game.player.getX() + 8 - Camera.getX()));
		double dx = Math.cos(ang);
		double dy =  Math.sin(ang);
		
		Shot e = new Shot(Game.player.getX(), Game.player.getY(), 3, 3, shotFace, dx, dy, 0, shotDamage, shotSpeed, 35);
		Game.shots.add(e);
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

	public static void IceAffect(Enemy e1, Shot e2) {
		if (Entity.isColiding(e1, e2)) {
			e1.frost = Math.max(frost, e1.frost);
		}
	}
	
	public void Dash() {
		int manaCost = 25;
		if (this.dashAva && Game.player.mana >= manaCost && !md1) {
			md1 = true;
			Game.player.mana -= manaCost;
		}
		if (md1) {
			dt += 1;
			double dashSpeed = Game.player.speed / 3;
			if (dt < dashTime) {
				Game.player.moveX += Math.signum(Game.player.moveX) * dashSpeed;
				Game.player.moveY += Math.signum(Game.player.moveY) * dashSpeed;
				if(dt % 4 == 0) {
					Game.entities.add(new AE_IceDs(Game.player.getX(), Game.player.getY() + 5, 16, 16, null, 60));
				}
			}
			else {
				md1 = false;
				dt = 0;
			}
		}
	}

	public void Ablt2() {
		int manaCost = 50;
		if (ablt2Ava && Game.player.mana >= manaCost && !md2) {
			md2 = true;
			Game.player.mana -= manaCost;
		}
		if (md2) {
			for (int c = 1; c <= ablt2Quant; c++) {
				Game.entities.add(new AE_IceSpike(Game.player.getX() + (13 * c), Game.player.getY(), 6, 16, null, 60, ablt2Dmg));
			}
			for (int c = 1; c <= ablt2Quant; c++) {
				Game.entities.add(new AE_IceSpike(Game.player.getX(), Game.player.getY() + (13 * c), 6, 16, null, 60, ablt2Dmg));
			}
			for (int c = 1; c <= ablt2Quant; c++) {
				Game.entities.add(new AE_IceSpike(Game.player.getX() - (13 * c), Game.player.getY(), 6, 16, null, 60, ablt2Dmg));
			}
			for (int c = 1; c <= ablt2Quant; c++) {
				Game.entities.add(new AE_IceSpike(Game.player.getX(), Game.player.getY() - (13 * c), 6, 16, null, 60, ablt2Dmg));
			}
			for (int c = 1; c <= ablt2Quant; c++) {
				Game.entities.add(new AE_IceSpike(Game.player.getX() + (8 * c), Game.player.getY() + (8 * c), 6, 16, null, 60, ablt2Dmg));
			}
			for (int c = 1; c <= ablt2Quant; c++) {
				Game.entities.add(new AE_IceSpike(Game.player.getX() - (8 * c), Game.player.getY() - (8 * c), 6, 16, null, 60, ablt2Dmg));
			}
			for (int c = 1; c <= ablt2Quant; c++) {
				Game.entities.add(new AE_IceSpike(Game.player.getX() + (8 * c), Game.player.getY() - (8 * c), 6, 16, null, 60, ablt2Dmg));
			}
			for (int c = 1; c <= ablt2Quant; c++) {
				Game.entities.add(new AE_IceSpike(Game.player.getX() - (8 * c), Game.player.getY() + (8 * c), 6, 16, null, 60, ablt2Dmg));
			}
			md2 = false;
		}
	}
	
	public void Ablt3() {
		int manaCost = 60;
		if (ablt3Ava && Game.player.mana >= manaCost && !md3) {
			md3 = true;
			Game.player.mana -= manaCost;
		}
		if (md3) {
			Game.entities.add(new AE_SnowStorm(Game.player.getX() - 16, Game.player.getY() - 16, 32, 32, null, 240, ablt3Spd, ablt3Dmg));
			md3 = false;
		}
	}
}
