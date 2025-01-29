package entities.AE;

import main.Game;
import world.Camera;

public class EAE_MortarShock extends AE_Attack_Entity {

    public EAE_MortarShock(int x, int y, double dmg){
        super(x, y, 32, 32, null, 30);
        setMask(0, 0, 32, 32);
        getAnimation(48, 176, 16, 16, 4);
        damage = dmg;
    }

    public void tick() {
        if (life % 10 == 0) {
            index++;
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
