package main;

import java.awt.Color;
import java.awt.Font;

import entities.Player;
import entities.runes.Rune;
import entities.weapons.*;
import graphics.elements.TextBox;
import world.Camera;

public class Menu_Player {
	private static int cur, curW, weaponCost;
	private static String[] weapons = { "Mana Weapon", "Fire Weapon", "Wind Weapon", "Ice Weapon", "Fisical Weapon",
			"Poison Weapon" };
	private static boolean clickR, clickL, weaponUnlocked, hasRunes;
	private static TextBox bookBox = new TextBox("arial", Font.BOLD, 9, weapons[0], 30, 70, Color.white),
			startBox = new TextBox("arial", Font.BOLD, 9, "Start", 30, 90, Color.white),
			runesBox = new TextBox("arial", Font.BOLD, 9, "Runes", 30, 110, Color.white),
			backBox = new TextBox("arial", Font.BOLD, 9, "Back", 30, 130, Color.white);

	public static void tick() {
		boolean enter = Game.keyController.contains(10);

		if (bookBox.hover())
			cur = 0;
		if (startBox.hover())
			cur = 1;
		if (runesBox.hover())
			cur = 2;
		if (backBox.hover())
			cur = 3;

		if (Game.keyController.contains(87) || Game.keyController.contains(38)) {// W UP
			cur--;
			if (cur < 0)
				cur = 3;
		}
		if (Game.keyController.contains(83) || Game.keyController.contains(40)) {// S DOWN
			cur++;
			if (cur > 3)
				cur = 0;
		}

		if (cur == 0) {
			if (Game.keyController.contains(68) || Game.keyController.contains(39) || Game.scrollNum > 0) {// D RIGHT
				clickR = true;
				clickL = false;
				curW++;
				if (curW > weapons.length - 1)
					curW = 0;
			}
			if (Game.keyController.contains(65) || Game.keyController.contains(37) || Game.scrollNum < 0) {// A LEFT
				clickR = false;
				clickL = true;
				curW--;
				if (curW < 0)
					curW = weapons.length - 1;
			}
			costWeapon();
		}
		Game.scrollNum = 0;

		if (cur == 1) {
			clickR = false;
			clickL = false;
			if (enter || startBox.click()) {
				if (costPossible() || weaponUnlocked) {
					if (!weaponUnlocked)
						Player.souls -= weaponCost;
					Game.gameStateHandler = Game.gameState.NORMAL;
					Camera.centerPlayer();
					weaponVerification();
					cur = 0;
				}
			}
		}

		if (cur == 2) {
			clickR = false;
			clickL = false;
			if ((enter || runesBox.click()) && hasRunes) {
				Menu_Runes.startMenu();
				Game.gameStateHandler = Game.gameState.MENURUNES;
				cur = 0;
			}
		}

		if (cur == 3) {
			if (enter || backBox.click()) {
				Game.gameStateHandler = Game.gameState.MENUINIT;
				cur = 0;
			}
		}
		Game.keyController.clear();
		Game.clickController.clear();
	}

	private static boolean costPossible() {
		if (Player.souls >= weaponCost) {
			return true;
		} else {
			return false;
		}
	}

	public static void startMenu() {
		costWeapon();
		for (Rune rn : Player.runesInventory) {
			if (rn.collected) {
				hasRunes = true;
				return;
			}
			hasRunes = false;
		}
	}

	private static void weaponVerification() {
		switch (curW) {
			case 0:
				Game.player.playerWeapon = new Weapon_Mana();
				Weapon_Mana.unlocked = true;
				break;
			case 1:
				Game.player.playerWeapon = new Weapon_Fire();
				Weapon_Fire.unlocked = true;
				break;
			case 2:
				Game.player.playerWeapon = new Weapon_Wind();
				Weapon_Wind.unlocked = true;
				break;
			case 3:
				Game.player.playerWeapon = new Weapon_Ice();
				Weapon_Ice.unlocked = true;
				break;
			case 4:
				Game.player.playerWeapon = new Weapon_Fisical();
				Weapon_Fisical.unlocked = true;
				break;
			case 5:
				Game.player.playerWeapon = new Weapon_Poison();
				Weapon_Poison.unlocked = true;
				break;
		}
		Player.runeSetup();
	}

	private static void costWeapon() {
		switch (curW) {
			case 0:
				weaponCost = Weapon_Mana.soulCost;
				weaponUnlocked = Weapon_Mana.unlocked;
				break;
			case 1:
				weaponCost = Weapon_Fire.soulCost;
				weaponUnlocked = Weapon_Fire.unlocked;
				break;
			case 2:
				weaponCost = Weapon_Wind.soulCost;
				weaponUnlocked = Weapon_Wind.unlocked;
				break;
			case 3:
				weaponCost = Weapon_Ice.soulCost;
				weaponUnlocked = Weapon_Ice.unlocked;
				break;
			case 4:
				weaponCost = Weapon_Fisical.soulCost;
				weaponUnlocked = Weapon_Fisical.unlocked;
				break;
			case 5:
				weaponCost = Weapon_Poison.soulCost;
				weaponUnlocked = Weapon_Poison.unlocked;
				break;
		}
	}

	private static void imgWeapon() {
		switch (curW) {
			case 0:
				Game.gameGraphics.drawImage(Weapon_Mana.sprite, 200, 54, 32, 32, null);
				break;
			case 1:
				Game.gameGraphics.drawImage(Weapon_Fire.sprite, 200, 54, 32, 32, null);
				break;
			case 2:
				Game.gameGraphics.drawImage(Weapon_Wind.sprite, 200, 54, 32, 32, null);
				break;
			case 3:
				Game.gameGraphics.drawImage(Weapon_Ice.sprite, 200, 54, 32, 32, null);
				break;
			case 4:
				Game.gameGraphics.drawImage(Weapon_Fisical.sprite, 200, 54, 32, 32, null);
				break;
			case 5:
				Game.gameGraphics.drawImage(Weapon_Poison.sprite, 200, 54, 32, 32, null);
				break;
		}
	}

	public static void render() {
		Game.gameGraphics.setColor(Color.black);
		Game.gameGraphics.fillRect(0, 0, Game.width, Game.height);

		Game.gameGraphics.setColor(Color.white);
		Game.gameGraphics.setFont(new Font("arial", Font.BOLD, 15));
		Game.gameGraphics.drawString("Player Configuration", 90, 20);

		bookBox.render();
		startBox.render();
		runesBox.updateColor(hasRunes ? Color.white : Color.darkGray);
		runesBox.render();
		backBox.render();

		if (cur == 0) {
			bookBox.updateText(weapons[curW]);
			Game.gameGraphics.drawString("<", 20, 70);
			Game.gameGraphics.drawString(">", bookBox.getWidth() + 35, 70);
			if (clickR) {
				Game.gameGraphics.setColor(Color.red);
				Game.gameGraphics.drawString(">", bookBox.getWidth() + 35, 70);
			} else if (clickL) {
				Game.gameGraphics.setColor(Color.red);
				Game.gameGraphics.drawString("<", 20, 70);
			}
		} else {
			Game.gameGraphics.drawString(">", 20, 70 + 20 * cur);
		}

		if (weaponUnlocked) {
			Game.gameGraphics.setColor(new Color(0, 127, 14));
			Game.gameGraphics.drawString("Unlocked", 194, 100);
		} else {
			if (costPossible()) {
				Game.gameGraphics.setColor(new Color(0, 127, 14));
			} else {
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