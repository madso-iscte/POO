package objects;

import pt.iscte.poo.utils.*;
import java.util.List;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import objects.GameElement;
import pt.iscte.poo.game.GameEngine;


public abstract class MovableObject extends GameElement {

	public MovableObject(Point2D initialPosition, String imageName, int layer) {
		super(initialPosition, imageName, layer);
	}

	public boolean interactStatus=false;

	//verifica se a interacao com o interactable foi sucedida
	public boolean isInteractStatusDone() {
		return interactStatus;
	}

	public void setInteractStatus(boolean interactStatus) {
		this.interactStatus = interactStatus;
	}

	//move o movable dada uma direcao
	public boolean move(Direction d) {
		Point2D newPosition = getPosition().plus(d.asVector());
		//cria uma lista com os objetos numa dada posicao
		List<GameElement> elementsAtNextPosition = GameEngine.getInstance().getGameElement(newPosition);
		//o seguinte for define o nextGameElemnt conforme o GameElement que tiver a maior layer na lista elementsAtNextPosition
		GameElement nextGameElement = elementsAtNextPosition.get(0);
		for (GameElement e : elementsAtNextPosition) {
			if (e.getLayer() > nextGameElement.getLayer()) {
				nextGameElement = e;
			}
		}
		
		System.out.println("Objeto depois da caixa: " + nextGameElement.getName());
		//verifica se a posicao dada se encontra dentro dos limites
		if (newPosition.getX() >= 0 && newPosition.getX() < 10 && newPosition.getY() >= 0 && newPosition.getY() < 10) {
			
			//verifica se o game element eÂ´ um movable
			if (nextGameElement instanceof MovableObject) {
				return false;
			}
			
			//verifica se o game element eÂ´ um instransposable
			if (nextGameElement instanceof Intransposable) {
				return false;
			}
			
			//verifica se o game element eÂ´ um interactable
			if (nextGameElement instanceof Interactable) {
				if (handleInteractable((Interactable) nextGameElement, newPosition, d))
					return true;
			}
			
			//define a nova posicao do movable
			setPosition(newPosition);
			return true;
		}
		return false;
	}
	
	//lida como o interactable
	private boolean handleInteractable(Interactable a, Point2D position, Direction d) {
		a.interact(this,position,d);
		System.out.println("handleInteractable");
		if(isInteractStatusDone())
			return true;
		return false;
	}

}
