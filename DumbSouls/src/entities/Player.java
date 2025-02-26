package entities;

import entities.runes.Rune;
import entities.shots.Shot;
import entities.weapons.Weapon;
import graphics.Shader;
import graphics.Spritesheet;
import graphics.UI;
import java.util.List;
import main.Game;
import main.Menu_Init;
import main.Menu_Level;
import main.Menu_Player;
import main.Save_Game;
import world.Camera;
import world.World;

public class Player extends Entity {

	private int tickTimer, attackTimer, camXOffset, camYOffset;
	public boolean moving, levelUp;
	public short abltCooldown;
	public int maxLife = 100, exp = 0, maxExp = 100, maxMana = 100;
	public static int souls;
	public int level = 1;
	private int frames, maxFrames = 40;
	public int direct = 2;
	public double maxSpeed = 1.5, speed = maxSpeed, speedBoost = 1, mana = 100, manaRec = 2, lifeRec = 1.001;
	public Vector moveDir = new Vector(0, 0);
	public Weapon playerWeapon;
	public static List<Rune> runesInventory, runesEquipped;
	public static int runeLimit = 3;
	public static Spritesheet sheet = new Spritesheet("res/spritesheets/Player.png");

	public Player(int x, int y) {
		super(x, y, 16, 16, Game.sheet.getSprite(0, 16, 16, 16));

		this.life = 100;

		getAnimation(0, 0, 16, 16, 4, sheet, 4);

		setMask(4, 1, 8, 15);
		depth = 1;
		camXOffset = (width - Game.width) >> 1;
		camYOffset = (height - Game.height) >> 1;
	}

	private void isAttacking() {
		if (attackTimer == playerWeapon.attackTimer && !Game.clickController.isEmpty()) {
			attackTimer = 0;
			playerWeapon.Attack();
			Game.clickController.clear();
		}
		if (attackTimer < playerWeapon.attackTimer) {
			attackTimer++;
		}
	}

	public void stopMoving() {
		moveDir.set(0, 0);
	}

	public static void runeSetup(){
		if (runesEquipped.size() == 0)
			return;
		for (int i = 0; i < runesEquipped.size(); i++) {
			runesEquipped.get(i).setup();
		}
	}

	private void runeTick() {
		if (runesEquipped.size() == 0)
			return;
		for (int i = 0; i < runesEquipped.size(); i++) {
			runesEquipped.get(i).tick();
		}
	}

	public static void die() {
		try {
			Save_Game.save();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		Game.entities.clear();
		Game.shots.clear();
		Game.enemies.clear();
		Game.eShots.clear();
		Game.player = new Player(0, 0);
		Game.entities.add(Game.player);
		World.maxEnemies = 5;
		World.wave = 1;
		World.bossName = "";
		World.bossTime = false;
		Game.world = new World("res/map00.png");
		Game.ui = new UI();
		Game.startMenu = new Menu_Init();
		Game.playerMenu = new Menu_Player();
		Game.levelUpMenu = new Menu_Level(3);
		Game.gameStateHandler = Game.gameState.MENUINIT;
	}

	private void checkExp() {
		if (exp >= maxExp) {
			levelUp = true;
			level++;
			exp -= maxExp;
			moveDir.set(0, 0);
			maxExp *= 1.2;
			Game.gameStateHandler = Game.gameState.MENULEVEL;
		}
	}

	private void isMoving() {
		if (!moving)
			return;
		frames++;
		if (frames == maxFrames)
			frames = 0;
	}

	private void shotDamage() {
		for (int i = 0; i < Game.eShots.size(); i++) {
			Shot eSh = Game.eShots.get(i);
			if (isColiding(eSh)) {
				life -= eSh.damage;
				damaged = true;
				Game.eShots.remove(eSh);
			}
		}
	}

	public int getSouls() {
		return souls;
	}

	private boolean TickTimer(int frames) {
		if (tickTimer % frames == 0)
			return true;
		return false;
	}

	private void refreshTick() {
		tickTimer++;
		if (tickTimer >= 60)
			tickTimer = 0;
	}

	private void castAblt() {
		if (playerWeapon.useDash)
			playerWeapon.Dash();
		if (playerWeapon.usePowerMove)
			playerWeapon.powerMove();
		if (playerWeapon.useSpecialMove)
			playerWeapon.specialMove();
		if (abltCooldown == 0) {
			if (Game.keyController.contains(32)) {
				playerWeapon.Dash();
				abltCooldown = 30;
			}
			if (Game.keyController.contains(16)) {
				playerWeapon.powerMove();
				abltCooldown = 30;
			}
			if (Game.keyController.contains(17)) {
				playerWeapon.specialMove();
				abltCooldown = 30;
			}
		} else {
			abltCooldown--;
		}
	}

	public void tick() {
		if (Game.keyController.contains(87) || Game.keyController.contains(38))// W UP
			moveDir.y--;
		if (Game.keyController.contains(83) || Game.keyController.contains(40))// S DOWN
			moveDir.y++;
		if (Game.keyController.contains(68) || Game.keyController.contains(39))// A LEFT
			moveDir.x++;
		if (Game.keyController.contains(65) || Game.keyController.contains(37))// D RIGHT
			moveDir.x--;
		moving = true;
		moveDir.normalize();
		if (moveDir.x == 0 && moveDir.y == 0) {
			moving = false;
		} else {
			if (moveDir.x > 0) {
				direct = 0;
				state = 0;
			}
			else if (moveDir.x < 0) {
				direct = 1;
				state = 1;
			}
			else if (moveDir.y > 0) {
				direct = 2;
				state = 2;
			}
			else if (moveDir.y < 0) {
				direct = 3;
				state = 3;
			}
		}

		castAblt();

		if (mana < maxMana && TickTimer(20))
			mana = Math.min(maxMana, mana + manaRec);

		if (Game.player.life < Game.player.maxLife && TickTimer(10))
			Game.player.life = Math.min(Game.player.maxLife, Game.player.life * Game.player.lifeRec);

		refreshTick();

		if (life <= 0)
			die();

		playerWeapon.tick();
		runeTick();

		pos.move(speed * speedBoost * moveDir.x, speed * speedBoost * moveDir.y);
		clampBounds(outOfBounds());
		isMoving();
		speedBoost = 1;

		isAttacking();
		checkExp();
		shotDamage();
		damagedAnimation();

		final int MED = 2; // Mouse-Efectiveness-Denominator
		Camera.Clamp(pos.getX() + camXOffset + (Game.mx / Game.scale - Game.width / 2) / MED,
				pos.getY() + camYOffset + (Game.my / Game.scale - Game.height / 2) / MED);
	}

	public void render() {
		Game.gameGraphics.drawImage(
			(damaged) ? Shader.reColor(animation[moving ? (frames / 10) + state * 4 : state * 4], damagedHue)
								: animation[moving ? (frames / 10) + state * 4 : state * 4],
						pos.getX() - Camera.getX(), pos.getY() - Camera.getY(), null);
						
		playerWeapon.render();
	}
}
