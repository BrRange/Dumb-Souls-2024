package main;

import java.awt.Color;
import java.awt.Font;

public class Menu_Init {
	private static int cur = 0;
	private static String[] options = {"New Game", "Help", "Exit"};
	
	public static void tick() {
		if (Game.keyController.contains(83) || Game.keyController.contains(40)) {//S DOWN
			cur++;
			if (cur > options.length - 1) cur = 0;
		}
		if (Game.keyController.contains(87) || Game.keyController.contains(38)) {//W UP
			cur--;
			if (cur < 0) cur = options.length - 1;
		}
		if (Game.keyController.contains(10)) {
			if (options[cur] == "New Game") {
				Game.gameStateHandler = Game.gameState.MENUPLAYER;
			}
			else if (options[cur] == "Help") {
				Game.gameStateHandler = Game.gameState.MENUHELP;
			}
			else if (options[cur] == "Exit") {
				System.exit(1);
			}
		}
		Game.keyController.clear();
	}
	
	public static void render() {
		Game.gameGraphics.setColor(Color.black); 
		Game.gameGraphics.fillRect(0, 0, Game.width, Game.height);
		Game.gameGraphics.setColor(Color.white);
		Game.gameGraphics.setFont(new Font("arial", Font.BOLD, 30));
		Game.gameGraphics.drawString("Dumb Souls", 70, 30);
		Game.gameGraphics.setFont(new Font("arial",  Font.BOLD, 10));
		
		Game.gameGraphics.drawString(options[0], 120, 60);
		Game.gameGraphics.drawString(options[1], 120, 85);
		Game.gameGraphics.drawString(options[2], 120, 110);
		
		if (cur == 0) {
			Game.gameGraphics.drawString(">", 90, 60);
		}
		else if (cur == 1) {
			Game.gameGraphics.drawString(">", 90, 85);
		}
		else if (cur == 2) {
			Game.gameGraphics.drawString(">", 90, 110);
		}
	}
}
