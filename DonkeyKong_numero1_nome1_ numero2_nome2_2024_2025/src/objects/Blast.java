package objects;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Blast extends GameElement implements ImageTile {

	public Blast(Point2D point2d) {
		super(point2d,"Blast",2);
	}

	@Override
	public String getName() {
		return "Blast";
	}

	@Override
	public int getLayer() {
		return 2;
	}
}
