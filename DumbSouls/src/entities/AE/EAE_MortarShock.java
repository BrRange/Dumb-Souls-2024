package entities.AE;

import entities.enemies.Enemy_Mortar;
import entities.types.Collider.ColliderCircle;
import main.Game;
import world.Camera;

public class EAE_MortarShock extends AE_Attack_Entity {

    public EAE_MortarShock(int x, int y, double dmg){
        super(x, y, 8, 8, 30);
        hitbox = new ColliderCircle(pos, 4);
        getAnimation(0, 48, 16, 16, 4, Enemy_Mortar.sheet);
        damage = dmg;
    }

    public void tick() {
        if (life % 10 == 0) {
            index++;
            ((ColliderCircle)hitbox).radius += 4;
            pos.move(-4, -4);
            width += 8;
            height += 8;
            if(isColiding(Game.player)) {
                Game.player.life -= damage;
            }
        }
        life--;
		if (life == 0) {
			die();
		}
	}

    public void render() {
		Game.gameGraphics.drawImage(animation[index], pos.getX() - Camera.getX(), pos.getY() - Camera.getY(), width, height, null);
	}
}
