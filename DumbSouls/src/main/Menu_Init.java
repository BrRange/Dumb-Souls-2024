package main;

import java.awt.Color;
import java.awt.Font;

import graphics.TextObject;

public class Menu_Init {
	private static int cur = 0;
	private static TextObject
	newGame = new TextObject("arial", Font.BOLD, 10, "New Game", 120, 60, Color.white),
	help = new TextObject("arial", Font.BOLD, 10, "Help", 120, 85, Color.white),
	exit = new TextObject("arial", Font.BOLD, 10, "Exit", 120, 110, Color.white);
	
	public static void tick() {
		if(newGame.isColliding(Game.mx, Game.my)){
			cur = 0;
			if(Game.clickController.contains(1)){
				Game.gameStateHandler = Game.gameState.MENUPLAYER;
			}
		}
		if(help.isColliding(Game.mx, Game.my)){
			cur = 1;
			if(Game.clickController.contains(1)){
				Game.gameStateHandler = Game.gameState.MENUHELP;
				cur = 0;
			}
		}
		if(exit.isColliding(Game.mx, Game.my)){
			cur = 2;
			if(Game.clickController.contains(1)){
				System.exit(1);
			}
		}
		if (Game.keyController.contains(83) || Game.keyController.contains(40)) {//S DOWN
			cur++;
			if (cur > 2) cur = 0;
		}
		if (Game.keyController.contains(87) || Game.keyController.contains(38)) {//W UP
			cur--;
			if (cur < 0) cur = 2;
		}
		Game.keyController.clear();
		Game.clickController.clear();
	}
	
	public static void render() {
		Game.gameGraphics.setColor(Color.black); 
		Game.gameGraphics.fillRect(0, 0, Game.width, Game.height);
		Game.gameGraphics.setColor(Color.white);
		Game.gameGraphics.setFont(new Font("arial", Font.BOLD, 30));
		Game.gameGraphics.drawString("Dumb Souls", 70, 30);
		
		newGame.render();
		help.render();
		exit.render();

		if (cur == 0) {
			Game.gameGraphics.drawString(">", 90, 60);
			if (Game.keyController.contains(10)){
				Game.keyController.clear();
				Game.gameStateHandler = Game.gameState.MENUPLAYER;
			}
		}
		else if (cur == 1) {
			Game.gameGraphics.drawString(">", 90, 85);
			if (Game.keyController.contains(10)){
				Game.keyController.clear();
				Game.gameStateHandler = Game.gameState.MENUHELP;
			}
		}
		else if (cur == 2) {
			Game.gameGraphics.drawString(">", 90, 110);
			if (Game.keyController.contains(10)){
				System.exit(1);
			}
		}
	}
}
