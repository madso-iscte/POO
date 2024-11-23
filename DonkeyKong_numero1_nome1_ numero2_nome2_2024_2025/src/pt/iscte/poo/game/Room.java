package pt.iscte.poo.game;

import objects.GameElement;
import objects.Manel;
import java.io.File;
import java.util.ArrayList;
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
	private List<GameElement> objects = new ArrayList<>();
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
		ImageGUI.getInstance().update();
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
	static private String[][] fileToMatrix(String level) {
	    File file = new File("./rooms/" + level);
	    String[][] room = new String[GRID_WIDTH][GRID_HEIGHT];
	    try {
	        Scanner scannerFile = new Scanner(file);
	        int i = 0;
	        while (scannerFile.hasNextLine()) {
	            String line = scannerFile.nextLine();
	            String[] elements = line.split(""); // Dividir palavras separadas por espaço
	            for (int j = 0; j < GRID_WIDTH && j < elements.length; j++) {
	                room[i][j] = elements[j];
	            }
	            i++;
	        }
	        scannerFile.close();
	    } catch (Exception e) {
	        System.err.println("Erro ao abrir o arquivo: " + file.getAbsolutePath());
	    }
	    return room;
	}



		

		//le a matriz de caracteres, cria e adiciona o GameElement a lista da GameEngine
	public void readLevel() {
	    String[][] room = fileToMatrix(filename); // Lê matriz de strings
	    for (int y = 0; y < GRID_HEIGHT; y++) {
	        for (int x = 0; x < GRID_WIDTH; x++) {
	            String element = room[y][x];
	            Point2D position = new Point2D(x, y);

	            if (element == null) {
	                System.out.println("Elemento vazio na posição " + position);
	                continue;
	            }

	            GameElement e = GameElement.createElement(element, position);
	            if (e != null) {
	                GameEngine.getInstance().addGameElement(e);
	                System.out.println("Elemento criado: " + e.getName() + " na posição " + position);
	            } else {
	                System.out.println("Elemento desconhecido: " + element + " na posição " + position);
	            }
	        }
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