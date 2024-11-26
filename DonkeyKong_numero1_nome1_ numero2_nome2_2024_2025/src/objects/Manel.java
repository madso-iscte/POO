package objects;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
//import pt.iscte.poo.utils.*;
import pt.iscte.poo.game.*;

public class Manel extends GameElement implements ImageTile, MovableObject, Intransposable {

	private Point2D position;
	private int vida = 100;
	private int damage = 10; 
	private boolean hasBife = false;
	private boolean hasSword = false;
	
	public Manel(Point2D initialPosition){
		super(initialPosition, "Manel",0);
		position = initialPosition;
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
			endGameLose();
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
			System.out.println("Manel atacou Gorilla! Dano causado: " + damage);
		}
	}
		//providencia uma direcao especifica como argumento

	public void move(Direction direction) {
		position = position.plus(direction.asVector());	
	}
	
}
