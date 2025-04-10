package entities.shots;

import java.awt.Color;
import entities.Entity;
import entities.AE.EAE_MortarShock;
import entities.types.Collider.ColliderCircle;
import main.Game;
import world.Camera;

public class Shot_MortarShell extends Shot {
    private static Color shadowColor = new Color(0, 0, 0, 136);
    public Shot_MortarShell(int x, int y, double dx, double dy, double spd, double dmg){
        super(x, y, 9, 9, dx, dy, 0, spd, dmg, 90, Game.sheet.getSprite(3, 180, 9, 9));
        hitbox = new ColliderCircle(pos, 0);
    }

    public void die(Entity target){
        Game.entities.add(new EAE_MortarShock(centerX(), centerY(), damage));
        Game.eShots.remove(this);
    }

    public void render(){
        double parabola = Math.sin(Math.PI * life / 90);
        Game.gameGraphics.drawImage(sprite, pos.getX() - Camera.getX(), pos.getY() - Camera.getY() - (int)(90 * parabola), null);
        Game.gameGraphics.setColor(shadowColor);
        Game.gameGraphics.fillOval(pos.getX() - Camera.getX(), pos.getY() - Camera.getY(), 8 - (int)(4 * parabola), 8 - (int)(4 * parabola));
    }
}
