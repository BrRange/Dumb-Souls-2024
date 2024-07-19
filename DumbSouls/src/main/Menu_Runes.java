package main;

import entities.Player;
import java.awt.Color;
import java.awt.Font;

public class Menu_Runes {
	
	private static int cur = 0, curR = 0;
	private static String[] options = {"Runes", "Equip", "Unequip", "Delete", "+Limit", "Back"};
	private static boolean clickR, clickL;
	
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
		
		if (options[cur] == "Runes") {
			if (Game.keyController.contains(68) || Game.keyController.contains(39) && !Player.runesInventory.isEmpty()) {//A LEFT
				clickR = true;
				clickL = false;
				curR++;
				if (curR > Player.runesInventory.size() - 1) curR = 0;
			}
			if (Game.keyController.contains(65) || Game.keyController.contains(37) && !Player.runesInventory.isEmpty()) {//D RIGHT
				clickR = false;
				clickL = true;
				curR--;
				if (curR < 0) curR = Player.runesInventory.size() - 1;
			}
		}
		
		if(options[cur] == "Equip") {
			if (enter && !Player.runesInventory.isEmpty()) {
				if (Game.player.runesEquipped.size() < Player.runeLimit ) {
					if (!Player.runesInventory.get(curR).equipped) {
						Game.player.runesEquipped.add(Player.runesInventory.get(curR));
						Player.runesInventory.get(curR).equipped = true;
					}
				}
			}	
		}
		
		if(options[cur] == "Unequip") {
			if (enter && !Player.runesInventory.isEmpty()) {
				Game.player.runesEquipped.remove(Player.runesInventory.get(curR));
				Player.runesInventory.get(curR).equipped = false;
			}	
		}
		
		if (options[cur] == "Delete") {
			if (enter && !Player.runesInventory.isEmpty()) {
				if (Player.runesInventory.size() > 0) {
					Player.runesInventory.remove(Player.runesInventory.get(curR));
				}
			}	
		}
		
		if (options[cur] == "+Limit") {
			if (enter) {
				if (Player.souls >= 5000) {
					Player.runeLimit ++;
					Player.souls -= 5000;
				}
			}	
		}
		
		if (options[cur] == "Back") {
			if (enter) {
				Game.gameStateHandler = Game.gameState.MENUPLAYER;
			}	
		}

		Game.keyController.clear();
	}
	
	private static void renderSprites() {
		Game.gameGraphics.setColor(new Color(50, 50, 50));
		Game.gameGraphics.fillRect(200, 54, 32, 32);
		
		Game.gameGraphics.drawImage(Player.runesInventory.get(curR).sprite, 200, 54, 32, 32, null);
		
		if (Player.runesInventory.get(curR).equipped) {
			Game.gameGraphics.setColor(new Color(0, 127, 14));
			Game.gameGraphics.drawString("Equipped", 193, 100);
		}
		else {
			Game.gameGraphics.setColor(new Color(127, 0, 0));
			Game.gameGraphics.drawString("Unequipped", 193, 100);
		}
		
		Game.gameGraphics.setColor(Color.white);
		Game.gameGraphics.setFont(new Font("arial", Font.BOLD, 9));
		Game.gameGraphics.drawString(Player.runesInventory.get(curR).description, 145, 113);
	}
	
	public static void render() {
		Game.gameGraphics.setColor(Color.black);
		Game.gameGraphics.fillRect(0, 0, Game.width, Game.height);
		
		Game.gameGraphics.setColor(Color.white);
		Game.gameGraphics.setFont(new Font("arial", Font.BOLD, 15));
		Game.gameGraphics.drawString("Player Configuration/ Runes", 90, 20);
		
		Game.gameGraphics.setFont(new Font("arial", Font.BOLD, 9));

		if(!Player.runesInventory.isEmpty()){
			Game.gameGraphics.drawString(Player.runesInventory.get(curR).name, 30, 40);
			renderSprites();
		}
		Game.gameGraphics.drawString("Equip", 30, 60);
		Game.gameGraphics.drawString("Unequip", 30, 80);
		Game.gameGraphics.drawString("Delete", 30, 100);
		Game.gameGraphics.drawString("+Limit", 30, 120);
		Game.gameGraphics.drawString("Back", 30, 140);
		
		if (cur == 0) {
			Game.gameGraphics.drawString("<", 20, 40);
			Game.gameGraphics.drawString(">", 100, 40);
			if (clickR) {
				Game.gameGraphics.setColor(Color.red);
				Game.gameGraphics.drawString(">", 100, 40);
			}
			if (clickL) {
				Game.gameGraphics.setColor(Color.red);
				Game.gameGraphics.drawString("<", 20, 40);
			}
		}
		else if (cur == 1) {
			Game.gameGraphics.drawString(">", 20, 60);
		}
		else if (cur == 2) {
			Game.gameGraphics.drawString(">", 20, 80);
		}
		else if (cur == 3) {
			Game.gameGraphics.drawString(">", 20, 100);
		}
		else if (cur == 4) {
			Game.gameGraphics.drawString(">", 20, 120);
			Game.gameGraphics.setColor(new Color(74, 52, 160));
			Game.gameGraphics.drawString("-5000 souls", 60, 120);
		}
		else if (cur == 5) {
			Game.gameGraphics.drawString(">", 20, 140);
		}
		
		Game.gameGraphics.setColor(new Color(0, 127, 0));
		Game.gameGraphics.setFont(new Font("arial", Font.BOLD, 9));
		Game.gameGraphics.drawString("Rune Limit: " + Game.player.runesEquipped.size() + "/" + Player.runeLimit, 20, 150);
		
		Game.gameGraphics.setColor(new Color(74, 52, 160));
		Game.gameGraphics.drawString("Souls : " + Player.souls, 255, 150);
	}
}
