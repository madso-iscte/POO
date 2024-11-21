package objects;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Stairs extends GameElement implements ImageTile, Intransposable, Interactable {

	public Stairs(Point2D point2d) {
		super(point2d, "Stairs", 1);
	}

	@Override
	public String getName() {
		return "Stairs";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public boolean isTransposable() {
		return true;
	}
	
	@Override
	public void interact(GameElement ge, Point2D position, Direction d) {
		//falta codigo
	}

	
}
