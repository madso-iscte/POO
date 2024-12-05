package objects;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.game.GameEngine;
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
		return false;
	}
	
	public void checkCollisionWithManel(Manel manel) {
	    Point2D positionBelowManel = manel.getPosition().plus(Direction.DOWN.asVector());
	    if (this.getPosition().equals(positionBelowManel)) {
	        manel.setVida(manel.getVida() - 10);
	        GameEngine.getInstance().getGui().setStatusMessage("Manel was attacked! Life " + manel.getVida() + "/100");
			System.out.println("Manel atingido pela armadilha! Vida restante: " + manel.getVida());
			
			if(manel.getVida()<=0) {
				manel.semVida();
				GameEngine.getInstance().getCurrentRoom().removeElementAt(manel.getPosition(),manel);
				GameEngine.getInstance().getGui().removeImage(manel);
			}
	    }
	}


}
