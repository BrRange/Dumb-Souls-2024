package main;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import entities.Player;

public class Menu_Level {
	private String[] options;
	private int cur;
	
	public Menu_Level(int numOP) {
		options = new String[numOP];
		for (int c = 0; c < numOP; c++) {
			options[c] = "";
		}
	}
	
	private void sortOptions(int numOP) {
		int opt = 0, lastOpt = -1;
		for (int c = 0; c < numOP; c++) {
			opt = Game.rand.nextInt(Game.player.playerWeapon.listNames.length);
			while (opt == lastOpt) {
				opt = Game.rand.nextInt(Game.player.playerWeapon.listNames.length);
			}
			options[c] = Game.player.playerWeapon.checkOptionName(opt);
			lastOpt = opt;
		}
	}
	
	public void tick() {
		if (Game.player.levelUp) {
			sortOptions(3);
			Game.player.levelUp = false;
		}
		if (Game.keyController.contains(87) || Game.keyController.contains(38)) {//W UP
			cur --;
			if (cur < 0) 
				cur = options.length - 1;
		}
		if (Game.keyController.contains(83) || Game.keyController.contains(40)) {//S DOWN
			cur ++;
			if (cur > options.length - 1)
				cur = 0;
		}
		if (Game.keyController.contains(10)) {
			Game.player.playerWeapon.checkOptions(options[cur]);
			Game.player.life = Game.player.maxLife;
			Game.gameState = "NORMAL";
		}
		 
		if (Game.keyController.contains(32)) {
			if (Player.souls >= 100) {
				Player.souls -= 100;
				sortOptions(3);
			}
		}
		Game.keyController.clear();
	}
	
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.width * Game.scale, Game.height * Game.scale);
		
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 10));
			
		g.drawString(options[0], 120, 60);
		g.drawString(options[1], 120, 90);
		g.drawString(options[2], 120, 120);
		
		if (cur == 0) {
			g.drawString(">", 100, 60);
		}
		else if (cur == 1) {
			g.drawString(">", 100, 90);
		}
		else if (cur == 2) {
			g.drawString(">", 100, 120);
		}
		
		g.setColor(new Color(74, 52, 160));
		g.drawString("Souls : " + Player.souls, 255, 20);
		g.drawString("[space] Refresh -100 souls", 180, 150);
	}
}
