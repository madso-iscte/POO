package objects;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.gui.ImageTile;

public class Door extends GameElement implements ImageTile, Intransposable {

	public Door(Point2D point2d) {
		super(point2d, "DoorClosed", 1);
	}

	@Override
	public String getName() {
		return "DoorClosed";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public boolean isTransposable() {
		return true;
	}


}
