package game;

import java.io.FileNotFoundException;

public class Game {
	
	public static void main(String[] args) {
		GameController cont;
		try {
			cont = new GameController();
			cont.play();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
