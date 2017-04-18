package game;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import actions.*;
import goap.*;
import providers.*;

public class GameController {
	private ArrayList<String> beings, places, items;
	private ArrayList<Provider> providers;
	private HashSet<Action> actions;
	private HashSet<Action> heroActions;
	private HashMap<String, Object> worldState;
	private HashMap<String, Object> goal;
	private Planner planner;
	private Provider activeProvider;
	private Random rnd;
	private Scanner in;
	
	public GameController() throws FileNotFoundException{
		planner = new Planner();
		worldState = new HashMap<String, Object>();
		goal = new HashMap<String, Object>();
		goal.put("princess", "saved");
		goal.put("dragon", "dead");
		providers = new ArrayList<Provider>();
		actions = new HashSet<Action>();
		heroActions = new HashSet<Action>();
		beings = new ArrayList<String>();
		places = new ArrayList<String>();
		items = new ArrayList<String>();
		in = new Scanner(System.in);
		rnd = new Random();
		loadProviders();
		loadBeigns();
		loadPlaces();
		loadItems();
		generateState();
		generateActions("prince");
	}
	
	private void loadProviders(){
		providers.add(new JailProvider());
		providers.add(new CaveProvider());
		providers.add(new GardenProvider());
	}
	
	private void loadBeigns() throws FileNotFoundException{
		Scanner scan = new Scanner(new FileReader("beings.txt"));
		while (scan.hasNextLine()){
			String line = scan.nextLine();
			beings.add(line);
		}
		scan.close();
	}
	
	private void loadPlaces() throws FileNotFoundException{
		Scanner scan = new Scanner(new FileReader("places.txt"));
		while (scan.hasNextLine()){
			String line = scan.nextLine();
			places.add(line);
		}
		scan.close();
	}
	
	private void loadItems() throws FileNotFoundException{
		Scanner scan = new Scanner(new FileReader("items.txt"));
		while (scan.hasNextLine()){
			String line = scan.nextLine();
			items.add(line);
		}
		scan.close();
	}
	
	private void generatePaths(){
		for (int i = 0; i < places.size() - 1; i++) {
			for (int j = i + 1; j < places.size(); j++) {
				if (rnd.nextInt(3) == 0){
					if (!worldState.containsKey("from" + places.get(i) + "to")){
						worldState.put("from" + places.get(i) + "to", new ArrayList<String>());
					}
					if (!worldState.containsKey("from" + places.get(j) + "to")){
						worldState.put("from" + places.get(j) + "to", new ArrayList<String>());
					}
					ArrayList<String> toListFrom = (ArrayList<String>) worldState.get("from" + places.get(i) + "to");
					ArrayList<String> toListTo = (ArrayList<String>) worldState.get("from" + places.get(j) + "to");
					if(places.get(i).equals("bridge") && toListFrom.size() >= 2){
						break;
					}
					if(places.get(j).equals("bridge") && toListTo.size() >= 2){
						break;
					}
					if(toListFrom.size() >= 3){
						break;
					}
					toListFrom.add(places.get(j));
					toListTo.add(places.get(i));
				}
			}
		}
	}
	
	private void generatePathsFull(){
		for (int i = 0; i < places.size(); i++) {
			for (int j = 0; j < places.size(); j++) {
				if (i != j){
					if (!worldState.containsKey("from" + places.get(i) + "to")){
						worldState.put("from" + places.get(i) + "to", new ArrayList<String>());
					}
						ArrayList<String> toList = (ArrayList<String>) worldState.get("from" + places.get(i) + "to");
						toList.add(places.get(j));
				}
			}
		}
	}
	
	private void generateBeings(){
		worldState.put("prince", "alive");
		worldState.put("princeplace", "castle");
		worldState.put("princeholds", new ArrayList<String>());
		worldState.put("princess", "alive");
		worldState.put("princessplace", places.get(rnd.nextInt(places.size())));
		worldState.put("princessholds", new ArrayList<String>());
		for (int i = 0; i < beings.size(); i++) {
			if (rnd.nextBoolean()){
				worldState.put(beings.get(i), "alive");
				worldState.put(beings.get(i)+"place", places.get(rnd.nextInt(places.size())));
				worldState.put(beings.get(i)+"holds", new ArrayList<String>());
			} else {
				worldState.put(beings.get(i), "dead");
				worldState.put(beings.get(i)+"place", places.get(rnd.nextInt(places.size())));
				worldState.put(beings.get(i)+"holds", new ArrayList<String>());
			}
		}
		worldState.put("dragon", "alive");
		beings.add("prince");
		beings.add("princess");
	}
	
	private void generateItems(){
		for (int i = 0; i < items.size(); i++) {
			if (rnd.nextBoolean()){
				worldState.put(items.get(i), "placed");
				worldState.put(items.get(i)+"place", places.get(rnd.nextInt(places.size())));
			} else {
				worldState.put(items.get(i), "notplaced");
				worldState.put(items.get(i)+"place", places.get(rnd.nextInt(places.size())));
			}
		}
		worldState.put("sword", "placed");
	}
	
	private void generateState(){
		worldState = new HashMap<String, Object>();
		generatePaths();
		generateBeings();
		generateItems();
	}
	
	private void generateActions(String hero){
		//move
		for (int i = 0; i < places.size(); i++) {
			for (int j = 0; j < beings.size(); j++) {
				if (beings.get(j).equals(hero)){
					heroActions.add(new Move(beings.get(j), places.get(i), worldState));
				}
				actions.add(new Move(beings.get(j), places.get(i), worldState));
			}
		}
		//kill
		for (int i = 0; i < beings.size(); i++) {
			for (int j = 0; j < beings.size(); j++) {
				if (i != j){
					if (beings.get(i) == hero){
						heroActions.add(new Kill(beings.get(i), beings.get(j), worldState));
					}
					actions.add(new Kill(beings.get(i), beings.get(j), worldState));
				}
			}
		}
		//save
		for (int i = 0; i < beings.size(); i++) {
			for (int j = 0; j < beings.size(); j++) {
				if (i != j){
					if (beings.get(i) == hero){
						heroActions.add(new Save(beings.get(i), beings.get(j), worldState));
					}
					actions.add(new Save(beings.get(i), beings.get(j), worldState));
				}
			}
		}
		//pickup
		for (int i = 0; i < items.size(); i++) {
			for (int j = 0; j < beings.size(); j++) {
				if (beings.get(j).equals(hero)){
					heroActions.add(new PickUp(beings.get(j), items.get(i), worldState));
				}
				actions.add(new PickUp(beings.get(j), items.get(i), worldState));
			}
		}
	}
	
	private HashSet<Action> getAvailableActions(){
		HashSet<Action> availableActions = new HashSet<Action>();
		for (Action a : actions){
			a.setState(worldState);
			if (planner.inState(a.getPreconditions(), worldState)){
				availableActions.add(a);
			}
		}
		return availableActions;
	}
	
	private HashSet<Action> getAvailableHeroActions(){
		HashSet<Action> availableActions = new HashSet<Action>();
		for (Action a : heroActions){
			a.setState(worldState);
			if (planner.inState(a.getPreconditions(), worldState)){
				availableActions.add(a);
			}
		}
		return availableActions;
	}
	
	private boolean checkGoal(){
		return planner.inState(goal, worldState);
	}
	
	public void setRandomActiveProvider(){
		activeProvider = providers.get(rnd.nextInt(providers.size()));
	}

	public void setNewAgentGoal(Agent agent){
		agent.setNewGoal(activeProvider.createGoalState());
	}
	
	public Stack<Action> findNewAgentPlan(Agent agent){
		return agent.findNewPlan(actions, worldState);
	}
	
	private void printState(HashMap<String, Object> state){
		for (Map.Entry<String, Object> tEntry : state.entrySet()){
			System.out.println(tEntry.getKey() + " ukazuje na " + tEntry.getValue());
		}
	}
	
	private void printWorldStatePaths(){
		for (Map.Entry<String, Object> tEntry : worldState.entrySet()){
			if (tEntry.getKey().contains("from")){
				System.out.println(tEntry.getKey() + " ukazuje na " + tEntry.getValue());
			}
		}
	}
	
	public void play(){
		System.out.println("STARTING GAME!!!");
		printWorldStatePaths();
		//printState(worldState);
		HashMap<String,Object> planningState = new HashMap<String, Object>(worldState);
		Stack<Action> plan = planner.plan(heroActions, planningState, goal);
		while(plan == null){
			generateState();
			planningState = new HashMap<String, Object>(worldState);
			plan = planner.plan(heroActions, planningState, goal);
		}
		while (!plan.isEmpty()){
			System.out.print(plan.pop().print() + ", ");
		}
		System.out.println();
		ArrayList<Action> availableActions = new ArrayList<Action>();
		while (!checkGoal()){
			availableActions = new ArrayList<Action>(getAvailableHeroActions());
			for (int i = 0; i < availableActions.size(); i++) {
				System.out.print(i + ": " + availableActions.get(i).print() + ", ");
			}
			System.out.println();
			int option = in.nextInt();
			if (option > -1 && option < availableActions.size()){
				worldState = planner.populateState(worldState, availableActions.get(option).getEffects());
			}
			/*
			if (heroActions.get(option).equals(stack.peek())){
				System.out.println("VYPISUJEM AKCIU ZO STACKU: " + stack.pop().print());
			}*/
		}
		System.out.println("Jupiiiiii!!!");
	}
	
}
