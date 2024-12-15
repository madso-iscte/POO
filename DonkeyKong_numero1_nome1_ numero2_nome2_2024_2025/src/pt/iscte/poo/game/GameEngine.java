package pt.iscte.poo.game;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import objects.Princess;
import objects.Stairs;
import objects.Trap;
import objects.TrapWall;
import objects.Bat;
import objects.Door;
import objects.Bomb;
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
	private static final String SCORE_FILE = "scores.txt";
	private List<Bomb> bombs = new ArrayList<>();
	
	
	private Manel manel;
	private Room nivelAtual;
	private int vidas = 3;
	private int totalTics = 0;
	private int levelTics = 0;
	
	public GameEngine() {
		gui = ImageGUI.getInstance(); 
		list = new ArrayList<>();
		gui.update();
	}
	
	public int getLevelTics() {
		return levelTics;
	}
	
	public int getTotalTics() {
		return totalTics;
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
	    Room initialRoom = Room.readLevel(Inicial_room); 
	    createLevel(initialRoom);
	    gui.setStatusMessage("DonkeyKong! Lives: 3!");
	    gui.update();
	}
	
	
	
	private void createLevel(Room room) {
		 this.currentRoom = room;
		 room.setEngine(this);
		 for (GameElement element : room.getList()) {
		     addGameElement(element); 
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
	    if (gorilla == null || !gorilla.temVida()) {
	        winGame();
	    } else {
	        gui.setStatusMessage("Kill the Gorilla first!");
	    }
	}
	
	public static void deleteScoresFile() {
	    File file = new File("scores.txt"); 
	    if (file.exists()) {
	        if (file.delete()) {
	            System.out.println("Scores.txt file sucessfully deleted!");
	        } else {
	            System.err.println("Error to delete Scores.txt!");
	        }
	    } else {
	        System.out.println("Scores.txt doesn't exist.");
	    }
	}
	
	
    public static void saveScore(String jogador, int score) {
        List<Score> scores = getScoresFromFile(SCORE_FILE, new Score(jogador, score));
        try (PrintWriter writer = new PrintWriter(new File(SCORE_FILE))) {
            int count = 0;
            for (Score s : scores) {
                if (count >= 3) break; 
                writer.println(s);
                count++;
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error to save the score!");
        }
    }

    
    private static List<Score> getScoresFromFile(String file, Score novoScore) {
        List<Score> scores = new ArrayList<>();
        scores.add(novoScore); // Adiciona o novo score
        try (Scanner scanner = new Scanner(new File(file))) {
            while (scanner.hasNextLine()) {
                scores.add(new Score(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            System.err.println("Scores archive not foung. A new one will be created.");
        }
        scores.sort(Comparator.comparingInt(Score::getScore));
        return scores;
    }

    
    public static void showScores() {
        try (Scanner scanner = new Scanner(new File(SCORE_FILE))) {
            StringBuilder scores = new StringBuilder("TOP SCORES:\n");
            while (scanner.hasNextLine()) {
                scores.append(scanner.nextLine()).append("\n");
            }
            ImageGUI.getInstance().showMessage("Ranking", scores.toString());
        } catch (FileNotFoundException e) {
            ImageGUI.getInstance().showMessage("Ranking", "None score found.");
        }
    }

    
    public void winGame() {
        gui.showMessage("YOU WON!", "Congrats, you won the game!");
        int totalScore = calculateScore();
        String playerName = JOptionPane.showInputDialog("Write your name:");
        if (playerName == null || playerName.isEmpty()) {
            playerName = "Anonimous";
        }
        saveScore(playerName, totalScore);
        showScores();
        gui.dispose();
        System.exit(0); 
    }

    
    public int calculateScore() {
        return totalTics;
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
	    handleKeyPresses(); // Gerencia teclas pressionadas

	    int t = ImageGUI.getInstance().getTicks();
	    while (lastTickProcessed < t) {
	        processTick();         // Processa lógica específica do "tick"
	           // Incrementa o contador de tics

	        handleManelFall();     // Lógica para queda do Manel
	        updateGorillas();      // Atualiza os movimentos dos Gorillas
	        updateBats();          // Atualiza os morcegos
	        updateBombs();         // Atualiza as bombas
	        updateFireballs();     // Atualiza bolas de fogo
	        updateTraps();         // Atualiza as armadilhas
	        updateTrapWalls();     // Atualiza paredes armadilha

	        totalTics++;
	        levelTics++; // Incrementa o contador do nível
	        checkSteakStatus();    // Verifica o estado do "Steak" (ou qualquer lógica especial)
	    }

	    ImageGUI.getInstance().update(); // Atualiza a interface gráfica
	}
	
//----------------------------------Funções Auxiliares ao Update-------------------------------------------
	private void handleKeyPresses() {
	    if (ImageGUI.getInstance().wasKeyPressed()) {
	        int k = ImageGUI.getInstance().keyPressed();
	        System.out.println("Keypressed " + k);

	        if (k == 66) {
	            manel.dropBomb(); // Manel larga uma bomba
	        }
	        if (Direction.isDirection(k)) {
	            System.out.println("Direction!");
	            Direction direction = Direction.directionFor(k);
	            currentRoom.moveManel(direction); // Move o Manel
	            checkManelColilsionWithDoor();   // Verifica colisão com portas
	        }
	        if (k == 'L') {
	            showScores(); // Exibe o ranking
	        }
	        if (k == 'D') {
	            deleteScoresFile();
	            gui.setStatusMessage("Ficheiro de scores apagado.");
	        }
	    }
	}

	private void handleManelFall() {
	    Point2D position = manel.getPosition();
	    Point2D nextPosition = position.plus(Direction.DOWN.asVector());
	    GameElement elementBelow = currentRoom.getElementAt(nextPosition);

	    if (!(elementBelow instanceof Stairs)) {
	        currentRoom.moveManel(Direction.DOWN);
	    }
	}

	private void updateGorillas() {
	    List<Gorilla> gorillas = currentRoom.getList().stream()
	        .filter(element -> element instanceof Gorilla)
	        .map(element -> (Gorilla) element)
	        .collect(Collectors.toList());

	    for (Gorilla gorilla : gorillas) {
	        if (gorilla.temVida()) {
	            gorilla.moveRandomly(); 
	            if (new Random().nextInt(100) < 40) {
	                 gorilla.lauchFire(); 
	            }
	        }
	    }
	}

	private void updateBats() {
	    List<Bat> bats = currentRoom.getList().stream()
	        .filter(element -> element instanceof Bat)
	        .map(element -> (Bat) element)
	        .collect(Collectors.toList());

	    for (Bat bat : bats) {
	        bat.moveRandomly();
	    }
	}

	private void updateBombs() {
	    List<Bomb> bombs = currentRoom.getList().stream()
	        .filter(element -> element instanceof Bomb)
	        .map(element -> (Bomb) element)
	        .collect(Collectors.toList());

	    for (Bomb bomb : bombs) {
	        bomb.tick();
	        bomb.checkCollisionWithEnemies();
	        bomb.explodeManel();
	    }
	}

	private void updateFireballs() {
	    List<GameElement> fireballs = currentRoom.getList().stream()
	        .filter(element -> element instanceof Fire)
	        .collect(Collectors.toList());

	    for (GameElement fireball : fireballs) {
	        Fire fire = (Fire) fireball;
	        fire.checkCollisionWithManel();
	        currentRoom.updateFire(fire);
	    }
	}

	private void updateTraps() {
	    List<GameElement> traps = currentRoom.getList().stream()
	        .filter(element -> element instanceof Trap)
	        .collect(Collectors.toList());

	    for (GameElement trap : traps) {
	        ((Trap) trap).checkCollisionWithManel(manel);
	    }
	}

	private void updateTrapWalls() {
	    List<GameElement> trapWalls = currentRoom.getList().stream()
	        .filter(element -> element instanceof TrapWall)
	        .collect(Collectors.toList());

	    for (GameElement trapWall : trapWalls) {
	        TrapWall tw = (TrapWall) trapWall;
	        Point2D positionBelowManel = manel.getPosition().plus(Direction.DOWN.asVector());
	        if (tw.getPosition().equals(positionBelowManel)) {
	            tw.interact(manel);
	        } else {
	            tw.resetIfManelLeft(manel);
	        }
	    }
	}

	public void addBomb(Bomb bomb) {
	    bombs.add(bomb);
	}
	
	private void processTick() {
		System.out.println("Tic Tac : " + lastTickProcessed);
		lastTickProcessed++;
	}

	

}
