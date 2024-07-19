package main;

import java.awt.Color;
import java.awt.Font;
import entities.*;
import entities.weapons.*;

public class Menu_Player {
	private static int cur, curW, weaponCost;
	private static String[] weapons = {"Mana Weapon", "Fire Weapon", "Wind Weapon", "Ice Weapon", "Fisical Weapon", "Poison Weapon"}, options = {"Books", "Start", "Runes", "Back"};
	private static boolean clickR, clickL, weaponBlock;
	
	public static void tick() {
		boolean enter = Game.keyController.contains(10);
		if (Game.keyController.contains(87) || Game.keyController.contains(38)) {//W UP
			cur --;
			if (cur < 0) cur = options.length - 1;
		}
		if (Game.keyController.contains(83) || Game.keyController.contains(40)) {//S DOWN
			cur ++;
			if (cur > options.length - 1) cur = 0;
		}
		
		if (options[cur] == "Books") {
			if (Game.keyController.contains(68) || Game.keyController.contains(39)) {//D RIGHT
				clickR = true;
				clickL = false;
				curW++;
				if (curW > weapons.length - 1) curW = 0;
			}
			if (Game.keyController.contains(65) || Game.keyController.contains(37)) {//A LEFT
				clickR = false;
				clickL = true;
				curW--;
				if (curW < 0) curW = weapons.length - 1;
			}
			costWeapon();
		}
		
		if (options[cur] == "Start") {
			clickR = false;
			clickL = false;
			if (enter) {
				if (costPossible() || isWeaponBlock()) {
					if (!isWeaponBlock()) Player.souls -= weaponCost;
					Game.gameStateHandler = Game.gameState.NORMAL;
					weaponVerification();
				}
			}
		}
		
		if (options[cur] == "Runes") {
			clickR = false;
			clickL = false;
			if (enter)
				Game.gameStateHandler = Game.gameState.MENURUNES;
		}
		
		if (options[cur] == "Back") {
			if (enter) {
				Game.gameStateHandler = Game.gameState.MENUINIT;
			}	
		}
		Game.keyController.clear();
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
		switch(weapons[curW]){
			case "Mana Weapon": 
				Game.player.playerWeapon = new Mana_Weapon();
				Mana_Weapon.block = false;
				break;
			case "Fire Weapon":
				Game.player.playerWeapon = new Fire_Weapon();
				Fire_Weapon.block = false;
				break;
			case "Wind Weapon":
				Game.player.playerWeapon = new Wind_Weapon();
				Wind_Weapon.block = false;
				break;
			case "Ice Weapon":
				Game.player.playerWeapon = new Ice_Weapon();
				Ice_Weapon.block = false;
				break;
			case "Fisical Weapon":
				Game.player.playerWeapon = new Fisical_Weapon();
				Fisical_Weapon.block = false;
				break;
			case "Poison Weapon":
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
		
		Game.gameGraphics.setFont(new Font("arial", Font.BOLD, 9));
		Game.gameGraphics.drawString(weapons[curW], 30, 70);
		Game.gameGraphics.drawString("Start", 30, 90);
		Game.gameGraphics.drawString("Runes", 30, 110);
		Game.gameGraphics.drawString("Back", 30, 130);
		
		if (cur == 0) {
			Game.gameGraphics.drawString("<", 20, 70);
			Game.gameGraphics.drawString(">", 105, 70);
			if (clickR) {
				Game.gameGraphics.setColor(Color.red);
				Game.gameGraphics.drawString(">", 105, 70);
			}
			if (clickL) {
				Game.gameGraphics.setColor(Color.red);
				Game.gameGraphics.drawString("<", 20, 70);
			}
		}
		else {
			Game.gameGraphics.drawString(">", 20, 70 + 20 * cur);
		}
		
		if (costPossible()) {
			Game.gameGraphics.setColor(new Color(0, 127, 14));
		}
		else {
			Game.gameGraphics.setColor(new Color(127, 0, 0));
		}
		
		if (isWeaponBlock()) {
			Game.gameGraphics.setColor(new Color(0, 127, 14));
			Game.gameGraphics.drawString("Unlocked", 194, 100);
		}
		else {
			Game.gameGraphics.setColor(new Color(127, 0, 0));
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
