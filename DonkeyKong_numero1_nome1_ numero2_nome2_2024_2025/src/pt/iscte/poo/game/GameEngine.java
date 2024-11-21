package pt.iscte.poo.game;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import objects.Manel;
import objects.GameElement;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class GameEngine implements Observer {
	
	private String filename;
	private Room currentRoom = new Room(filename);
	private int lastTickProcessed = 0;
	
	private static final String Inicial_room = "room0.txt";
	
	//nao sei se é necessario
	public static final int GRID_HEIGHT = 10;
	public static final int GRID_WIDTH = 10;
	
	private static GameEngine INSTANCE;
	private ImageGUI gui;
	private List<GameElement> list;
	private List<Room> niveis;
	private Point2D posicaoInicialManel;
	private Manel manel;
	private Room nivelAtual;
	
	public GameEngine() {
		ImageGUI.getInstance().update();
		list = new ArrayList<>();
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
	
	
	public void addGameElement(GameElement e) {
		list.add(e);
		gui.addImage(e);
	}
	
	//remove o GameElement recebido
	public void removeGameElement(GameElement e) {
		list.remove(e);
		gui.removeImage(e);
	}

	//remove todos os GameElement da lista
	public void removeAllGameElements() {
		for (GameElement e : list) {
			gui.removeImage(e);
		}
		list.removeAll(list);
	}



	public List<GameElement> getGameElement(Point2D position) {
		List<GameElement> list = new ArrayList<>();
		for (GameElement element : list) {
			if (element.getPosition().equals(position)) {
				list.add(element);
			}
		}
		return list;
	}
	
	
	public void start() {

		gui = ImageGUI.getInstance(); // 1. obter instancia ativa de ImageGUI
		gui.setSize(GRID_HEIGHT, GRID_WIDTH); // 2. configurar as dimensoes
		gui.registerObserver(this); // 3. registar o objeto ativo GameEngine como observador da GUI
		gui.go(); // 4. lancar a GUI
		
		// Criar o cenario de jogo
		createLevel(new Room(Inicial_room));// cria o mapa de um nivel especifico

		gui.setStatusMessage("DonkeyKong");
		gui.update();
	}
	
	
	
	private void createLevel(Room n) {
		n.readLevel();
		nivelAtual = n;
		// tileList = nivelAtual.getTileList();
	}

	
	public void restartLevel() {
		removeAllGameElements();
		createLevel(nivelAtual);
	}
	
	private void loadGame() {
	//	File[] files = new File("./rooms").niveis;
		
	}
	
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
		}
		ImageGUI.getInstance().update();
	}

	private void processTick() {
		System.out.println("Tic Tac : " + lastTickProcessed);
		lastTickProcessed++;
	}



}
