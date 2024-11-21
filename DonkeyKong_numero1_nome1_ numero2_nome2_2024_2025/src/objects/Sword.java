package objects;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.gui.ImageTile;

public class Sword extends GameElement implements ImageTile, Intransposable{
	
	public Sword(Point2D point2d) {
		super(point2d, "Sword", 0);
	}

	@Override
	public String getName() {
		return "Sword";
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
