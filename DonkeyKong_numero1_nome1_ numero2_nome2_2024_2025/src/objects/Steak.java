package objects;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.gui.ImageTile;

public class Steak extends GameElement implements ImageTile, Interactable{
	
	public Steak(Point2D point2d) {
		super(point2d, "Sword", 0);
	}

	@Override
	public String getName() {
		return "GoodMeat";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	
	@Override
	public void interact(Manel manel) {
		manel.setVida(100);
		
		GameEngine.getInstance().getCurrentRoom().removeElementAt(this.getPosition());
		GameEngine.getInstance().getGui().setStatusMessage("Full life!");
		
		System.out.println("Vida atual " + manel.getVida());
		}
}
