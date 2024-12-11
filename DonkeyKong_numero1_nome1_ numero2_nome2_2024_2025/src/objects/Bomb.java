package objects;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;
import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;

public class Bomb extends GameElement implements Intransposable {

    private int ticksToExplode = 50; // 5 segundos, assumindo 10 ticks por segundo
    private boolean isActive = false;
    private boolean isPickedUp = false;

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
        if (isActive && !isPickedUp) {
            ticksToExplode--;
            if (ticksToExplode <= 0) {
                explode();
            }
        }
    }

    public void pickUp() {
        isPickedUp = true;
        isActive = false;
        GameEngine.getInstance().getCurrentRoom().removeElementAt(this.getPosition(), this);
    }

    public void drop(Point2D position) {
        isPickedUp = false;
        isActive = true;
        setPosition(position);
        GameEngine.getInstance().getCurrentRoom().addGameElement(this);
    }

    public void checkCollisionWithManel() {
        Point2D manelPosition = GameEngine.getInstance().getManel().getPosition();
        if (getPosition().equals(manelPosition)) {
            pickUp();
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
                    currentRoom.removeElementAt(target, element);
                }
            }
        }

        if (GameEngine.getInstance().getManel().getPosition().distanceTo(position) <= 1) {
            GameEngine.getInstance().getManel().setVida(GameEngine.getInstance().getManel().getVida() - 1);
        }

        currentRoom.removeElementAt(position, this);
        GameEngine.getInstance().getGui().update();
    }
}