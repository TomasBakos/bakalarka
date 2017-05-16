package game;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Game {
	
	public static void main(String[] args) {
		GameController cont;
		try {
			cont = new GameController();
			cont.play();
			//cont.testing();
			//cont.generateToFile();
			//cont.playFromFile("worlds/world0.json");
		} catch (FileNotFoundException e) {
			System.out.println("Some of the game files are missing");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
