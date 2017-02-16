package goap;

import java.util.*;

/**
 * Planuje ktore akcie mozu byt splnene aby
 * sme sa dostali do cieloveho stavu.
 *
 */
public class Planner {
	
	/**
	 * Planovanie postupnosti akcii na splnenie ciela.
	 * Vracia null ak sa nevedel najst plan alebo zoznam
	 * akcii, ktore musia byt vykonane aby sa splnil ciel
	 * TODO 
	 */
	public Queue<Action> plan(Object agent, HashSet<Action> availableActions, 
			HashMap<String,Object> worldState, HashMap<String,Object> goal){
		return null;
	}
	
	/**
	 * Vrati true ako sa naslo aspon jedno riesenie.
	 * Vsetky mozne cesty su ulozene v zozname listov.
	 * Kazdy list ma cenu a ten s najmensou cenu bude
	 * najlepsi.
	 * TODO
	 */
	private boolean buildGraph (Node parent, List<Node> leaves, HashSet<Action> usableActions, HashMap<String, Object> goal){
		return false;
	}
	
	/**
	 * Vytvori podmnozinu akcii bez akcie removeMe. Vytvara novy mnozinu.
	 * TODO
	 */
	private HashSet<Action> actionSubset(HashSet<Action> actions, Action removeMe) {
		return null;
	}
	
	/**
	 * Skontroluje ze vsetky polozky v 'test' su v 'state'. Ak co len jedna nesedi alebo tam nieje
	 * vracia false.
	 * TODO
	 */
	private boolean inState(HashMap<String,Object> test, HashMap<String,Object> state) {
		return false;
	}
	
	/**
	 * Aplikuje stateChange na currentState.
	 */
	private HashMap<String,Object> populateState(HashMap<String,Object> currentState, HashMap<String,Object> stateChange) {
		return null;
	}
	
	private class Node {
		public Node parent;
		public float runningCost;
		public HashMap<String,Object> state;
		public Action action;

		public Node(Node parent, float runningCost, HashMap<String,Object> state, Action action) {
			this.parent = parent;
			this.runningCost = runningCost;
			this.state = state;
			this.action = action;
		}
	}
}
