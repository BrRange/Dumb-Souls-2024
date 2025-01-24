package entities.shots;

import entities.Entity;
import entities.AE.AE_PoisonPool;
import main.Game;

public class Shot_PlayerPoison extends Shot {
    private int poolSize, poolD;
    public Shot_PlayerPoison(int x, int y, double dx, double dy, double ang, int dmg, int size){
        super(x, y, 8, 8, dx, dy, ang, 3, 0, 35, Game.sheet.getSprite(208, 32, 16, 16));
        poolSize = size;
        poolD = dmg;
    }
    
    public void die(Entity target){
        Game.entities.add(new AE_PoisonPool(centerX(), centerY(), poolSize, 120, poolD));
        Game.shots.remove(this);
    }
}
