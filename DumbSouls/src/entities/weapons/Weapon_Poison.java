package entities.weapons;

import java.awt.image.BufferedImage;
import main.Game;
import world.Camera;
import entities.Player;
import entities.AE.AE_PoisonDs;
import entities.AE.AE_PoisonPool;
import entities.AE.AE_VenomGas;
import entities.shots.Shot_PlayerPoison;

public class Weapon_Poison extends Weapon{
    
    public static BufferedImage sprite = Game.sheet.getSprite(144, 32, 16, 16);
    private static int poisonPoolSize = 32, poisonPoolD = 1;
    private int ablt2S = 64, ablt2D = 5;
    private int dashSize = 18, dashDamage = 5;
    private int tspw, maxTspw = 180;
    private double ablt3D = 0.05;
    public static int soulCost = 700;
    public static boolean unlocked = false;
	
    public Weapon_Poison() {
		setAttackTimer(30);
        setOptionsNames(9);
        getAnimation(160, 32, 16, 16, 3);
		dashDuration = 120;
    }
	
    private void setOptionsNames(int opt) {
        listNames = new String[opt];
		listNames[0] = "Life Boost";
		listNames[1] = "Speed Boost";
		listNames[2] = "Max Mana";
		listNames[3] = "Mana Recover";
		listNames[4] = "Poison Strength";
		listNames[5] = "Poison Area";
		listNames[6] = "Poison Barrier";
		listNames[7] = "Venom Pool";
		listNames[8] = "Venom Gas";
    }

    public void checkOptions(String opt) {
        switch(opt) {
            case "Life Boost":
                Game.player.maxLife += 30;
                break;
            case "Speed Boost":
                Game.player.maxSpeed += 0.2;
                Game.player.speed = Game.player.maxSpeed;
                break;
            case "Max Mana":
                Game.player.maxMana += 15;
                break;
            case "Mana Recover":
                    Game.player.manaRec += 0.75;
                break;
            case "Poison Strength":
            		poisonPoolD += 1;
            	break;
            case "Poison Area":
            		poisonPoolSize += 8;
            	break;
            case "Poison Barrier":
            	if (availableDash) {
            		dashSize += 2;
            		dashDamage += 2;
            		dashDuration += 30;
            	}
            	else {
            		availableDash = true;
            	}
            	break;
            case "Venom Pool":
            	if (availablePowerMove) {
            		ablt2S += 16;
            		ablt2D += 1;
            	}
            	else {
            		availablePowerMove = true;
            	}
            	break;
            case "Venom Gas":
            	if (availableSpecialMove) {
            		 maxTspw += 20;
                	 ablt3D += 0.03;
            	}
            	else {
            		availableSpecialMove = true;
            	}
            	break;
			default:
				Player.souls += 200;
        }
    }

    public void Attack() {
		double ang = Math.atan2(Game.my / Game.scale  - (Game.player.centerY() - Camera.getY()) , Game.mx / Game.scale  - Game.player.centerX() + Camera.getX());
		double dx = Math.cos(ang);
		double dy =  Math.sin(ang);
		
		Game.shots.add(new Shot_PlayerPoison(Game.player.centerX(), Game.player.centerY(), dx, dy, ang, poisonPoolD, poisonPoolSize));
	}
    
    public void Dash() {
		int manaCost = 30;
		
		if (availableDash && Game.player.mana >= manaCost && !useDash) {
			useDash = true;
			Game.player.mana -= manaCost;
		}
		if (useDash) {
			Game.entities.add(new AE_PoisonDs(Game.player.centerX(), Game.player.centerY(), dashSize, dashDuration, dashDamage));
			useDash = false;
        }
    }

    public void powerMove() {
		int manaCost = 36;
		
		if (availablePowerMove && Game.player.mana >= manaCost && !usePowerMove) {
			usePowerMove = true;
			Game.player.mana -= manaCost;
		}
		if (usePowerMove) {
			Game.entities.add(new AE_PoisonPool(Game.player.centerX(), Game.player.centerY(), ablt2S, 150, ablt2D));
			usePowerMove = false;
        }
    }

    public void specialMove() {
		int manaCost = 54;
		
		if (availableSpecialMove && Game.player.mana >= manaCost) {
			if (!useSpecialMove) {
				useSpecialMove = true;
				Game.player.mana -= manaCost;
			}
		}
		if (useSpecialMove) {
			tspw++;
			int off = Game.rand.nextInt(40);
			int off2 = Game.rand.nextInt(1);
			
			if (off2 == 0) {
				off *= -1;
			}
		
			
			if (tspw % 2 == 0) {
				double deltaX = Game.mx / Game.scale + off - Game.player.centerX() + Camera.getX();
				double deltaY = Game.my / Game.scale + off - Game.player.centerY() + Camera.getY();
				double mag = Math.hypot(deltaX, deltaY);
				Game.entities.add(new AE_VenomGas(Game.player.centerX(), Game.player.centerY(), deltaX / mag, deltaY / mag, ablt3D));
			}
			if (tspw == maxTspw) {
				tspw = 0;
				useSpecialMove = false;
			}
		}
    }
}
