package main;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

public class Menu_Help {
	
	public static void tick() {
		if (Game.keyController.contains(10)){
			Game.keyController.clear();
			Game.gameStateHandler = Game.gameState.MENUINIT;
		}
	}
	
	public static void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.width, Game.height);
		
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 20));
		g.drawString("Controls", 120, 20);
		
		g.setFont(new Font("arial", Font.BOLD, 10));
		g.drawString("Movement Controls: WASD (Menus also)", 40, 36);
		g.drawString("Dash : space", 40, 51);
		g.drawString("Basic Attack : Any mouse button", 40, 66);
		g.drawString("Skills : 1, 2", 40, 81);
		g.drawString("Select : Enter", 40, 96);
		g.drawString("Pause : Escape", 40, 111);
		g.drawString("> Back", 10, 140);
	}
}
