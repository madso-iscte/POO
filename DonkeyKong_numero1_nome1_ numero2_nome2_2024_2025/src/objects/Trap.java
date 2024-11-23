package objects;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.gui.ImageTile;

public class Trap extends GameElement implements ImageTile, Intransposable {

	public Trap(Point2D point2d) {
		super(point2d, "Trap", 0);
	}

	@Override
	public String getName() {
		return "Trap";
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
