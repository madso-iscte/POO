package objects;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;
import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;

public class Bomb extends GameElement implements Intransposable, Interactable {

	private int ticksToExplode = 20; // 5 segundos, assumindo 10 ticks por segundo
    private boolean isActive = false;
    private boolean isPickedUp = false;
    private boolean hasBeenDropped = false;

    public Bomb(Point2D position) {
        super(position, "Bomb", 1);
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
    
    public void checkCollisionWithEntities() {
        Room currentRoom = GameEngine.getInstance().getCurrentRoom();
        Point2D position = getPosition();
        GameElement element = currentRoom.getElementAt(position);

        if (element instanceof Gorilla || element instanceof Bat) {
            explode();
        }
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
                        manel.setVida(manel.getVida() - 100);
                        GameEngine.getInstance().getGui().setStatusMessage("Manel hit by bomb! Life: " + manel.getVida());
                        manel.semVida();
                    } else if (element instanceof Gorilla || element instanceof Bat) {
                        currentRoom.removeElementAt(target, element);
                        GameEngine.getInstance().getGui().setStatusMessage(element.getName() + " hit by bomb and removed!");
                    }
                }
            }
        }

        currentRoom.removeElementAt(position, this);
        GameEngine.getInstance().getGui().update();
    }
}