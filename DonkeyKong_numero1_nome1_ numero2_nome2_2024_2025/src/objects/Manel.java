package objects;

//import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.game.*;
//import java.util.List;
//import objects.Intransposable;
//import objects.Gorilla;


public class Manel extends GameElement implements ImageTile, MovableObject, Intransposable {

	private Point2D position;
	private int vida = 100;
	private int damage = 10; 
	private boolean hasBife = false;
	private boolean hasSword = false;
	private GameEngine gameEngine;
	private boolean hasBomb = false;


	
	public Manel(Point2D initialPosition, GameEngine gameEngine){
		super(initialPosition, "Manel",0);
		position = initialPosition;
		this.gameEngine = gameEngine;

	}
	
	@Override
	public String getName() {
		return "JumpMan";
	}

	@Override
	public Point2D getPosition() {
		return position;
	}
	
	@Override
	public int getLayer() {
		// TODO Auto-generated method stub
		return 2;
	}
	
	@Override
	public boolean isTransposable() {
		return false;
	}
	
	public void setPosition(Point2D newPosition) { 
		this.position = newPosition; 
		System.out.println("New Manel position defined: " + this.position); 
	}
	
	public void setGameEngine(GameEngine gameEngine) {
		this.gameEngine = gameEngine;
	}
	
	
	public int getVida() {
		return vida;
	}
	
	public void setVida(int vida) {
		this.vida = vida;
	}
	
	public boolean temVida() {
		return this.getVida()>0;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}

	public void semVida() {
		if(vida <= 0) {
			if(gameEngine != null) {
				gameEngine.restartGame();
				vida = 100;
			} else {
				System.out.println("gameEngine not defined");
			}
		}
	}
	
	public void endGameLose() {
		//falta codigo
	}
	
	
	public boolean hasBife() {
		return hasBife;
	}
	
	public boolean hasSword() {
		return hasSword;
	}
	
	public void setHasSword(boolean hasSword) {
		this.hasSword = hasSword;
	}
	
	public void attack(Gorilla gorilla) {
		if(this.temVida()) {	
			if(hasSword) {
				gorilla.takeDamage(damage);
			} else {
				gorilla.takeDamage(damage);
			}
			System.out.println("Manel attacked Gorila. Damage caused: " + damage);
		}
	}
	
	
	
	
		//providencia uma direcao especifica como argumento

//	public void move(Direction direction) {
//		position = position.plus(direction.asVector());	
//	}
	
	
	
	public void move(Direction direction) {
		Point2D newPosition = position.plus(direction.asVector());
		GameElement elementAtNewPosition = GameEngine.getInstance().getCurrentRoom().getElementAt(newPosition);
		
		if(elementAtNewPosition instanceof Princess) {
			gameEngine.foundPrincess();
		} else {
			position = newPosition;
		}
		GameEngine.getInstance().getGui().update();
	}
	
	

			
	public void setHasBomb(boolean hasBomb) {
		this.hasBomb = hasBomb;
	}
	public void dropBomb() {
		if(hasBomb) {
			Bomb bomb = new Bomb(getPosition());
			bomb.activate();
			GameEngine.getInstance().getCurrentRoom().addGameElement(bomb);
			hasBomb = false;
		}
	}
	
	
//	public void foundPrincess() {
//		GameEngine gameEngine = GameEngine.getInstance();
//		GameEngine.getInstance().getGui().setStatusMessage("Jogador venceu!");
//		GameEngine.getInstance().getGui().showMessage("Fim do jogo!", "Jogador encontrou a Princessa!");
//		gameEngine.restartLevel();
//	}
}
