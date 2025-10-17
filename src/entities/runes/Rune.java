package entities.runes;

import java.awt.image.BufferedImage;

import main.Game;

public abstract class Rune {

	public static int runesInGame = 5;
	public BufferedImage sprite = Game.sheet.getSprite(0, 0, 16, 16);
	public boolean collected = false, equipped = false;
	public int index = 0;
	public String name, description;
	
	public Rune(String name, String desc, int id, BufferedImage sprite) {
		this.name = name;
		description = desc;
		index = id;
		this.sprite = sprite;
	}
	
	public abstract void setup();
	public abstract void tick();
	
}