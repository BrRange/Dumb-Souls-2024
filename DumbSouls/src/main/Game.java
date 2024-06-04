package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JFrame;

import entities.Entity;
import entities.Player;
import entities.enemies.*;
import entities.shots.*;
import graphics.Spritesheet;
import graphics.UI;
import sounds.SoundPlayer;
import world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener{
	
	private static final long serialVersionUID = 1L;
	private Thread thread;
	private boolean isRuning = false;
	
	public static HashSet<Integer> keyController = new HashSet<Integer>();
	public static JFrame frame;
	public static final int width = 320;
	public static final int height = 160;
	public static final int scale = 3;
	public static int mx, my, amountTicks;
	
	private static BufferedImage image;
	public static Spritesheet sheet;
	public enum gameState{
		NORMAL(() -> Game.tick(), g -> render(g)),
		MENULEVEL(() -> Menu_Level.tick(), g -> Menu_Level.render(g)),
    	MENUINIT(() -> Menu_Init.tick(), g -> Menu_Init.render(g)),
		MENUPLAYER(() -> Menu_Player.tick(), g -> Menu_Player.render(g)),
		MENUHELP(() -> Menu_Help.tick(), g -> Menu_Help.render(g)),
		MENUPAUSE(() -> Menu_Pause.tick(), g -> Menu_Pause.render(g)),
		MENURUNES(() -> Menu_Runes.tick(), g -> Menu_Runes.render(g));

		private Runnable stateTick;
		private Consumer<Graphics> stateRender;
		gameState(Runnable stateTick, Consumer<Graphics> stateRender){
			this.stateTick = stateTick;
			this.stateRender = stateRender;
		}
	} public static gameState gameStateHandler;

	public static UI ui;
	public static World world;
	public static Player player;
	public static Random rand;
	public static Menu_Level levelUpMenu;
	public static Menu_Player playerMenu;
	public static Menu_Init startMenu;
	public static Menu_Help helpMenu;
	public static Menu_Pause pauseMenu;
	public static Menu_Runes runesMenu;
	private static SoundPlayer soundtrack;
	
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<Shot> shots;
	public static List<Enemy_Shot> eShots;

	public Game() {
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setPreferredSize(new Dimension(width * scale, height * scale));
		initFrame();
		entities = new ArrayList<Entity>();
		shots = new ArrayList<Shot>();
		enemies = new ArrayList<Enemy>();
		eShots = new ArrayList<Enemy_Shot>();
		rand = new Random();
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		sheet = new Spritesheet("spritesheet.png");
		player = new Player(0, 0, 16, 16, sheet.getSprite(0, 16, 16, 16));
		entities.add(player);
		world = new World("map00.png");
		ui = new UI();
		soundtrack = new SoundPlayer("sndTrack.wav");
		startMenu = new Menu_Init();
		playerMenu = new Menu_Player();
		pauseMenu = new Menu_Pause();
		levelUpMenu = new Menu_Level(3);
		helpMenu = new Menu_Help();
		runesMenu = new Menu_Runes();
		gameStateHandler = gameState.MENUINIT;
		Save_Game.loadSave();
	}
	
	public void start() {
		Thread thread = new Thread(this);
		thread.start();
		isRuning = true;
	}
	
	public static void main(String args[]) {
		Game game = new Game();
		game.start();
		soundtrack.LoopSound();
	}
	public void end() {
		isRuning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public void initFrame() {
		frame = new JFrame("Dumb Souls");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.createBufferStrategy(3);
	}
	
	private static void spawnEnemies() {
		if (enemies.size() == 0) {
			if (World.wave % 10 != 0) {
				world.raiseMaxEnemies();
				World.wave++;
				for (int c = 0; c <= World.maxEnemies; c++) {
					world.spawnEnemy();
				}
			}
			else {
				world.spawnBoss();
				World.wave++;
			}
		}
	}

	private void baseRender(){
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = Game.image.getGraphics();
		gameStateHandler.stateRender.accept(g);
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, width * scale, height * scale, null);
		if(gameStateHandler == gameState.NORMAL) ui.render(g);
		bs.show();
	}

	private static void tick() {
		player.moveX = player.moveY = 0;
		for(int i = 0; i < entities.size(); i++) {
			entities.get(i).tick();
		}
		for(int i = 0; i < shots.size(); i++) {
			shots.get(i).tick();
		}
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).tick();
		}
		for(int i = 0; i < eShots.size(); i++) {
			eShots.get(i).tick();
		}
		spawnEnemies();
	}

	private static void render(Graphics g) {
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, width, height);
		world.render(g);
		Collections.sort(entities, Entity.entityDepth);
		Collections.sort(enemies, Entity.entityDepth);
		for(Entity e : entities) {
			e.render(g);
		}
		for(Enemy e : enemies) {
			e.render(g);
		}
		for(Shot e : shots) {
			e.render(g);
		}
		for(Enemy_Shot e : eShots) {
			e.render(g);
		}
	}
	
	public void run() {
		requestFocus();
		long lastTime = System.currentTimeMillis();
		int amountTicks = 60;
		float framesSec = 1f / amountTicks;
		double delta;
		
		while(isRuning) {
			long now = System.currentTimeMillis();
			delta = (now - lastTime) / 1000.0;
			if(delta >= framesSec) {
				gameStateHandler.stateTick.run();
				lastTime = now;
			}
			baseRender();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		end();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keyController.add(e.getKeyCode());

		if (keyController.contains(KeyEvent.VK_ESCAPE)) {
			switch(gameStateHandler) {
				case NORMAL:
					gameStateHandler = gameState.MENUPAUSE;
					break;
				case MENUPAUSE:
					player.stopMoving();
					gameStateHandler = gameState.NORMAL;
					break;
				default: break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keyController.remove(e.getKeyCode());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		player.attack = true;
		player.playerWeapon.mx = e.getX() / scale;
		player.playerWeapon.my = e.getY() / scale;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		player.attack = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if(gameStateHandler == gameState.NORMAL) gameStateHandler = gameState.MENUPAUSE;
		keyController.clear();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
	}
}