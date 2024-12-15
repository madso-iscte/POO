package objects;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;

public class Bomb extends GameElement implements Interactable {
    private int ticksToExplode = 10; // 5 segundos, assumindo 10 ticks por segundo
    private boolean isActive = false;
    private boolean isPickedUp = false;
    private boolean hasBeenDropped = false;

    public Bomb(Point2D position) {
        super(position, "bomb", 1);
    }

    @Override
    public int getLayer() {
        return 1;
    }

//    @Override
//    public boolean isTransposable() {
//        return true;
//    }

    public void activate() {
        isActive = true;
        GameEngine.getInstance().addBomb(this);
    }

    public void tick() {
        if (isActive) {
            ticksToExplode--;
            if (ticksToExplode % 2 == 0) {
                GameEngine.getInstance().getGui().setStatusMessage("Bomb will explode in " + (ticksToExplode / 10.0) + " seconds!");
            }
            if (ticksToExplode <= 0) {
                explode();
            }
        }
    }

    public void checkCollisionWithManel() {
        Point2D manelPosition = GameEngine.getInstance().getManel().getPosition();
        if (getPosition().equals(manelPosition)) {
            interact(GameEngine.getInstance().getManel());
        }
    }

    public void checkCollisionWithEnemies() {
        Room currentRoom = GameEngine.getInstance().getCurrentRoom();
        GameElement elementAtPosition = currentRoom.getElementAt(getPosition());
        if (elementAtPosition instanceof Gorilla || elementAtPosition instanceof Bat) {
            explode(); // Explode imediatamente se pisada
        }
    }
    
    public void explodeManel() {
    	Room currentRoom = GameEngine.getInstance().getCurrentRoom();
        GameElement elementAtPosition = currentRoom.getElementAt(getPosition());
    	Point2D manelPosition = GameEngine.getInstance().getManel().getPosition();
    	if(this.getPosition() != manelPosition) {
    		if(elementAtPosition instanceof Manel) {
    			explode();
    		}
    	}
    }

    @Override
    public void interact(Manel manel) {
        if (!isPickedUp && !hasBeenDropped && !isActive) {
            isPickedUp = true;
            isActive = false;
            manel.setHasBomb(true);
            GameEngine.getInstance().getCurrentRoom().removeElementAt(getPosition(), this);
            GameEngine.getInstance().getGui().setStatusMessage("Bomb picked up!");
        }
    }

    public void drop(Point2D position) {
        isPickedUp = false;
        isActive = true;
        hasBeenDropped = true;
        setPosition(position);
        GameEngine.getInstance().getCurrentRoom().addGameElement(this);
    }



    public void explode() { 
    	Point2D position = getPosition(); 
    	Room currentRoom = GameEngine.getInstance().getCurrentRoom();
    	boolean hitSomething = false;
    	
    	List<Blast> blasts = new ArrayList<>();
    		
    	for(int dx = -1; dx<= 1; dx++) {
    		for(int dy = -1; dy<=1; dy++) {
    			Point2D target = position.plus(new Vector2D(dx,dy));
    			Blast blast = new Blast(target);
    			currentRoom.addGameElement(blast);
    			blasts.add(blast);
    		}
    	}   	

    	for (int dx = -1; dx <= 1; dx++) { 
    		for (int dy = -1; dy <= 1; dy++) { 
    			Point2D target = position.plus(new Vector2D(dx, dy));
    			GameElement element = currentRoom.getElementAt(target);
    			if (element != null && !(element instanceof Wall) && !(element instanceof Stairs)) { 
    				if (element instanceof Manel) { 
    					Manel manel = (Manel) element;
    					manel.setVida(manel.getVida()-100);
    					manel.semVida();
    					//System.out.println("manel atingido");
    					GameEngine.getInstance().getGui().setStatusMessage("Manel got hit by bomb!");
    					hitSomething = true;
    				} else if (element instanceof Gorilla) {
    					Gorilla gorilla = (Gorilla) element; 
    					gorilla.setVida(gorilla.getVida() - 100);
    					currentRoom.removeElementAt(target, element);
    					GameEngine.getInstance().getGui().setStatusMessage("Bomb hit Gorilla!");
    					hitSomething = true;
    					System.out.println("gorilla atingido");
    				} else if (element instanceof Bat) { 
    					GameEngine.getInstance().getGui().setStatusMessage("Bomb hit Bat!"); 
    					currentRoom.removeElementAt(target, element);
    					hitSomething = true;
    				} else if (element instanceof Trap) { 
    					GameEngine.getInstance().getGui().setStatusMessage("Bomb hit Trap!"); 
    					currentRoom.removeElementAt(target, element);
    					hitSomething = true;
    				}
    			} 		 
    		} 
    	}
    	if(!hitSomething) {
    		GameEngine.getInstance().getGui().setStatusMessage("Bomb didn´t hit anyone!");
    	}
    	currentRoom.removeElementAt(position, this);
  	
    	Timer timer = new Timer(500, new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			for(Blast blast : blasts) {
    				currentRoom.removeElementAt(blast.getPosition(), blast);
    			}
    			GameEngine.getInstance().getGui().update();
    		}
    	});
    	timer.setRepeats(false);
    	timer.start();
    	
    	GameEngine.getInstance().getGui().update(); 
    }


    
    
    


}