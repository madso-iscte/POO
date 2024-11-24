package pt.iscte.poo.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;
import objects.*;


public class Room{
	
	private Point2D heroStartingPosition;
	private Manel manel;

	
	//manel = new Manel(heroStartingPosition);
	//addObject(manel);
	
	public static final int GRID_HEIGHT = 10;
	public static final int GRID_WIDTH = 10;
	
	private String filename;
	private int levelNumber;
	private String nextRoomFilename;
	private List<GameElement> list;
	//private List<Room> rooms = new ArrayList<>();
	private GameEngine engine;

	private GameElement[][] map;

	private Map<Point2D, List<GameElement>> objectsByPosition = new HashMap<>();
	
	private ImageGUI gui;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Room(String filename, GameEngine engine) {
		//heroStartingPosition = new Point2D(2,2);
		//manel = new Manel(heroStartingPosition);
		//ImageGUI.getInstance().addImage(manel);
		//ImageGUI.getInstance().addImage(new Wall());
		//List<GameElement> objects = new ArrayList<>();
		this.filename = filename;
		this.engine = engine;
		this.list = new ArrayList<>();
		this.map = new GameElement[GRID_HEIGHT][GRID_WIDTH];
		//this.gui = engine.getGui();
//		manel = new Manel(heroStartingPosition);
//      ImageGUI.getInstance().addImage(manel);
//        ImageGUI.getInstance().update();
	}
	
	public List<GameElement> getList(){
		return list;
	}
	
	
	public void setEngine(GameEngine engine) {
		this.engine = engine;
		this.gui = engine.getGui();
	}
	
	public int getLevelNumber(){
		return levelNumber;
	}
	
	public void setLevelNumber(int levelNumber) {
		this.levelNumber = levelNumber;
	}
	
	public String getNextRoomFilename() {
        return nextRoomFilename;
    }

    public void setNextRoomFilename(String nextRoomFilename) {
        this.nextRoomFilename = nextRoomFilename;
    }
	
	public String getFilename() {
		return filename;
	}
	
	public void setHeroStartingPosition(Point2D position) {
		this.heroStartingPosition = position;
	}
	
	public int getWidth() {
		return GRID_WIDTH;
	}
	
	public int getHeight() {
		return GRID_HEIGHT;
	}
	
	

//	public Room readLevel(String f) {
//        Room r = new Room(filename, GameEngine.getInstance());
//		//list = new ArrayList<>();
//		try {
//			Scanner s = new Scanner(new File(f));
//            int i = 0;
//                while (s.hasNextLine()) {
//                    String line = s.nextLine();
//                    if (line.startsWith("#")) {
//                        readConfiguration(line.substring(1),r);
//                        continue;
//                    }
//                    for (int j = 0; j < line.length(); j++) {
//                        char c = line.charAt(j);
//                        if(Character.isDigit(c)) {
//                        	r.setDoorPosition(c+"", j, i);
//                        } else {
//                        	GameElement obj = GameElement.fromChar(c, r, j, i);
//                        	if(obj!=null){
//                        		obj.setPosition(j, i);
//                        		r.addGameElement(obj);
//                        }
//                    }
//                }
//                i++;
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        return r;
//    }
	
	
	public static Room readLevel(String filename) {
		Room room = new Room(filename, GameEngine.getInstance());
        try (Scanner scanner = new Scanner(new File(filename))) {
            int row = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("#")) {
                	readConfiguration(line.substring(1),room);
                    continue;
                }
                for (int col = 0; col < line.length(); col++) {
                	Point2D position = new Point2D(col, row);
                	GameElement floor = new Floor(position);
                	room.list.add(floor); //adiciona floor na camada inferior
                	//room.map[row][col] = floor;
                    char c = line.charAt(col);
                    GameElement element = GameElement.fromChar(c, room, col, row);
                    if (element != null) {
                        room.map[row][col] = element;
                        room.list.add(element);
                    }
                }
                row++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return room;
    }

    
	

    public void addGameElement(GameElement element) { 
    	Point2D position = element.getPosition(); 
    	list.add(element); 
    	objectsByPosition.computeIfAbsent(position, k -> new ArrayList<>()).add(element); 
    	gui.addImage(element);
    	gui.update();
    }
    
	

	private static void readConfiguration(String substring, Room r) {
		// Divide a configuração pelo delimitador ";"
        String[] parts = substring.split(";");

        if (parts.length > 0) {
            try {
                int roomNumber = Integer.parseInt(parts[0]);
                // Exemplo: Armazena o número do nível (implementação fictícia)
                //r.setLevelNumber(roomNumber); 
            } catch (NumberFormatException e) {
                System.err.println("Erro ao interpretar o número da sala.");
            }
        }

        if (parts.length > 1) {
            String nextRoomFile = parts[1];
            // Exemplo: Define o próximo ficheiro de sala (implementação fictícia)
            //r.setNextRoomFilename(nextRoomFile);
        }			
	}

	
	
//	public void setDoorPosition(String doorId, int x, int y) {
//		Point2D position = new Point2D(x, y);
//        Door door = new Door(position);
//        GameEngine.addGameElement(door);
//	}	
		
		
	//delimita o campo de jogo
	public static boolean isPositionValid(Point2D position) {
	    int mapWidth = 10;
	    int mapHeight = 10;

	    return position.getX() >= 0 && position.getX() < mapWidth &&
	           position.getY() >= 0 && position.getY() < mapHeight;
	}

	
	public GameElement getElementAt(Point2D position) {
		for(GameElement element : list) {
			if(element.getPosition().equals(position) && !(element instanceof Floor)){
				return element;
			}
		}
		return new Floor(position);
	}
	
	public void removeElementAt(Point2D position) {
		GameElement element = getElementAt(position);
		if (element != null) { 
			list.remove(element); 
			objectsByPosition.get(position).remove(element);
			if (objectsByPosition.get(position).isEmpty()) {
				objectsByPosition.remove(position);
				} 
			gui.removeImage(element);
			gui.update();
		}
	}
	
	public void moveManel(Direction direction) {
		Manel manel = (Manel) list.stream()
				.filter(element -> element instanceof Manel)
				.findFirst() 
				.orElse(null); 
		if (manel != null) { 
			Point2D oldPosition = manel.getPosition();
			Point2D newPosition = manel.getPosition().plus(direction.asVector());
			
			if (Room.isPositionValid(newPosition)) { 
				GameElement nextElement = getElementAt(newPosition);
				if (nextElement instanceof Intransposable && !((Intransposable) nextElement).isTransposable()) {
					System.out.println("Intransposable Object"+ nextElement.getName());
				} else {
					manel.move(direction);
					//GameElement floor = new Floor(oldPosition);
					//addGameElement(floor);
				}
			} 
		} 
		
	
//		public void moveManel(Direction direction) {
//			Manel manel = (Manel) list.stream()
//					.filter(element -> element instanceof Manel)
//					.findFirst() 
//					.orElse(null); 
//			if (manel != null) { 
//				Point2D newPosition = manel.getPosition().plus(direction.asVector());
//				if (Room.isPositionValid(newPosition)) { 
//					manel.move(direction);						
//				} 
//			}	
		
		
//	public void moveManel(Direction direction) {
//		//calcula a nova posicao
//		Point2D newPosition = manel.getPosition().plus(direction.asVector());
//	    if (Room.isPositionValid(newPosition)) {
//	        manel.move(direction);
//	        
//	    }
  }
	
	
}