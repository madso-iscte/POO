package objects;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;
import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;

public class Bomb extends GameElement implements Intransposable, Interactable {
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

    @Override
    public boolean isTransposable() {
        return !isActive;
    }

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

    @Override
    public void interact(Manel manel) {
        if (!isPickedUp && !hasBeenDropped) {
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
    				} else if (element instanceof Gorilla) {
    					Gorilla gorilla = (Gorilla) element; 
    					gorilla.setVida(gorilla.getVida() - 100);
    					currentRoom.removeElementAt(target, element);
    					GameEngine.getInstance().getGui().setStatusMessage("Gorila got hit by bomb!"); 
    					System.out.println("gorilla atingido");
    				} else if (element instanceof Bat) { 
    					GameEngine.getInstance().getGui().setStatusMessage("Morcego got hit by bomb!"); 
    					currentRoom.removeElementAt(target, element); 
    				} 
    			} //else {
//    				GameEngine.getInstance().getGui().setStatusMessage("Bomb didn´t hit anyone!");
//    				System.out.println("nada explodiu");
//    			}
    			 
    		} 
    	}
    	//GameEngine.getInstance().getGui().setStatusMessage("Bomb didn´t hit anyone!");
		System.out.println("nada explodiu");
    	currentRoom.removeElementAt(position, this); 
    	GameEngine.getInstance().getGui().update(); 
    }


    
    
    


}