package entities.runes;

import java.awt.image.BufferedImage;

public class Rune {
	
	public BufferedImage sprite;
	public boolean equipped;
	public String name, description;
	
	public Rune(BufferedImage sprite) {
		this.sprite = sprite;
		this.name = "Default";
		this.description = "Default desc.";
	}
	
	public void tick() {
		
	}
	
}
