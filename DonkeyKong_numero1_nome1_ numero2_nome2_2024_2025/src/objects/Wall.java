package objects;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Wall extends GameElement implements ImageTile, Intransposable {

	public Wall(Point2D point2d) {
		super(point2d,"Wall",1);
	}

	@Override
	public String getName() {
		return "Wall";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public boolean isTransposable() {
		return false;
	}

	
}
