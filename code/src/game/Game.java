package game;

import java.io.FileNotFoundException;

public class Game {
	
	public static void main(String[] args) {
		GameController cont;
		try {
			cont = new GameController();
			cont.play();
			//cont.testing();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
