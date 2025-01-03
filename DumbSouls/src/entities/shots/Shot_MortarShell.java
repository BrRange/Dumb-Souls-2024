package entities.shots;

import entities.Entity;
import entities.AE.EAE_MortarCrater;
import main.Game;
import world.Camera;

public class Shot_MortarShell extends Shot {
    public Shot_MortarShell(int x, int y, double dx, double dy, double spd, double dmg, int life){
        super(x, y, 9, 9, dx, dy, 0, spd, dmg, life, Game.sheet.getSprite(3, 180, 9, 9));
        setMask(0, 0, 0, 0);
        maxLife = life;
    }

    public void die(Entity target){
        Game.entities.add(new EAE_MortarCrater(centerX(), centerY(), damage));
        Game.eShots.remove(this);
    }

    public void render(){
        Game.gameGraphics.drawImage(sprite, getX() - Camera.getX(), getY() - Camera.getY() - (int)(16 * Math.sin(life / maxLife * Math.PI)), null);
    }
}
