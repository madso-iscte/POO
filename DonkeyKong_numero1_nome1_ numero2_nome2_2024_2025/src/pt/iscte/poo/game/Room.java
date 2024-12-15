package pt.iscte.poo.game;

import java.io.File;

//import objects.Fire;
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
	
	
	public static final int GRID_HEIGHT = 10;
	public static final int GRID_WIDTH = 10;
	
	private String filename;
	private int levelNumber;
	private String nextRoomFilename;
	private List<GameElement> list;

	private GameElement[][] map;

	private Map<Point2D, List<GameElement>> objectsByPosition = new HashMap<>();
	
	private ImageGUI gui;
	
	private Point2D manelInitialPosition;

	
	public Room(String filename, GameEngine engine) {
		this.filename = filename;
		this.list = new ArrayList<>();
		this.map = new GameElement[GRID_HEIGHT][GRID_WIDTH];
	}
	
	public List<GameElement> getList(){
		return list;
	}
	
	
	public Point2D getManelInitialPosition() {
		return this.manelInitialPosition;
	}

	public void setManelInitialPosition(Point2D position) {
		this.manelInitialPosition = position;
	}

	
	public void setEngine(GameEngine engine) {
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
	
	
	public int getWidth() {
		return GRID_WIDTH;
	}
	
	public int getHeight() {
		return GRID_HEIGHT;
	}

	
	public static Room readLevel(String filename) {
	    Room room = null;
	    while (room == null) { // Continua pedindo até obter um arquivo válido
	        try (Scanner scanner = new Scanner(new File(filename))) {
	            room = new Room(filename, GameEngine.getInstance());
	            int row = 0;
	            while (scanner.hasNextLine()) {
	                String line = scanner.nextLine();
	                if (line.startsWith("#")) {
	                    readConfiguration(line.substring(1), room);
	                    continue;
	                }
	                for (int col = 0; col < line.length(); col++) {
	                    if (row < room.map.length && col < room.map[0].length) {
	                        Point2D position = new Point2D(col, row);
	                        GameElement floor = new Floor(position);
	                        room.list.add(floor);
	                        char c = line.charAt(col);
	                        GameElement element = GameElement.fromChar(c, room, col, row);
	                        if (element != null) {
	                            room.map[row][col] = element;
	                            room.list.add(element);
	                            if (element instanceof Manel) {
	                                room.setManelInitialPosition(position);
	                            }
	                        }
	                    } else {
	                        System.out.println("fora dos limites");
	                    }
	                }
	                row++;
	            }
	        } catch (FileNotFoundException e) {
	            System.out.println("Ficheiro não encontrado. Insira o nome do ficheiro:");
	            Scanner input = new Scanner(System.in);
	            filename = input.nextLine();
	        }
	    }
	    return room;
	}




	private static void readConfiguration(String substring, Room r) {
        String[] parts = substring.split(";");
        if (parts.length > 0) {
            try {
                int roomNumber = Integer.parseInt(parts[0]);
                r.setLevelNumber(roomNumber);
            } catch (NumberFormatException e) {
                System.err.println("Erro ao interpretar o número da sala.");
            }
        }

        if (parts.length > 1) {
            String nextRoomFile = parts[1];
            r.setNextRoomFilename(nextRoomFile);
        }			
	}


		
	//delimita o campo de jogo
	public static boolean isPositionValid(Point2D position) {
	    int mapWidth = GRID_WIDTH;
	    int mapHeight = GRID_HEIGHT;

	    return position.getX() >= 0 && position.getX() < mapWidth &&
	           position.getY() >= 0 && position.getY() < mapHeight;
	}

	
	public GameElement getElementAt(Point2D position) {
		for(GameElement element : list) {
			if(element.getPosition().equals(position) && !(element instanceof Floor)){
				return element;
			}
		}
		return null;
	}
	

	
    public void addGameElement(GameElement element) { 
    	Point2D position = element.getPosition(); 
    	list.add(element); 
    	objectsByPosition.computeIfAbsent(position, k -> new ArrayList<>()).add(element); 
    	gui.addImage(element);
    	gui.update();
    }
	
	public void removeElementAt(Point2D position, GameElement element) {
		if (element != null) { 
			list.remove(element); 
		
			List<GameElement> elementsAtPosition = objectsByPosition.get(position);
			if (elementsAtPosition == null) { 
				elementsAtPosition = new ArrayList<>();
				objectsByPosition.put(position, elementsAtPosition); 
			}
		
			elementsAtPosition.remove(element); 
			if (elementsAtPosition.isEmpty()) { 
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
			//Point2D oldPosition = manel.getPosition();
			Point2D newPosition = manel.getPosition().plus(direction.asVector());
			
			if (Room.isPositionValid(newPosition)) { 
				GameElement nextElement = getElementAt(newPosition);
				
				if(nextElement instanceof Gorilla) {
					System.out.println("Manel está a atacar o Gorilla");
					manel.attack((Gorilla) nextElement) ;
				} else if (nextElement instanceof Intransposable && !((Intransposable) nextElement).isTransposable()) {
					//System.out.println("Intransposable Object"+ nextElement.getName());
				} else if(nextElement instanceof Interactable) {
					((Interactable) nextElement).interact(manel);
					manel.move(direction);
					//gui.removeImage(getElementAt(oldPosition));
				} else {
					manel.move(direction);
					//GameElement floor = new Floor(oldPosition);
					//addGameElement(floor);
				}
			} 
		} 
	}	

	
	public void updateFire(Fire fire) {		
		fire.moveDown();
		gui.update();		
	}

	
	
	
	
  }
	
	
