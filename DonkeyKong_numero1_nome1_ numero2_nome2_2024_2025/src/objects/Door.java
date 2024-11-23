package objects;

import pt.iscte.poo.utils.Point2D;

public class Door extends GameElement {
	
	
	private Point2D position;
	
    public Door(Point2D position) {
        super(position, "DoorClosed", 2); // Nome da imagem: "gorilla", camada: 2
    }
    
    @Override
	public String getName() {
		return "DoorClosed";
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
}
