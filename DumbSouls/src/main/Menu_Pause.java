package main;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import entities.Player;

public class Menu_Pause {
    private static String[] options = {"Resume", "Initial Menu"};
	private static int cur;
	
    public static void tick() {
		if (Game.keyController.contains(83) || Game.keyController.contains(40)) {//S DOWN
			cur++;
			if (cur > options.length - 1) {
				cur = 0;
			}
		}
		if (Game.keyController.contains(87) || Game.keyController.contains(38)) {//W UP
			cur--;
			if (cur < 0) {
				cur = options.length - 1;
			}
		}
		if (Game.keyController.contains(10)) {
			if (options[cur] == "Resume") {
				Game.player.stopMoving();
				Game.gameStateHandler = Game.gameState.NORMAL;
			}
			else if (options[cur] == "Initial Menu") {
				cur = 0;
				Player.die();
			}
		}
		Game.keyController.clear();
	}

    public static void render(Graphics g) {
        g.setColor(Color.black); 
		g.fillRect(0, 0, Game.width, Game.height);
        g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 20));
		g.drawString("Pause", 120, 30);
		g.setFont(new Font("arial",  Font.BOLD, 10));
		
		g.drawString(options[0], 120, 60);
		g.drawString(options[1], 120, 90);

		g.drawString("Life : " + (int)Game.player.life + "/" + Game.player.maxLife, 10, Game.height - 15);
		g.drawString("Mana : " + (int)Game.player.mana + "/" + Game.player.maxMana, 10, Game.height - 5);
		g.drawString("Souls : " + Game.player.getSouls(), Game.width * 2 / 3, Game.height - 15);
		g.drawString("EXP : " + Game.player.exp + "/" + Game.player.maxExp, Game.width * 2 / 3, Game.height - 5);
		
		if (cur == 0) {
			g.drawString(">", 100, 60);
		}
		else if (cur == 1) {
			g.drawString(">", 100, 90);
		}
    }
}
