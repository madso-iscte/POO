package objects;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Gorilla extends GameElement implements ImageTile, Intransposable {
	
	public Gorilla(Point2D point2d) {
		super(point2d, "DonkeyKong", 0);
	}

	@Override
	public String getName() {
		return "DonkeyKong";
	}

	@Override
	public int getLayer() {
		return 0;
	}

	@Override
	public boolean isTransposable() {
		return false;
	}


}
