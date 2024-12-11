package objects;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction; 
import pt.iscte.poo.utils.Point2D; 
import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import java.util.Random;

public class Bat extends GameElement implements ImageTile, Intransposable, MovableObject, Interactable{

	private Point2D position;
	private Random random = new Random();
	private int damage = 20;
	private boolean isDead;
	
	public Bat(Point2D point2d) {
		super(point2d, "Bat", 1);
		this.position = point2d;
	}

	@Override
	public String getName() {
		return "Bat";
	}

	@Override
	public int getLayer() {
		return 2;
	}

	@Override
	public boolean isTransposable() {
		return true;
	}

	@Override
	public Point2D getPosition() {
		return position;
	}
	
	@Override
	public void setPosition(Point2D position) {
		this.position = position;
	}
	

	public void move(Direction direction) {
		position = position.plus(direction.asVector());
	}
	
	public void moveRandomly() {
		Direction[] directions = {Direction.LEFT, Direction.RIGHT};
		Direction down = Direction.DOWN;
		Direction up = Direction.UP;
		Direction randomDirection = directions[random.nextInt(directions.length)];
		
		Point2D positionBelow = position.plus(down.asVector());
		GameElement elementBelow = GameEngine.getInstance().getCurrentRoom().getElementAt(positionBelow);
		
		if(elementBelow instanceof Stairs) {
			Point2D newPosition = positionBelow;
			if(Room.isPositionValid(newPosition)) {
				GameElement nextElement = GameEngine.getInstance().getCurrentRoom().getElementAt(newPosition); 
				if (nextElement == null || !(nextElement instanceof Intransposable) || ((Intransposable) nextElement).isTransposable()) { 
					move(down); 
					setPosition(newPosition);

				}
			}		
		} else {
		
			Point2D newPosition = position.plus(randomDirection.asVector()); 
			if (Room.isPositionValid(newPosition)) { 
				GameElement nextElement = GameEngine.getInstance().getCurrentRoom().getElementAt(newPosition);
				if(nextElement instanceof Manel) {
					interact((Manel) nextElement);
				} else if (!(nextElement instanceof Intransposable) || ((Intransposable) nextElement).isTransposable()) {
					move(randomDirection); 
					setPosition(newPosition); 
				} 
			}
		}
	}
	
	
	@Override
	public void interact(Manel manel) {
		if(!isDead) {
			manel.setVida(manel.getVida()-damage);			
			GameEngine.getInstance().getCurrentRoom().removeElementAt(this.getPosition(),this);
			GameEngine.getInstance().getGui().setStatusMessage("Ouch! Life: " + manel.getVida() + "/100");
			isDead = true;
		}
	}
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	public void takeDamage(int damage) {
//		if(this.temVida()) {
//			this.vida -= damage;
//			GameEngine.getInstance().getGui().setStatusMessage("DonnkeyKong was attacked! Life: " + this.getVida() + "/100");
//			if(this.vida <=0) {
//				GameEngine.getInstance().getCurrentRoom().removeElementAt(this.getPosition(),this );
//				System.out.println("Gorilla foi derrotado!");
//				GameEngine.getInstance().getGui().setStatusMessage("DonkeyKong killed!");
//			}
//		}
//	}
	
//	public void attack(Manel manel) {
//		if(this.temVida()) {
//			manel.setVida(manel.getVida()-this.damage);
//			GameEngine.getInstance().getGui().setStatusMessage("Manel was attacked! Life: " + manel.getVida() + "/100");
//			System.out.println("Gorilla atacou o Manel! Dano causado: " + damage);
//			if(manel.getVida()<=0) {
//				manel.semVida();
//			}
//		}
//	}
	
	
	
}
