package objects;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.gui.ImageTile;

public class Steak extends GameElement implements ImageTile, Interactable{
	
	//private GameEngine gameEngine;
	private int tics = 0;
//	private boolean isRotten = false;
	
	public Steak(Point2D point2d) {
		super(point2d, "Sword", 0);
	}
	
	public void processTics() {
		tics++;
		System.out.println("Tics: " + tics);
	}

	@Override
	public String getName() {
		return "GoodMeat";
	}
	
//	@Override
//	public String getName() {
//		return isRotten ? "GooMeat" : "Poison";
//	}

	@Override
	public int getLayer() {
		return 1;
	}
	
	public boolean isRotten() {
		int levelTics = GameEngine.getInstance().getLevelTics();
		if(levelTics > 12) {
			return true;
		} else {
			return false;
		}
	}

	public void showMessageRottenSteak() {
		int levelTics = GameEngine.getInstance().getLevelTics();
		if(levelTics > 12) {
			GameEngine.getInstance().getGui().setStatusMessage("Steak has got rotten	!");
		}
	}
	
	@Override
	public void interact(Manel manel) {
		int levelTics = GameEngine.getInstance().getLevelTics();
		if(levelTics > 12) {
			manel.setVida(manel.getVida()-20);
			GameEngine.getInstance().getGui().setStatusMessage("Oops! You waited too long! Player´s life: " + manel.getVida());
			
		} else {
			manel.setVida(100);
			GameEngine.getInstance().getGui().setStatusMessage("Full life!");
		}
	
		GameEngine.getInstance().getCurrentRoom().removeElementAt(this.getPosition(),this);
		
		System.out.println("Life:" + manel.getVida());
	}
	
	
	
	
	
}
