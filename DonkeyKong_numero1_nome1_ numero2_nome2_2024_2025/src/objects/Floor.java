package objects;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Floor extends GameElement implements ImageTile, Intransposable {

	public Floor(Point2D point2d) {
		super(point2d, "Floor", 0);
	}

	@Override
	public String getName() {
		return "Floor";
	}

	@Override
	public int getLayer() {
		return 0;
	}

	@Override
	public boolean isTransposable() {
		return true;
	}


}
