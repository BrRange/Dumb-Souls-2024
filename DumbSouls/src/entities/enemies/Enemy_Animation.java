package entities.enemies;

import java.awt.image.BufferedImage;
import entities.Entity;
import main.Game;

public class Enemy_Animation extends Enemy {
	private String style;
	private Entity followEntity;
	
	public Enemy_Animation(int x, int y, int width, int height, BufferedImage sprite, int time, 
			int maxFrames, int maxIndex, int xSprite,int ySprite, int wSprite, int hSprite, String style, Entity entity, boolean specialRare) {
		super(x, y, width, height, sprite);
		life = time;
		this.specialRare = specialRare;
		getAnimation(xSprite, ySprite, wSprite, hSprite, maxIndex);
		this.maxFrames = maxFrames;
		this.maxIndex = maxIndex;
		this.style = style;
		followEntity = entity;
		depth = 2;
	}

	private void styleReader() {
		switch (style){
			case "goToUp_1": 
				depth = 0;
				y -= 0.3;
			break;
			case "goToUp_2": 
				depth = 2;
				y -= 0.1;
			break;
			case "goToUp_Objt_1": 
				depth = 2;
				y -= 0.4;
				x = followEntity.getX();
			break;
			case "frames_1":
				animate();
			break;	
		}
	}
	
	public void tick() {
		attackTimer++;
		styleReader();
		
		if (attackTimer == life) {
			Game.enemies.remove(this);
		}
		
	}
}
