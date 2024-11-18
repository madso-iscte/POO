package pt.iscte.poo.game;

import objects.Manel;
import objects.Wall;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Direction;

public class Room {
	
	private Point2D heroStartingPosition = new Point2D(1, 1);
	private Manel manel;
	
	public Room() {
		manel = new Manel(heroStartingPosition);
		ImageGUI.getInstance().addImage(manel);
		ImageGUI.getInstance().addImage(new Wall());

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