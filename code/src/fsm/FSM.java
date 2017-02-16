package fsm;

import java.util.*;

/**
 * Stavovy automat so zasobnikom,
 * pushuje a popuje stavy do zasobnika.
 *
 * Stavy by mali pushovat ine stavy na zasobnik
 * a sami seba popnut.
 */

public class FSM {
	
	private Stack<FSMState> stateStack = new Stack<FSMState>();
	
	public void Update(Object gameObject){
		if (stateStack.peek() != null){
			stateStack.peek().Update(this, gameObject);
		}
	}
	
	public void pushState(FSMState state){
		stateStack.push(state);
	}
	
	public void popState(){
		stateStack.pop();
	}
}
