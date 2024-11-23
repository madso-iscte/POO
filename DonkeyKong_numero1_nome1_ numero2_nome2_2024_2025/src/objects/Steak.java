package objects;

import pt.iscte.poo.utils.Point2D;

public class Steak extends GameElement{
	
	 public Steak(Point2D position) {
	        super(position, "Steak", 1); // Nome da imagem: "sword", camada: 1
	    }
	    
	    @Override
		public String getName() {
			return "Steak";
		}

		@Override
		public int getLayer() {
			return 1;
		}

}
