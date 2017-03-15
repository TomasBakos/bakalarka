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
	 */
	public Stack<Action> plan(Object agent, HashSet<Action> availableActions, 
			HashMap<String,Object> worldState, HashMap<String,Object> goal){
		
		for (Action a : availableActions) {
			a.doReset();
		}
		
		ArrayList<Node> leaves = new ArrayList<Node>();
		Node root = new Node(null,0,worldState,null);
		boolean success = buildGraph(root, leaves, availableActions, goal);
		
		if (!success){
			return null;
		} else {
			Node best = leaves.get(0);
			for (Node n : leaves){
				if (best.cost > n.cost){
					best = n;
				}
			}
			Stack<Action> stack = new Stack<Action>();
			while (best != null){
				if (best.action != null){
					stack.push(best.action);
				}
				best = best.parent;
			}
			return stack;
		}
	}
	
	/**
	 * Vrati true ako sa naslo aspon jedno riesenie.
	 * Vsetky mozne cesty su ulozene v zozname listov.
	 * Kazdy list ma cenu a ten s najmensou cenu bude
	 * najlepsi.
	 */
	private boolean buildGraph (Node parent, List<Node> leaves, HashSet<Action> usableActions, HashMap<String, Object> goal){
		boolean found = false;
		
		for (Action a : usableActions){
			if (inState(a.getPreconditions(),parent.state)){
				HashMap<String, Object> currentState = populateState(parent.state, a.getEffects());
				Node node = new Node(parent, parent.cost+a.interest, currentState, a);
				
				if (inState(goal, currentState)){
					leaves.add(node);
					found = true;
				} else {
					HashSet<Action> subset = actionSubset(usableActions, a);
					if (buildGraph(node, leaves, subset, goal)){
						found = true;
					}
				}
			}
		}
		return found;
	}
	
	/**
	 * Vytvori podmnozinu akcii bez akcie removeMe. Vytvara novu mnozinu.
	 */
	private HashSet<Action> actionSubset(HashSet<Action> actions, Action removeMe) {
		HashSet<Action> subset = new HashSet<Action>();
		for (Action a : actions){
			if(!a.equals(removeMe)){
				subset.add(a);
			}
		}
		return subset;
	}
	
	/**
	 * Skontroluje ze vsetky polozky z 'test' su v 'state'. Ak co len jedna nesedi alebo tam nieje
	 * vracia false.
	 */
	public boolean inState(HashMap<String,Object> test, HashMap<String,Object> state) {
		for (Map.Entry<String, Object> tEntry : test.entrySet()){
			if (!state.containsKey(tEntry.getKey())){
				return false;
			} else {
				if (!tEntry.getValue().equals(state.get(tEntry.getKey()))){
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Aplikuje stateChange na currentState.
	 */
	public HashMap<String,Object> populateState(HashMap<String,Object> currentState, HashMap<String,Object> stateChange) {
		HashMap<String,Object> state = new HashMap<String,Object>();
		for (Map.Entry<String, Object> entry : currentState.entrySet()){
			state.put(entry.getKey(),entry.getValue());
		}
		
		for (Map.Entry<String, Object> entry : stateChange.entrySet()){
			if (state.containsKey(entry.getKey())){
				state.replace(entry.getKey(), entry.getValue());
			} else {
				state.put(entry.getKey(), entry.getValue());
			}
		}
		
		return state;
	}
	
	private class Node {
		public Node parent;
		public float cost;
		public HashMap<String,Object> state;
		public Action action;

		public Node(Node parent, float cost, HashMap<String,Object> state, Action action) {
			this.parent = parent;
			this.cost = cost;
			this.state = state;
			this.action = action;
		}
	}
}
