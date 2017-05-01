package goap;

import java.util.*;

/**
 * Planuje ktore akcie mozu byt splnene aby
 * sme sa dostali do cieloveho stavu.
 *
 */
public class Planner {
	
	private int MAX_DEPTH = 10;
	boolean aStar;
	
	public Planner(boolean aStar){
		this.aStar = aStar;
	}
	
	/**
	 * Planovanie postupnosti akcii na splnenie ciela.
	 * Vracia null ak sa nevedel najst plan alebo zoznam
	 * akcii, ktore musia byt vykonane aby sa splnil ciel 
	 */
	public Stack<Action> plan(HashSet<Action> availableActions, 
			HashMap<String,Object> worldState, HashMap<String,Object> goal){
		
		HashSet<Node> visited = new HashSet<Node>();
		PriorityQueue<Node> queue = new PriorityQueue<Node>();
		ArrayList<Node> leaves = new ArrayList<Node>();
		Node root = new Node(null, 0, worldState, null, 0, countUnsatisfiedGoals(goal, worldState));
		boolean success = false;
		if (aStar){
			queue.add(root);
			success = aStarSearch(queue, visited, leaves, availableActions, goal);
		} else {
			success = buildGraph(root, leaves, availableActions, goal);
		}
		
		if (success){
			Node best = leaves.get(0);
			for (Node n : leaves){
				//System.out.println("Nodes search");
				//System.out.println(n.cost);
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
		return null;
	}
	
	/**
	 * Vrati true ak sa naslo aspon jedno riesenie.
	 * Vsetky mozne cesty su ulozene v zozname listov.
	 * Kazdy list ma cenu a ten s najmensou cenu bude
	 * najlepsi.
	 */
	private boolean buildGraph (Node parent, List<Node> leaves, HashSet<Action> usableActions, HashMap<String, Object> goal){
		boolean found = false;
		
		for (Action a : usableActions){
			a.setState(parent.state);
			if (inState(a.getPreconditions(),parent.state)){
				HashMap<String, Object> currentState = populateState(parent.state, a.getEffects());
				Node node = new Node(parent, parent.cost+a.interest, currentState, a, parent.level + 1, 0);
				
				if (parent.level > MAX_DEPTH){
					return false;
				}
				
				if (inState(goal, currentState)){
					leaves.add(node);
					found = true;
				} else {
					if (buildGraph(node, leaves, usableActions, goal)){
						found = true;
					}
				}
			}
		}
		return found;
	}
	
	private boolean aStarSearch(PriorityQueue<Node> queue, HashSet<Node> visited, List<Node> leaves, HashSet<Action> usableActions, HashMap<String, Object> goal){
		//TODO doladit nechodi vzdy neviem preco
		boolean found = false;
		while (!queue.isEmpty()){
			Node parent = queue.poll();
			visited.add(parent);
			
			if (parent.level > MAX_DEPTH){
				return false;
			}

			if (inState(goal, parent.state)){
				leaves.add(parent);
				return true;	
			}

			for (Action a : usableActions){
				a.setState(parent.state);
				if (inState(a.getPreconditions(),parent.state)){
					HashMap<String, Object> currentState = populateState(parent.state, a.getEffects());
					Node node = new Node(parent, parent.cost+a.interest, currentState, a, parent.level + 1, countUnsatisfiedGoals(goal, currentState));
					
					boolean add = true;
					for (Node n : visited){
						if (inState(n.state,node.state)){
							if (n.compareTo(node) > 0){
								visited.remove(n);
							} else { 
								add = false;
							}
						}
					}
					if (add){
						queue.add(node);
					}
				}
			}

		}
		
		return found;
	}
	
	private int countUnsatisfiedGoals(HashMap<String, Object> goal, HashMap<String, Object> state){
		int result = 0;
		for (Map.Entry<String, Object> tEntry : goal.entrySet()){
			if (!state.containsKey(tEntry.getKey())){
				result++;
			} else {
				if (state.get(tEntry.getKey()) instanceof ArrayList<?>){
					ArrayList<String> list = (ArrayList<String>) state.get(tEntry.getKey());
					if(!list.contains((String)tEntry.getValue())){
						result++;
					}
				}else if (!tEntry.getValue().equals(state.get(tEntry.getKey()))){
					result++;
				}
			}
		}
		return result;
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
				if (state.get(tEntry.getKey()) instanceof ArrayList<?>){
					ArrayList<String> list = (ArrayList<String>) state.get(tEntry.getKey());
					if (tEntry.getValue() instanceof ArrayList<?>){
						if (!checkArrays(list, (ArrayList<String>)tEntry.getValue())){
							return false;
						}
					} else {
						if(!list.contains((String)tEntry.getValue())){
							return false;
						}
					}
				}else if (!tEntry.getValue().equals(state.get(tEntry.getKey()))){
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean checkArrays(ArrayList<String> array1, ArrayList<String> array2){
		for (String s : array2){
			if (!array1.contains(s)){
				return false;
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
	
	private class Node implements Comparable<Node>{
		public Node parent;
		public int cost;
		public int notSolved;
		public HashMap<String,Object> state;
		public Action action;
		public int level;

		public Node(Node parent, int cost, HashMap<String,Object> state, Action action, int level, int notSolved) {
			this.parent = parent;
			this.cost = cost;
			this.state = state;
			this.action = action;
			this.level = level;
			this.notSolved = notSolved;
		}

		@Override
		public int compareTo(Node node) {
			if (cost + notSolved < node.cost + node.notSolved){
				return -1;
			} else if (cost + notSolved == node.cost + node.notSolved){
				return 0;
			} else {
				return 1;
			}
		}
	}
}
