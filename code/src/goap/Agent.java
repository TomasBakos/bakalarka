package goap;

import java.util.*;

public class Agent {
	private HashSet<Action> actions;
	private Stack<Action> remainingActions;
	private Planner planner;
	private HashMap<String, Object> goal;
	
	public Agent(HashSet<Action> actions){
		this.actions = actions;
		planner = new Planner(false);
	}
	
	/**
	 * Ma co agent este vykonavat?
	 */
	public boolean hasActionPlan(){
		return remainingActions.size() > 0;
	}
	
	
	/**
	 * Nastavi novy ciel agentovi
	 * @param goal je dany novy ciel.
	 */
	public void setNewGoal(HashMap<String, Object> goal){
		this.goal = goal;
	}
	
	/**
	 * Najde cestu k dodanemu cielu pomocou @param actions.
	 */
	public Stack<Action> findNewPlan(HashSet<Action> actions, HashMap<String, Object> worldState){
		remainingActions = planner.plan(actions, worldState, goal);
		return remainingActions;
	}
	
	public ArrayList<Action> getAvailableActions(HashMap<String, Object> worldState){
		ArrayList<Action> availableActions = new ArrayList<Action>();
		for (Action a : actions){
			if (planner.inState(a.getPreconditions(), worldState)){
				availableActions.add(a);
			}
		}
		return availableActions;
	}
}
