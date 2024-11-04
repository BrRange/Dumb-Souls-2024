package entities.orbs;

import entities.enemies.Enemy;
import main.Game;
import world.Camera;
import entities.Player;
import entities.runes.*;

public class Rune_Orb extends Enemy {
	
	private String[] runes = {"Life", "Mana", "Speed", "Double Attack", "EXP"};
	private int indexRunes;
	
	public Rune_Orb(int x, int y, int width, int height) {
		super(x, y, width, height, null);
		
		this.indexRunes = Game.rand.nextInt(runes.length - 1);
		
		switch (runes[indexRunes]) {
			case "Life":
				this.sprite = Life_Rune.sprite;
			break;
			case "Mana":
				this.sprite = Mana_Rune.sprite;
			break;
			case "Speed":
				this.sprite = Speed_Rune.sprite;
			break;
			case "Double Attack":
				this.sprite = MultiAttack_Rune.sprite;
			break;
			case "EXP":
				this.sprite = EXP_Rune.sprite;
			break;
		}
		this.speed = Game.player.maxSpeed * 1.1;
		this.setMask(0, 0, 16, 16);
		this.depth = 1;
	}
	
	public void tick() {
		this.movement();

		if (isColiding(Game.player)){
	        Game.enemies.remove(this);
	        
	        switch (runes[indexRunes]) {
	        	case "Life":
	        		Player.runesInventory.add(new Life_Rune());
	        	break;
	        	case "Mana":
	        		Player.runesInventory.add(new Mana_Rune());
	        	break;	
	        	case "Speed":
	        		Player.runesInventory.add(new Speed_Rune());
	        	break;
	        	case "Double Attack":
	        		Player.runesInventory.add(new MultiAttack_Rune());
	        	break;	
	        	case "EXP":
	        		Player.runesInventory.add(new EXP_Rune());
	        	break;	
	        }
	    }
	}
	
	public void render() {
		Game.gameGraphics.drawImage(this.sprite, this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
	}
}
