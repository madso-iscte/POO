package objects;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.game.Room;
import java.util.ArrayList;
import java.util.List;
import pt.iscte.poo.game.GameEngine;


public abstract class GameElement implements ImageTile {
	
	private Point2D position;
	private String imageName;
	private int layer;

	
	static GameEngine gameEngine;
	static List<GameElement> list;
	static ImageGUI gui;
	static Manel manel;
	
	
	
	public GameElement(Point2D inicialPosition, String imageName, int layer) {
		position = inicialPosition;
		this.imageName = imageName;
		this.layer = layer;
	}
	
	@Override
	public String getName() {
		return imageName;
	}

	@Override
	public Point2D getPosition() {
		return position;
	}
	
	public void setPosition(Point2D position) {
		this.position = position;
	}
	
	public void setPosition(int i, int j) {
		position = new Point2D(i,j);
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
	public int getLayer() {
		return layer;
	}
	
	

	//@Override
	//public abstract int getLayer();


	
	public static GameElement createElement(char key, Point2D position) {
		switch (key) {
		case 'W':
			return new Wall(position);
		case ' ':
			return new Floor(position);
		case 'H':
			return new Manel(position);
		case 'S':
			return new Stairs(position);
		case 's':
			return new Sword(position);
		case 'G': 
			return new Gorilla(position);
		case 'P':
			return new Princess(position);
		default:
			return null;
		}
	}

	
	
}
