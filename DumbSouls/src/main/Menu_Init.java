package main;

import java.awt.Color;
import java.awt.Font;

import graphics.elements.OptionArrow;
import graphics.elements.TextBox;

public class Menu_Init {
	private static int cur = 0;
	private static TextBox
	newGameBox = new TextBox("arial", Font.BOLD, 10, "New Game", 120, 60, Color.white),
	helpBox = new TextBox("arial", Font.BOLD, 10, "Help", 120, 85, Color.white),
	exitBox = new TextBox("arial", Font.BOLD, 10, "Exit", 120, 110, Color.white);
	private static OptionArrow arrow = new OptionArrow("arial", Font.BOLD, 10, ">", Color.white, 90, 60, (d) -> d * (2 - d), 20);
	
	public static void tick() {
		if(newGameBox.hover()){
			cur = 0;
		}
		if(newGameBox.click()){
			Game.gameStateHandler = Game.gameState.MENUPLAYER;
		}
		if(helpBox.hover()){
			cur = 1;
		}
		if(helpBox.click()){
			Game.gameStateHandler = Game.gameState.MENUHELP;
			cur = 0;
		}
		if(exitBox.hover()){
			cur = 2;
		}
		if(exitBox.click()){
			System.exit(1);
		}
		if (Game.keyController.contains(83) || Game.keyController.contains(40)) {//S DOWN
			cur++;
			if (cur > 2) cur = 0;
		}
		if (Game.keyController.contains(87) || Game.keyController.contains(38)) {//W UP
			cur--;
			if (cur < 0) cur = 2;
		}
		switch (cur) {
		case 0:
			arrow.setTarget(90, 60);	
			break;
		case 1:
			arrow.setTarget(90, 85);
			break;
		case 2:
			arrow.setTarget(90, 110);
			break;
		}
		arrow.tick();
		if (Game.keyController.contains(10)){
			switch (cur) {
			case 0:
				Game.keyController.clear();
				Menu_Player.startMenu();
				Game.gameStateHandler = Game.gameState.MENUPLAYER;	
				break;
			case 1:
				Game.keyController.clear();
				Game.gameStateHandler = Game.gameState.MENUHELP;;
				break;
			case 2:
				System.exit(1);;
				break;
			}
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
		
		newGameBox.render();
		helpBox.render();
		exitBox.render();
		arrow.render();
	}
}