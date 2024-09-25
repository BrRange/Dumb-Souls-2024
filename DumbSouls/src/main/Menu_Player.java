package main;

import java.awt.Color;
import java.awt.Font;
import entities.*;
import entities.weapons.*;
import graphics.TextObject;

public class Menu_Player {
	private static int cur, curW, weaponCost;
	private static String[] weapons = {"Mana Weapon", "Fire Weapon", "Wind Weapon", "Ice Weapon", "Fisical Weapon", "Poison Weapon"};
	private static boolean clickR, clickL, weaponBlock;
	private static TextObject
	book = new TextObject("arial", Font.BOLD, 9, weapons[0], 30, 70, Color.white),
	start = new TextObject("arial", Font.BOLD, 9, "Start", 30, 90, Color.white),
	runes = new TextObject("arial", Font.BOLD, 9, "Runes", 30, 110, Color.white),
	back = new TextObject("arial", Font.BOLD, 9, "Back", 30, 130, Color.white);
	
	public static void tick() {
		boolean enter = Game.keyController.contains(10);

		if(book.isColliding(Game.mx, Game.my)) cur = 0;
		if(start.isColliding(Game.mx, Game.my)) cur = 1;
		if(runes.isColliding(Game.mx, Game.my)) cur = 2;
		if(back.isColliding(Game.mx, Game.my)) cur = 3;

		if (Game.keyController.contains(87) || Game.keyController.contains(38)) {//W UP
			cur --;
			if (cur < 0) cur = 3;
		}
		if (Game.keyController.contains(83) || Game.keyController.contains(40)) {//S DOWN
			cur ++;
			if (cur > 3) cur = 0;
		}
		
		if (cur == 0) {
			if (Game.keyController.contains(68) || Game.keyController.contains(39) || Game.scrollNum > 0) {//D RIGHT
				clickR = true;
				clickL = false;
				curW++;
				if (curW > weapons.length - 1) curW = 0;
			}
			if (Game.keyController.contains(65) || Game.keyController.contains(37) || Game.scrollNum < 0) {//A LEFT
				clickR = false;
				clickL = true;
				curW--;
				if (curW < 0) curW = weapons.length - 1;
			}
			costWeapon();
		}
		Game.scrollNum = 0;
		
		if (cur == 1) {
			clickR = false;
			clickL = false;
			if (enter || (start.isColliding(Game.mx, Game.my) && Game.clickController.contains(1))) {
				if (costPossible() || isWeaponBlock()) {
					if (!isWeaponBlock()) Player.souls -= weaponCost;
					Game.gameStateHandler = Game.gameState.NORMAL;
					weaponVerification();
					cur = 0;
				}
			}
		}
		
		if (cur == 2) {
			clickR = false;
			clickL = false;
			if (enter || (runes.isColliding(Game.mx, Game.my) && Game.clickController.contains(1))){
				Game.gameStateHandler = Game.gameState.MENURUNES;
				cur = 0;
			}
		}
		
		if (cur == 3) {
			if (enter || (back.isColliding(Game.mx, Game.my) && Game.clickController.contains(1))) {
				Game.gameStateHandler = Game.gameState.MENUINIT;
				cur = 0;
			}	
		}
		Game.keyController.clear();
		Game.clickController.clear();
	}
	
	private static boolean isWeaponBlock() {
		if (weaponBlock == false) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private static boolean costPossible() {
		if (Player.souls >= weaponCost) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private static void weaponVerification() {
		switch(curW){
			case 0: 
				Game.player.playerWeapon = new Mana_Weapon();
				Mana_Weapon.block = false;
				break;
			case 1:
				Game.player.playerWeapon = new Fire_Weapon();
				Fire_Weapon.block = false;
				break;
			case 2:
				Game.player.playerWeapon = new Wind_Weapon();
				Wind_Weapon.block = false;
				break;
			case 3:
				Game.player.playerWeapon = new Ice_Weapon();
				Ice_Weapon.block = false;
				break;
			case 4:
				Game.player.playerWeapon = new Fisical_Weapon();
				Fisical_Weapon.block = false;
				break;
			case 5:
				Game.player.playerWeapon = new Poison_Weapon();
				Poison_Weapon.block = false;
				break;
		}
	}
	
	private static void costWeapon() {
		switch(curW) {
		case 0:
			weaponCost = Mana_Weapon.soulCost;
			weaponBlock = Mana_Weapon.block;
			break;
		case 1:
			weaponCost = Fire_Weapon.soulCost;
			weaponBlock = Fire_Weapon.block;
			break;
		case 2:
			weaponCost = Wind_Weapon.soulCost;
			weaponBlock = Wind_Weapon.block;
			break;
		case 3:
			weaponCost = Ice_Weapon.soulCost;
			weaponBlock = Ice_Weapon.block;
			break;
		case 4:
			weaponCost = Fisical_Weapon.soulCost;
			weaponBlock = Fisical_Weapon.block;
			break;
		case 5:
			weaponCost = Poison_Weapon.soulCost;
			weaponBlock = Poison_Weapon.block;
			break;
		}
	}
	
	private static void imgWeapon() {
		switch(curW){
		case 0: 
			Game.gameGraphics.drawImage(Mana_Weapon.sprite, 200, 54, 32, 32, null);
			break;
		case 1:
			Game.gameGraphics.drawImage(Fire_Weapon.sprite, 200, 54, 32, 32, null);
			break;
		case 2:
			Game.gameGraphics.drawImage(Wind_Weapon.sprite, 200, 54, 32, 32, null);
			break;
		case 3:
			Game.gameGraphics.drawImage(Ice_Weapon.sprite, 200, 54, 32, 32, null);
			break;
		case 4:
			Game.gameGraphics.drawImage(Fisical_Weapon.sprite, 200, 54, 32, 32, null);
			break;
		case 5:
			Game.gameGraphics.drawImage(Poison_Weapon.sprite, 200, 54, 32, 32, null);
			break;
		}
	}
	
	public static void render() {
		Game.gameGraphics.setColor(Color.black);
		Game.gameGraphics.fillRect(0, 0, Game.width, Game.height);
		
		Game.gameGraphics.setColor(Color.white);
		Game.gameGraphics.setFont(new Font("arial", Font.BOLD, 15));
		Game.gameGraphics.drawString("Player Configuration", 90, 20);
		
		book.render();
		start.render();
		runes.render();
		back.render();
		
		if (cur == 0) {
			book.updateText(weapons[curW]);
			if (clickR) {
				Game.gameGraphics.drawString("<", 20, 70);
				Game.gameGraphics.setColor(Color.red);
				Game.gameGraphics.drawString(">", 105, 70);
			}
			else if (clickL) {
				Game.gameGraphics.drawString(">", 105, 70);
				Game.gameGraphics.setColor(Color.red);
				Game.gameGraphics.drawString("<", 20, 70);
			}
			else{
				Game.gameGraphics.drawString("<", 20, 70);
				Game.gameGraphics.drawString(">", 105, 70);
			}
		}
		else {
			Game.gameGraphics.drawString(">", 20, 70 + 20 * cur);
		}
		
		if (isWeaponBlock()) {
			Game.gameGraphics.setColor(new Color(0, 127, 14));
			Game.gameGraphics.drawString("Unlocked", 194, 100);
		}
		else {
			if (costPossible()) {
				Game.gameGraphics.setColor(new Color(0, 127, 14));
			}
			else {
				Game.gameGraphics.setColor(new Color(127, 0, 0));
			}
			Game.gameGraphics.drawString("Locked", 183, 110);
			Game.gameGraphics.drawString("Soul Cost: " + weaponCost, 183, 100);
		}
		
		Game.gameGraphics.setColor(new Color(74, 52, 160));
		Game.gameGraphics.drawString("Souls : " + Player.souls, 255, 150);
		
		Game.gameGraphics.setColor(new Color(50, 50, 50));
		Game.gameGraphics.fillRect(200, 54, 32, 32);
		
		imgWeapon();
	}
}
