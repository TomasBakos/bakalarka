package goap;

import java.util.*;
import fsm.*;

public class Agent {
	private FSM stateMachine;
	
	private FSMState idleState; //najdi co mas robit
	private FSMState moveToState; // pohni sa k cielu
	private FSMState performActionState; // vykonaj akciu
	
	private HashSet<Action> availableActions;
	private Queue<Action> currentActions;
	
	private IGoap dataProvider; //implementacia classy ktora poskytuje data o svete a pocuva feedback planovania
	
	private Planner planner;
	
	void Start(){
		stateMachine = new FSM();
		availableActions = new HashSet<Action>();
		currentActions = new LinkedList<Action>();
		planner = new Planner();
		findDataProvider();
		createIdleState();
		createMoveToState();
		createPerformActionState();
		stateMachine.pushState(idleState);
		loadActions();
	}
	
	public void addAction(Action a) {
		availableActions.add(a);
	}
	
	//todo
	public Action getAction(Action action) {
		return null;
	}
	
	public void removeAction(Action action) {
		availableActions.remove(action);
	}
	
	private boolean hasActionPlan(){
		return currentActions.size() > 0;
	}
	
	//todo
	private void createIdleState() {
	}
	
	//todo
	private void createMoveToState() {
	}
	
	//todo
	private void createPerformActionState() {
	}
	
	//todo
	private void findDataProvider() {
	}
	
	//todo
	private void loadActions (){
	}
}
