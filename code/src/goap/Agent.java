package goap;

import java.util.*;

public class Agent {
	private HashSet<Action> availableActions;
	private Stack<Action> remainingActions;
	private HashMap<String, Object> worldState;
	
	private IGoap dataProvider; //implementacia classy ktora poskytuje data o svete a pocuva feedback planovania
	
	private Planner planner;
	
	public void start(){
		planner = new Planner();
		getDataProvider();
		loadActions();
	}
	
	public void addAction(Action a) {
		availableActions.add(a);
	}
	
	public void removeAction(Action action) {
		availableActions.remove(action);
	}
	
	private boolean hasActionPlan(){
		return remainingActions.size() > 0;
	}
	
	/**
	 * Nastavi data providera
	 */
	private void getDataProvider() {
	}
	
	/**
	 * Nacita akcie, ktore sa aktualne mozu vykonat.
	 */
	private void loadActions (){
	}
}
