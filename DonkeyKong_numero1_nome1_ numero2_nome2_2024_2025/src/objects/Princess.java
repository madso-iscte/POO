package objects;

import pt.iscte.poo.gui.ImageTile;

import pt.iscte.poo.utils.Point2D;

public class Princess extends GameElement implements ImageTile, Intransposable{
	
	public Princess(Point2D point2d) {
		super(point2d, "Princess", 0);
	}

	@Override
	public String getName() {
		return "Princess";
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
