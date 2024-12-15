package objects;

import java.util.List;
import java.util.stream.Collectors;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Fire extends GameElement implements ImageTile, Intransposable, MovableObject{
	
	private Point2D position;
	private int damage = 10;
	//private int currentLevel;
	
	
	public Fire(Point2D initialPosition) {
		super(initialPosition, "Fire",2);
		this.position = initialPosition;
		//this.currentLevel = GameEngine.getInstance().getCurrentRoom().getLevelNumber();
	}

	
	@Override
	public String getName() {
		return "Fire";
	}

	@Override
	public int getLayer() {
		return 2;
	}
	
	@Override
	public Point2D getPosition() {
		return position;
	}
	
	@Override
	public void setPosition(Point2D position) {
		this.position = position;
	}

	@Override
	public boolean isTransposable() {
		return true;
	}
	
	@Override
	public void move(Direction direction) {
		position = position.plus(direction.asVector());
	}
	
	public void moveDown() {
		move(Direction.DOWN);
	}


	public void checkCollisionWithManel() {
		GameElement elementAtPosition = GameEngine.getInstance().getCurrentRoom().getElementAt(position);		
		
		if(elementAtPosition instanceof Manel) {
			Manel manel = (Manel) elementAtPosition;
			manel.setVida(manel.getVida()-this.damage);
			GameEngine.getInstance().getGui().setStatusMessage("Manel was attacked! Life " + manel.getVida() + "/100");
			System.out.println("Manel attacked by fire ball! Life: " + manel.getVida());
			
			GameEngine.getInstance().getCurrentRoom().removeElementAt(this.getPosition(), this);
			GameEngine.getInstance().getGui().removeImage(this);
			
			if(manel.getVida()<=0) {
				manel.semVida();
				//GameEngine.getInstance().getCurrentRoom().removeElementAt(manel.getPosition(),manel);
				//GameEngine.getInstance().getGui().removeImage(manel);
			}

		}	
	}
	
	

	
}
