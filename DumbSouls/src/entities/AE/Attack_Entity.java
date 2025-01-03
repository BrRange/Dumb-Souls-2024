package entities.AE;

import java.awt.image.BufferedImage;
import entities.*;
import main.Game;
import world.Camera;

public class Attack_Entity extends Entity {
	
	protected int tickTimer;
	protected BufferedImage[] animation;
	
	public Attack_Entity(int x, int y, int width, int height, BufferedImage sprite, int life) {
		super(x, y, width, height, sprite);
		this.life = life;
	}
	
	protected void getAnimation(int x, int y, int width, int height, int frames) {
		animation = new BufferedImage[frames];
		
		for(int i = 0; i < animation.length; i++ ) {
			animation[i] = Game.sheet.getSprite(x , y, width, height);
			x += width;
		}
	}
	
	protected void die() {
		Game.entities.remove(this);
	}
	
	public void tick() {
		
	}

	protected boolean TickTimer(int frames){
		if (tickTimer % frames == 0){
			return true;
		}
		else{
			return false;
		}
	}

	protected void refreshTick(){
		tickTimer++;
		if (tickTimer >= 60){
			tickTimer = 0;
		}
	}
	
	public void render() {
		Game.gameGraphics.drawImage(animation[0], getX() - Camera.getX(), getY() - Camera.getY(), null);
	}
}
