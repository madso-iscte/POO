package objects;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public interface Interactable {
	
	public void interact(GameElement ge, Point2D position, Direction d);
}
