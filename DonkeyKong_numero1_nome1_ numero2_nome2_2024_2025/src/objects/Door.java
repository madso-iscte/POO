package objects;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.gui.ImageTile;

public class Door extends GameElement implements ImageTile, Intransposable {

	private String nextRoomFilename;
	
	public Door(Point2D point2d, String nextRoomFilename) {
		super(point2d, "DoorClosed", 1);
		this.nextRoomFilename = nextRoomFilename;
	}
	
	public String getNextRoomFilename() {
		return nextRoomFilename;
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
