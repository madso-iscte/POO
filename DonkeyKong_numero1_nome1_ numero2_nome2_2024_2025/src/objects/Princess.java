package objects;

import pt.iscte.poo.utils.Point2D;

public class Princess extends GameElement {

    public Princess(Point2D position) {
        super(position, "Princess", 1); // Nome da imagem: "princess", camada: 1
    }
    
    @Override
	public String getName() {
		return "Princess";
	}

	@Override
	public int getLayer() {
		return 1;
	}
}