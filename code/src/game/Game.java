package game;

import java.util.Stack;
import goap.*;

public class Game {
	
	public static void main(String[] args) {
		GameController cont = new GameController();
		cont.loadProviders();
		cont.setNewAgentGoal();
		Stack<Action> stack = cont.getHero().remainingActions;
		while (!stack.isEmpty()){
			System.out.println(stack.pop().print());
		}
	}

}
