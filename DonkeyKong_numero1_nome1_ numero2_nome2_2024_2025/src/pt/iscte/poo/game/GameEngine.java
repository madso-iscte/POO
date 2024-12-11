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
import objects.Princess;
import objects.Stairs;
import objects.Trap;
import objects.Bat;
import objects.Door;
import objects.Gorilla;
import objects.Floor;
import objects.Manel;
import objects.Fire;
import objects.Steak;
import objects.GameElement;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Direction;

public class GameEngine implements Observer {
	

	private static GameEngine engine;
	private Room currentRoom = new Room(Inicial_room, engine);
	private int lastTickProcessed = 0;
	
	private static final String Inicial_room = "rooms/room0.txt";
	

	public static final int GRID_HEIGHT = 10;
	public static final int GRID_WIDTH = 10;
	
	private static GameEngine INSTANCE;
	
	private ImageGUI gui;
	
	
	
	private List<GameElement> list = new ArrayList<>(); 
	private Map<Point2D, List<GameElement>> objectsByPosition = new HashMap<>();
	
	
	private Manel manel;
	private Room nivelAtual;
	private int vidas = 3;
	private int levelTics = 0;
	
	public GameEngine() {
		gui = ImageGUI.getInstance(); 
		list = new ArrayList<>();
		gui.update();
	}
	
	public int getLevelTics() {
		return levelTics;
	}
	
	public int getVidas() {
		return vidas;
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
	
	public void addGameElement(GameElement element) {
		Point2D position = element.getPosition(); 
		if (!objectsByPosition.containsKey(position)) { 
			objectsByPosition.put(position, new ArrayList<>()); 
			}
		objectsByPosition.get(position).add(element); 
		list.add(element);
		gui.addImage(element);
		gui.update();
    }
	
	public void removeGameElement(GameElement e) {
		list.remove(e);
		gui.removeImage(e);
		gui.update();
	}




	public void removeAllGameElements() { 
		List<GameElement> elementsToRemove = new ArrayList<>(list); 
		for (GameElement e : elementsToRemove) { 
			gui.removeImage(e); 
			list.remove(e); 
		} 
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
	    //System.out.println("GUI inicializada!");
	    Room initialRoom = Room.readLevel(Inicial_room); // Lê a sala inicial
	    createLevel(initialRoom);
	    gui.setStatusMessage("DonkeyKong! Lives: 3!");
	    gui.update();
	}
	
	
	
	private void createLevel(Room room) {
		 this.currentRoom = room;
		 room.setEngine(this);
		 for (GameElement element : room.getList()) {
		     addGameElement(element); // Adiciona ao ImageGUI
		 }
		 if(manel!=null) {
			 manel.setVida(manel.getVida());
			 manel.setGameEngine(this);
		 } 
		
		 gui.update();
	}
	
	
	public void loadNextLevel(String nextRoomFilename) {
		int vidaAtual = manel.getVida();
		
		ImageGUI.getInstance().clearImages();
		removeAllGameElements();
		String roomFilePath = "rooms/" + nextRoomFilename;
		Room nextRoom = Room.readLevel(roomFilePath);
		createLevel(nextRoom);
		
		manel.setVida(vidaAtual);
		
		levelTics = 0;
	}
	
	public void resetManelPosition() {
		Point2D initialPosition = currentRoom.getManelInitialPosition();
		addGameElement(manel);
		manel.setPosition(initialPosition);
		
		gui.update();
	}
	
	public void restartGame() {
		if(vidas > 1) {
			removeGameElement(manel);
			resetManelPosition();
			vidas--;
			GameEngine.getInstance().getGui().setStatusMessage("Player lost a life! Lives remaining: " + vidas);
		} else {		
			ImageGUI.getInstance().clearImages(); 
			removeAllGameElements(); 
			currentRoom = Room.readLevel(Inicial_room);
			createLevel(currentRoom);
			GameEngine.getInstance().getGui().setStatusMessage("Player lost all his lives. Game reseted! Lives: 3");
			vidas = 3;
		}
	}

	
	
	public void foundPrincess() {
		Gorilla gorilla = (Gorilla) list.stream()
				.filter(element -> element instanceof Gorilla)
				.findFirst()
				.orElse(null);
		if(gorilla == null || !gorilla.temVida()) {
			GameEngine gameEngine = GameEngine.getInstance();
			GameEngine.getInstance().getGui().setStatusMessage("Player Wins!");
			GameEngine.getInstance().getGui().showMessage("End of Game!", "Player found Princess!");
			gameEngine.restartGame();
			GameEngine.getInstance().getGui().setStatusMessage("Game reseted!");
		} else {
			GameEngine.getInstance().getGui().setStatusMessage("Kill gorilla first!");
			System.out.println("Kill gorilla first!");		
		}
	}
	
	
	
	
	private void checkManelColilsionWithDoor() {
		Point2D manelPosition = manel.getPosition();
		GameElement elementAtPosition = currentRoom.getElementAt(manelPosition);
		if(elementAtPosition instanceof Door) {
			Door door = (Door) elementAtPosition;
			Gorilla gorilla = (Gorilla) list.stream()
					.filter(element -> element instanceof Gorilla)
					.findFirst()
					.orElse(null);
			if(gorilla == null || !gorilla.temVida()) {
				loadNextLevel(door.getNextRoomFilename());
			} else {
				GameEngine.getInstance().getGui().setStatusMessage("Kill gorilla first!");
				System.out.println("Kill gorilla first!");		
			}
		}		
	}
	
	
	public void checkSteakStatus() { 
		if (levelTics > 12) { 
			for (GameElement element : currentRoom.getList()) { 
				if (element instanceof Steak) { 
					GameEngine.getInstance().getGui().setStatusMessage("Steak has gotten rotten!"); 
					break; 
					} 
				} 
			} 
		}
	
	
	@Override
	public void update(Observed source) {
		
		if (ImageGUI.getInstance().wasKeyPressed()) {
			int k = ImageGUI.getInstance().keyPressed();
			System.out.println("Keypressed " + k);
			if (Direction.isDirection(k)) {
				System.out.println("Direction! ");
				Direction direction = Direction.directionFor(k);				
				currentRoom.moveManel(direction);
				checkManelColilsionWithDoor();
			}
		}
								
		
		int t = ImageGUI.getInstance().getTicks();
		while (lastTickProcessed < t) {
			processTick();

			
			Point2D position = manel.getPosition(); 
	        Point2D nextPosition = position.plus(Direction.DOWN.asVector()); 
	        GameElement elementBelow = currentRoom.getElementAt(nextPosition);

	        if (!(elementBelow instanceof Stairs)) {
	            Direction down = Direction.DOWN;  
	            currentRoom.moveManel(down); 
	        }
	        
	        
			List<Gorilla> gorillas = list.stream()
					.filter(element -> element instanceof Gorilla)
					.map(element -> (Gorilla) element)
					.collect(Collectors.toList());
			for(Gorilla gorilla : gorillas) {
				if(gorilla.temVida()) {
					gorilla.moveRandomly();
					if(new Random().nextInt(100)<60) {
						gorilla.lauchFire();
					}
				}
			}
			
			List<Bat> bats = list.stream()
					.filter(element -> element instanceof Bat)
					.map(element -> (Bat) element)
					.collect(Collectors.toList());
			for(Bat bat : bats) {
				bat.moveRandomly();
			}
			
			
		
			List<GameElement> fireballs = currentRoom.getList().stream()
					.filter(element -> element instanceof Fire)
					.collect(Collectors.toList());
			for(GameElement fireball : fireballs) {
				Fire fire = (Fire) fireball;
				fire.checkCollisionWithManel();
				currentRoom.updateFire(fire);
			}
			
			List<GameElement> traps = currentRoom.getList().stream()
	                .filter(element -> element instanceof Trap)
	                .collect(Collectors.toList());
	        for (GameElement trap : traps) {
	            ((Trap) trap).checkCollisionWithManel(manel);
	        }
	        
	        levelTics++; //rottenSteak
	        checkSteakStatus();
		
		}
		ImageGUI.getInstance().update();
	}

	
	private void processTick() {
		System.out.println("Tic Tac : " + lastTickProcessed);
		lastTickProcessed++;
	}

	

}
