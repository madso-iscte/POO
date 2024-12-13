package objects;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.gui.ImageTile;

public class TrapWall extends GameElement implements ImageTile, Intransposable, Interactable{
	
	private boolean activated = false;
	private int damage = 10;
	
	public TrapWall(Point2D position) {
		super(position, "Wall", 0);
	}
	
	@Override
	public String getName() {
		return activated ? "Trap" : "Wall";
	}
	
	@Override
	public int getLayer(){
		return 1;
	}
	
	@Override
	public boolean isTransposable() {
		return false;
	}
	
//	@Override
//	public void interact(Manel manel) {
//		if(!activated) {
//			activated = true;
//			manel.setVida(manel.getVida()-damage);
//			GameEngine.getInstance().getGui().setStatusMessage("Armadilha ativada! Vida: " + manel.getVida() + "/100");
//			System.out.println("Armadilha ativa!");
//			if(manel.getVida()<0) {
//				manel.semVida();
//			}
//		}
//	}
	
	@Override
	public void interact(Manel manel) {
		Point2D positionBelowManel = manel.getPosition().plus(Direction.DOWN.asVector());
	    if(!activated) {
	    	if (this.getPosition().equals(positionBelowManel)) {
	    		activated = true;
	    		manel.setVida(manel.getVida() - 10);
	    		GameEngine.getInstance().getGui().setStatusMessage("Ouch! Looks like you found a sneaky trap! Life " + manel.getVida() + "/100");
	    		//System.out.println("Trap ativada");
			
	    		if(manel.getVida()<=0) {
				manel.semVida();
	    		}
	    	}	
	    }
	}
	
	public void resetIfManelLeft(Manel manel) { 
		if (activated && !this.getPosition().equals(manel.getPosition())) {
			activated = false; 
			//System.out.println("TrapWall desativada!"); 
			} 
		}
	
	
//	public void resetIfManelLeft(Manel manel) { 
//		if (activated && !this.getPosition().equals(manel.getPosition())) {
//			activated = false; 
//			GameEngine.getInstance().getGui().setStatusMessage("TrapWall desativada!"); 
//			System.out.println("TrapWall desativada!");  
//		}
//	}
	
//	public void checkManelPosition(Manel manel) {
//		if(!this.getPosition().equals(manel.getPosition())) {
//			if(!activated) {
//				interact(manel);
//			}
//		} else {
//			if(activated) {
//				activated = false;
//				System.out.println("TrapWall desativada");
//			}
//		}
//	}
	
//	public void checkCollisionWithManel(Manel manel) {
//	    Point2D positionBelowManel = manel.getPosition().plus(Direction.DOWN.asVector());
//	    if(!activated) {
//	    	if (this.getPosition().equals(positionBelowManel)) {
//	    		activated = true;
//	    		manel.setVida(manel.getVida() - 10);
//	    		GameEngine.getInstance().getGui().setStatusMessage("Manel was attacked! Life " + manel.getVida() + "/100");
//			
//	    		if(manel.getVida()<=0) {
//				manel.semVida();
//	    		}
//	    	}	
//	    }
//	}
	
//	public void checkCollisionWithManel(Manel manel) {
//	    if(!activated) {
//	    	activated = true;
//	    	Point2D positionBelowManel = manel.getPosition().plus(Direction.DOWN.asVector());
//	    	if (this.getPosition().equals(positionBelowManel)) {
//	    		manel.setVida(manel.getVida() - 10);
//	    		GameEngine.getInstance().getGui().setStatusMessage("Manel was attacked! Life " + manel.getVida() + "/100");
//	    		System.out.println("Manel atingido pela armadilha! Vida restante: " + manel.getVida());
//			
//	    		if(manel.getVida()<=0) {
//				manel.semVida();
//	    		}
//	    	}
//	    }	
//	}
}
