package pt.iscte.poo.game;

import pt.iscte.poo.gui.ImageGUI;

import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import objects.Gorilla;
import objects.Manel;
import objects.Fire;
import objects.GameElement;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class GameEngine implements Observer {
	

	private GameEngine engine;
	private String filename;
	private Room currentRoom = new Room(Inicial_room, engine);
	private int lastTickProcessed = 0;
	
	private static final String Inicial_room = "rooms/room0.txt";
	
	//nao sei se é necessario
	public static final int GRID_HEIGHT = 10;
	public static final int GRID_WIDTH = 10;
	
	private static GameEngine INSTANCE;
	
	private ImageGUI gui;
	
	//private List<GameElement> list = new ArrayList<>();;	
	private List<Room> niveis;
	
	
	private List<GameElement> list = new ArrayList<>(); 
	private Map<Point2D, List<GameElement>> objectsByPosition = new HashMap<>();
	
	
	private Manel manel;
	private Room nivelAtual;
	
	public GameEngine() {
		gui = ImageGUI.getInstance(); 
		list = new ArrayList<>();
		gui.update();
	}
	
	public Room getCurrentRoom() {
		return currentRoom;
	}
	
	public ImageGUI getGui() {
		return gui;
	}
	
	public Manel getManel() {
		return manel;
	}

	public Room getNivel() {
		return nivelAtual;
	}
		

	public void setManel(Manel manel) {
		this.manel = manel;
	}
	
	//singleton
	public static GameEngine getInstance() {
		if (INSTANCE == null)
			return INSTANCE = new GameEngine();
		return INSTANCE;
	}
	
	
//	public void addGameElement(GameElement e) {	
//		list.add(e);  // Adiciona o elemento à lista interna
//	    gui.addImage(e);  // Atualiza a interface gráfica
//	    gui.update();  // Refresca a tela
//	} 
	
	public void addGameElement(GameElement element) {
		Point2D position = element.getPosition(); 
		list.add(element);
		if (!objectsByPosition.containsKey(position)) { 
			objectsByPosition.put(position, new ArrayList<>()); 
			}
		objectsByPosition.get(position).add(element); 
		gui.addImage(element);
		System.out.println("Added element: " + element.getName() + " at " + position);
		gui.update();
    }
	
	public void removeGameElement(GameElement e) {
		list.remove(e);
		gui.removeImage(e);
		gui.update();
	}

	public void removeAllGameElements() {
		for (GameElement e : list) {
			gui.removeImage(e);
		}
		list.removeAll(list);
		gui.update();
	}
	

	public List<GameElement> getGameElement(Point2D position) {
		List<GameElement> result = new ArrayList<>();
		for (GameElement element : list) {
			if (element.getPosition().equals(position)) {
				list.add(element);
			}
		}
		return result;
	}
	
	
	public void start() {
	    gui = ImageGUI.getInstance();	    
	    gui.registerObserver(this);
	    gui.go();
	    System.out.println("GUI inicializada!");
	    Room initialRoom = Room.readLevel(Inicial_room); // Lê a sala inicial
	    createLevel(initialRoom);
	    gui.setStatusMessage("DonkeyKong!");
	    gui.update();
	}
	
	
//	public void loadRoom(Room room) {
//		this.currentRoom = room;
//		room.setEngine(this);
//		for(GameElement element : list) {
//			addGameElement(element);
//		}
//		gui.update();
//		
//	}
	
	
	private void createLevel(Room room) {
		 this.currentRoom = room;
		    room.setEngine(this);
		    // Adiciona todos os elementos da sala ao jogo
		    for (GameElement element : room.getList()) {
		        addGameElement(element); // Adiciona ao ImageGUI
		    }
		    gui.update();
	}
//	
//	public void restartLevel() {
//		removeAllGameElements();
//		createLevel(nivelAtual);
//	}
	
	
	@Override
	public void update(Observed source) {
		
		if (ImageGUI.getInstance().wasKeyPressed()) {
			int k = ImageGUI.getInstance().keyPressed();
			System.out.println("Keypressed " + k);
			if (Direction.isDirection(k)) {
				System.out.println("Direction! ");
				//cria uma direcao para aplicar no moveManel
				Direction direction = Direction.directionFor(k);
				currentRoom.moveManel(direction); 
			}
		}
		
				
		
		int t = ImageGUI.getInstance().getTicks();
		while (lastTickProcessed < t) {
			processTick();
			
			Gorilla gorilla = (Gorilla) list.stream()
					.filter(element -> element instanceof Gorilla)
					.findFirst()
					.orElse(null);
			if(gorilla != null) {
				gorilla.moveRandomly();
				if (new Random().nextInt(100) < 20) {
					gorilla.lauchFire();
				}
			}
		}
		
		List<GameElement> fireballs = currentRoom.getList().stream()
				.filter(element -> element instanceof Fire)
				.collect(Collectors.toList());
		for(GameElement fireball : fireballs) {
			Fire fire = (Fire) fireball;
			fire.checkCollisionWithManel();
			currentRoom.updateFire(fire);
		}
		ImageGUI.getInstance().update();
	}

	private void processTick() {
		System.out.println("Tic Tac : " + lastTickProcessed);
		lastTickProcessed++;
	}

	

}
