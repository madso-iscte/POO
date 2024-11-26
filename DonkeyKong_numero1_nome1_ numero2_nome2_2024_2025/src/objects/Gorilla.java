package objects;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction; 
import pt.iscte.poo.utils.Point2D; 
import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;

import java.util.Random;

public class Gorilla extends GameElement implements ImageTile, Intransposable, MovableObject {
	
	private Point2D position;
	private Random random = new Random();
	private int vida = 100;
	private int damage = 10;
	
	public Gorilla(Point2D point2d) {
		super(point2d, "DonkeyKong", 1);
		this.position = point2d;
	}

	@Override
	public String getName() {
		return "DonkeyKong";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public boolean isTransposable() {
		return false;
	}

	@Override
	public Point2D getPosition() {
		return position;
	}
	
	@Override
	public void setPosition(Point2D position) {
		this.position = position;
	}
	
	public boolean temVida() {
		return this.getVida()>0;
	}
		
	public int getVida() {
		return vida;
	}
	
	public void move(Direction direction) {
		position = position.plus(direction.asVector());
	}
	
	public void moveRandomly() {
		Direction[] directions = {Direction.LEFT, Direction.RIGHT};
		Direction randomDirection = directions[random.nextInt(directions.length)];
		
		Point2D newPosition = position.plus(randomDirection.asVector()); 
		if (Room.isPositionValid(newPosition)) { 
			GameElement nextElement = GameEngine.getInstance().getCurrentRoom().getElementAt(newPosition);
			if(nextElement instanceof Manel) {
				attack((Manel) nextElement);
			} else if (!(nextElement instanceof Intransposable) || ((Intransposable) nextElement).isTransposable()) {
				move(randomDirection); 
				setPosition(newPosition); 
	    	} 
	    }
	}
		
	public void takeDamage(int damage) {
		if(this.temVida()) {
			this.vida -= damage;
			GameEngine.getInstance().getGui().setStatusMessage("DonnkeyKong was attacked! Life: " + this.getVida() + "/100");
			if(this.vida <=0) {
				GameEngine.getInstance().getCurrentRoom().removeElementAt(this.getPosition());
				System.out.println("Gorilla foi derrotado!");
				GameEngine.getInstance().getGui().setStatusMessage("DonkeyKong killed!");
			}
		}
}
	
	public void attack(Manel manel) {
		if(this.temVida()) {
			manel.setVida(manel.getVida()-this.damage);
			GameEngine.getInstance().getGui().setStatusMessage("Manel was attacked! Life: " + manel.getVida() + "/100");
			System.out.println("Gorilla atacou o Manel! Dano causado: " + damage);
			if(manel.getVida()<=0) {
				manel.semVida();
				GameEngine.getInstance().getCurrentRoom().removeElementAt(manel.getPosition());
			}
		}
	}
	
	public void lauchFire() {
		Point2D firePosition = new Point2D(position.getX(),position.getY()+1);
		if(temVida())
			if(Room.isPositionValid(firePosition)) {
				Fire fire = new Fire(firePosition);
				GameEngine.getInstance().getCurrentRoom().addGameElement(fire);
				System.out.println("Fire!");
			}
		}
	

	
}
