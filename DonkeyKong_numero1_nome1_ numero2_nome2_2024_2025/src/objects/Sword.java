package objects;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.gui.ImageTile;
//import pt.iscte.poo.utils.Direction;
//import pt.iscte.poo.game.GameEngine;

public class Sword extends GameElement implements ImageTile, Interactable{
	
	private int damage = 15;
	
	public Sword(Point2D point2d, int damage) {
		super(point2d, "Sword", 0);
		this.damage = damage;
	}

	@Override
	public String getName() {
		return "Sword";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	public int getSwordDamage() {
		return damage;
	}
	
	@Override
	public void interact(Manel manel) {
		manel.setDamage(getSwordDamage());
		manel.setHasSword(true);
		
		GameEngine.getInstance().getCurrentRoom().removeElementAt(this.getPosition());
		GameEngine.getInstance().getGui().setStatusMessage("Damage has increased by 15!");
		
		System.out.println("Damage atual: " + manel.getDamage());
		}
}
	

