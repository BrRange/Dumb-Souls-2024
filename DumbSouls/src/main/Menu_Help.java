package main;
import java.awt.Color;
import java.awt.Font;

import graphics.elements.TextBox;

public class Menu_Help {
	
	private static TextBox
	backBox = new TextBox("arial", Font.BOLD, 10, "> Back", 10, 140, Color.white);

	public static void tick() {
		backBox.hover();
		if (Game.keyController.contains(10) || backBox.click()){
			Game.gameStateHandler = Game.gameState.MENUINIT;
		}
		Game.keyController.clear();
		Game.clickController.clear();
	}
	
	public static void render() {
		Game.gameGraphics.setColor(Color.black);
		Game.gameGraphics.fillRect(0, 0, Game.width, Game.height);
		
		Game.gameGraphics.setColor(Color.white);
		Game.gameGraphics.setFont(new Font("arial", Font.BOLD, 20));
		Game.gameGraphics.drawString("Controls", 120, 20);
		
		Game.gameGraphics.setFont(new Font("arial", Font.BOLD, 10));
		Game.gameGraphics.drawString("Movement : WASD and arrows", 40, 36);
		Game.gameGraphics.drawString("Dash : space", 40, 51);
		Game.gameGraphics.drawString("Basic Attack : Click", 40, 66);
		Game.gameGraphics.drawString("Skills : Shift and Control", 40, 81);
		Game.gameGraphics.drawString("Select : Enter", 40, 96);
		Game.gameGraphics.drawString("Pause : Escape", 40, 111);
		backBox.render();
	}
}
