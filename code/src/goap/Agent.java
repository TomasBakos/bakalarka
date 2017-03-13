package goap;

import java.util.*;
import actions.*;

public class Agent {
	private HashSet<Action> availableActions;
	public Stack<Action> remainingActions;
	private HashMap<String, Object> worldState;
	
	private IGoap dataProvider; //implementacia classy ktora poskytuje data o svete a pocuva feedback planovania
	
	private Planner planner;
	
	public Agent(){
		planner = new Planner();
		loadActions();
	}
	
	public void addAction(Action a) {
		availableActions.add(a);
	}
	
	public void removeAction(Action action) {
		availableActions.remove(action);
	}
	
	public boolean hasActionPlan(){
		return remainingActions.size() > 0;
	}
	
	/**
	 * Nastavi data providera
	 */
	public void setDataProvider(IGoap prov) {
		dataProvider = prov;
	}
	
	public void findNewGoal(){
		worldState = dataProvider.getWorldState();
		remainingActions = planner.plan(this, availableActions, worldState, dataProvider.createGoalState());
	}
	
	/**
	 * Nacita akcie, ktore sa aktualne mozu vykonat.
	 */
	public void loadActions (){
		availableActions = new HashSet<Action>();
		availableActions.add(new MoveToCastleFromGarden());
		availableActions.add(new MoveToCastleFromVillage());
		availableActions.add(new MoveToGarden());
		availableActions.add(new MoveToJail());
		availableActions.add(new MoveToVillageFromCastle());
		availableActions.add(new MoveToVillageFromCave());
		availableActions.add(new MoveToCave());
		availableActions.add(new MoveToCastleFromJail());
		availableActions.add(new KillDragon());
	}
}
