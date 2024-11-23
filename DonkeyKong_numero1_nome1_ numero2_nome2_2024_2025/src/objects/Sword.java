package objects;

import pt.iscte.poo.utils.Point2D;

public class Sword extends GameElement {

    public Sword(Point2D position) {
        super(position, "Sword", 1); // Nome da imagem: "sword", camada: 1
    }
    
    @Override
	public String getName() {
		return "Sword";
	}

	@Override
	public int getLayer() {
		return 1;
	}
}