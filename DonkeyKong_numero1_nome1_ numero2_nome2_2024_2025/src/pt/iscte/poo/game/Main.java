package pt.iscte.poo.game;

import pt.iscte.poo.gui.ImageGUI;

public class Main {

	public static void main(String[] args) {
		//ImageGUI gui = ImageGUI.getInstance();
//		GameEngine engine = new GameEngine();
//		gui.setStatusMessage("DonkeyKong");
//		gui.registerObserver(engine);
//		gui.go();
//		engine.start();
		GameEngine engine = GameEngine.getInstance();
		engine.start();

	}
	
}
