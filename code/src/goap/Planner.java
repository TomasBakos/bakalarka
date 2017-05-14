package goap;

import java.util.*;
import actions.*;

/**
 * Planuje ktore akcie mozu byt splnene aby
 * sme sa dostali do cieloveho stavu.
 *
 */
public class Planner {
	
	private int MAX_DEPTH = 50;
	boolean aStar;
	
	public Planner(boolean aStar){
		this.aStar = aStar;
	}
	
	/**
	 * Planovanie postupnosti akcii na splnenie ciela.
	 * Vracia null ak sa nevedel najst plan alebo zoznam
	 * akcii, ktore musia byt vykonane aby sa splnil ciel 
	 */
	public ArrayList<Action> plan(HashSet<Action> availableActions, HashMap<String,Object> worldState, HashMap<String,Object> goal){
		
		
		ArrayList<Node> leaves = new ArrayList<Node>();
		Node root = new Node(null, 0, worldState, null, 0, countUnsatisfiedGoals(goal, worldState));
		boolean success = false;
		if (aStar){
			HashSet<Node> visited = new HashSet<Node>();
			PriorityQueue<Node> queue = new PriorityQueue<Node>();
			queue.add(root);
			success = aStarSearch(queue, visited, leaves, availableActions, goal);
		} else {
			success = buildGraph(root, leaves, availableActions, goal);
		}
		
		if (success){
			Node best = leaves.get(0);
			for (Node n : leaves){
				if (best.cost > n.cost){
					best = n;
				}
			}
			ArrayList<Action> plan = new ArrayList<Action>();
			while (best.parent != null){
				plan.add(best.action);
				best = best.parent;
			}
			Collections.reverse(plan);
			return plan;
			
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
				Node node = new Node(parent, parent.cost+a.interestCost, currentState, a, parent.level + 1, 0);
				
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
		boolean found = false;
		while (!queue.isEmpty()){
			Node parent = queue.poll();
			visited.add(parent);

			if (inState(goal, parent.state)){
				leaves.add(parent);
				return true;	
			}

			for (Action a : usableActions){
				a.setState(parent.state);
				if (inState(a.getPreconditions(),parent.state)){
					HashMap<String, Object> currentState = populateState(parent.state, a.getEffects());
					Node node = new Node(parent, parent.cost+a.interestCost, currentState, a, parent.level + 1, countUnsatisfiedGoals(goal, currentState));
					
					boolean add = true;
					for (Node n : visited){
						if (inState(n.state,node.state)){
							add = false;
						}
					}
					
					boolean remove = false;
					boolean noAdd = false;
					Node toRemove = null;
					if (add){
						for (Node n : queue){
							if (inState(n.state, node.state)){
								if (n.compareTo(node) > 0){
									remove = true;
									toRemove = n;
								} else {
									noAdd = true;
								}
							}
						}
						if (remove){
							queue.remove(toRemove);
						}
						if (!noAdd){
							queue.add(node);
						}
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
	 * Skontroluje ze vsetky polozky z 'test' su v 'state'. Ak co len jedna nesedi alebo tam nieje
	 * vracia false.
	 */
	public boolean inState(HashMap<String,Object> test, HashMap<String,Object> state) {
		for (Map.Entry<String, Object> tEntry : test.entrySet()){
			if (!state.containsKey(tEntry.getKey())){
				return false;
			} else {
				if (state.get(tEntry.getKey()) instanceof ArrayList<?>){
					ArrayList<String> stateList = (ArrayList<String>) state.get(tEntry.getKey());
					if (tEntry.getValue() instanceof ArrayList<?>){
						if (!checkArrays(stateList, (ArrayList<String>)tEntry.getValue())){
							return false;
						}
					} else {
						if(!stateList.contains((String)tEntry.getValue())){
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
	
	//TODO: prerobit na Set-y nech sa to ta rychlo porovnat
	private boolean checkArrays(ArrayList<String> array1, ArrayList<String> array2){;
		for (String s : array1){
			if (!array2.contains(s)){
				return false;
			}
		}
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
	
	public int ratePlan(ArrayList<Action> plan){
		if (plan == null){
			return Integer.MIN_VALUE;
		}
		int planRating = 100;
		for (int i = 0; i < plan.size(); i++) {
			if (plan.get(i) instanceof Move){
				int lastIndex = i+1;
				while (plan.get(lastIndex) instanceof Move){
					lastIndex++;
				}
				//System.out.println("Odcitavam za viacnasobny Move: " + (lastIndex-i-1));
				planRating -= lastIndex-i-1;
				i = lastIndex;
			}
		}
		
		for (int i = 0; i < plan.size(); i++) {
			if (plan.get(i) instanceof PickUp){
				if (plan.get(i+1) instanceof Kill || plan.get(i+1) instanceof Trade){
					//System.out.println("Odcitavam za PickUp: " + (3));
					planRating -= 5;
				}
				if (plan.get(i+1) instanceof PickUp){
					planRating -= 3;
				}
			}
		}
		
		for (int i = 0; i < plan.size(); i++) {
			if (plan.get(i) instanceof Kill || plan.get(i) instanceof Trade || plan.get(i) instanceof Solve){
				if (plan.get(i+1) instanceof Kill || plan.get(i+1) instanceof Trade || plan.get(i+1) instanceof Solve){
					//System.out.println("Odcitavam za zdvojeny Kill/Trade/Solve: " + (5));
					planRating -= 5;
				}
				if (plan.get(i+1) instanceof PickUp){
					//System.out.println("Odcitavam za PickUp po Kill/Trade/Solve: " + (3));
					planRating -= 3;
				}
			}
		}
		
		ArrayList<Action> partialPlan = new ArrayList<Action>();
		for (int i = 0; i < plan.size(); i++) {
			if (plan.get(i) instanceof Kill || plan.get(i) instanceof Solve){
				partialPlan.add(plan.get(i));
			}
		}
		
		for (int i = 0; i < partialPlan.size() - 1; i++) {
			if (partialPlan.get(i) instanceof Kill){
				int lastIndex = i+1;
				while (lastIndex < partialPlan.size() && partialPlan.get(lastIndex) instanceof Kill){
					lastIndex++;
				}
				//System.out.println("Odcitavam za vianasobny Kill: " + ((lastIndex-i-1) * 5));
				planRating -= (lastIndex-i-1) * 5;
				i = lastIndex - 1;
			} else if (partialPlan.get(i) instanceof Solve){
				int lastIndex = i+1;
				while (lastIndex < partialPlan.size() && partialPlan.get(lastIndex) instanceof Solve){
					lastIndex++;
				}
				//System.out.println("Odcitavam za vianasobny Solve: " + ((lastIndex-i-1) * 5));
				planRating -= (lastIndex-i-1) * 5;
				i = lastIndex - 1;
			}
		}
		//System.out.println("------------------------------");
		
		return planRating;
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
