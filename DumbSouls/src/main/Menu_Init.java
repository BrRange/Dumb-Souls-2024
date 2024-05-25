package main;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

public class Menu_Init {
	private int cur = 0;
	private String[] options = {"New Game", "Help", "Exit"};
	
	public void tick() {
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
				Game.gameState = "MENUPLAYER";
			}
			else if (options[cur] == "Help") {
				Game.gameState = "MENUHELP";
			}
			else if (options[cur] == "Exit") {
				System.exit(1);
			}
		}
		Game.keyController.clear();
	}
	
	public void render(Graphics g) {
		g.setColor(Color.black); 
		g.fillRect(0, 0, Game.width, Game.height);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 30));
		g.drawString("Dumb Souls", 70, 30);
		g.setFont(new Font("arial",  Font.BOLD, 10));
		
		g.drawString(options[0], 120, 60);
		g.drawString(options[1], 120, 85);
		g.drawString(options[2], 120, 110);
		
		if (cur == 0) {
			g.drawString(">", 90, 60);
		}
		else if (cur == 1) {
			g.drawString(">", 90, 85);
		}
		else if (cur == 2) {
			g.drawString(">", 90, 110);
		}
	}
}
