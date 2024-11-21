package pt.iscte.poo.game;

import objects.GameElement;
import objects.Manel;
import java.io.File;
import java.util.List;
import java.util.Scanner;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;
import objects.*;

public class Room{
	
	private Point2D heroStartingPosition = new Point2D(1,1);
	private Manel manel;

	
	public static final int GRID_HEIGHT = 10;
	public static final int GRID_WIDTH = 10;
	private String filename; //nome do ficheiro do nivel
	private List<GameElement> objects;
	private GameEngine engine;

	
	public Room(String filename) {
		manel = new Manel(heroStartingPosition);
		ImageGUI.getInstance().addImage(manel);
		//ImageGUI.getInstance().addImage(new Wall());
		this.filename = filename;

	}
	
	public String getFilename() {
		return filename;
	}
	
	public void setHeroStartingPosition(Point2D position) {
		this.heroStartingPosition = position;
	}
	
	private void setEngine(GameEngine engine) {
		this.engine = engine;
	}
	
	public void addObject(GameElement obj) {
		objects.add(obj);
		engine.update();
	}

	public List<GameElement> getObjects(){
		return objects;
	}
	
	//devolve o numero do nivel
	public int getLevelNumber() {
		String[] temp = filename.split("\\."); //ta mal
		char level = temp[0].charAt(temp[0].length() - 1);
		int nrLevel = Character.getNumericValue(level);
		return nrLevel;
	}

	
	// transforma o ficheiro do nivel numa matriz de caracteres
		static private char[][] fileToMatrix(String level) {
			File file = new File("room" + level);
			char[][] room = new char[GRID_WIDTH][GRID_HEIGHT];
			try {
				Scanner scannerFile = new Scanner(file);
				int i = 0;
				while (scannerFile.hasNextLine()) {
					String line = scannerFile.nextLine();
					char[] characters = line.toCharArray();
					for (int j = 0; j < GRID_WIDTH; j++) {
						room[i][j] = characters[j];
					}
					i++;
				}
				scannerFile.close();
			} catch (Exception e) {
				System.err.println("File Not Found");
			}
			return room;
		}

		

		//le a matriz de caracteres, cria e adiciona o GameElement a lista da GameEngine
		public void readLevel() {
			char[][] room = fileToMatrix(filename);
			for (int x = 0; x < GRID_HEIGHT; x++)
				for (int y = 0; y < GRID_HEIGHT; y++) {
					char element = room[y][x];
					Point2D position = new Point2D(x, y);
					// if E setBobcat()
					GameElement e = GameElement.createElement(element, position);
					GameEngine.getInstance().addGameElement(e);
					if (element == 'H') {
						GameEngine.getInstance().setManel((Manel) e);
					}
					if (element == 'W') {
						GameEngine.getInstance().addGameElement(new Wall(position));
					}
					if (element == ' ') {
						GameEngine.getInstance().addGameElement(new Floor(position));
					}
					if (element == 'S') {
						GameEngine.getInstance().addGameElement(new Stairs(position));
					}
					if (element == 'G') {
						GameEngine.getInstance().addGameElement(new Gorilla(position));
					}
					if (element == '0') {
						GameEngine.getInstance().addGameElement(new Door(position));
					}
					//if (element == )
					//falta adicionar os elementos do nivel
				}
			}
		

	//delimita o campo de jogo
	private boolean isPositionValid(Point2D position) {
	    int mapWidth = 10;
	    int mapHeight = 10;

	    return position.getX() >= 0 && position.getX() < mapWidth &&
	           position.getY() >= 0 && position.getY() < mapHeight;
	}
	
	//recebe direcao como parametro
	public void moveManel(Direction direction) {
		//calcula a nova posicao
		Point2D newPosition = manel.getPosition().plus(direction.asVector());
	    if (isPositionValid(newPosition)) {
	        manel.move(direction);
	    }
  }
	
}