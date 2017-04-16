package game;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import actions.*;
import goap.*;
import providers.*;

public class GameController {
	private Agent hero;
	private ArrayList<String> beings, places;
	private ArrayList<Provider> providers;
	private HashSet<Action> actions;
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
		providers = new ArrayList<Provider>();
		actions = new HashSet<Action>();
		beings = new ArrayList<String>();
		places = new ArrayList<String>();
		in = new Scanner(System.in);
		rnd = new Random();
		loadProviders();
		loadBeigns();
		loadPlaces();
		generateState();
		generateActions();
		//setRandomActiveProvider();
		hero = new Agent(actions);
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
	
	private void generatePaths(){
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
		worldState.put("princess", "alive");
		worldState.put("princessplace", places.get(rnd.nextInt(places.size())));
		for (int i = 0; i < beings.size(); i++) {
			if (rnd.nextBoolean()){
				worldState.put(beings.get(i), "alive");
				worldState.put(beings.get(i)+"place", places.get(rnd.nextInt(places.size())));
			} else {
				worldState.put(beings.get(i), "dead");
				worldState.put(beings.get(i)+"place", places.get(rnd.nextInt(places.size())));
			}
		}
		beings.add("prince");
		beings.add("princess");
	}
	
	private void generateState(){
		generatePaths();
		generateBeings();
	}
	
	private void generateActions(){
		//move
		for (int i = 0; i < places.size(); i++) {
			for (int j = 0; j < beings.size(); j++) {
				actions.add(new Move(beings.get(j), places.get(i), worldState));
			}
		}
		//kill
		for (int i = 0; i < beings.size(); i++) {
			for (int j = 0; j < beings.size(); j++) {
				if (i != j){
					actions.add(new Kill(beings.get(i), beings.get(j), worldState));
				}
			}
		}
		//save
		for (int i = 0; i < beings.size(); i++) {
			String being = beings.get(i);
			if (being != "prince"){
				actions.add(new Save(being, worldState));
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
	
	public Agent getHero() {
		return hero;
	}
	
	public void printAvailableHeroActions(){
		
	}
	
	private void printState(HashMap<String, Object> state){
		for (Map.Entry<String, Object> tEntry : state.entrySet()){
			System.out.println(tEntry.getKey() + " ukazuje na " + tEntry.getValue());
		}
	}
	
	public void play(){
		System.out.println("STARTING GAME!!!");
		//printState(worldState);
		HashMap<String,Object> planningState = new HashMap<String, Object>(worldState);
		Stack<Action> plan = planner.plan(actions, planningState, goal);
		while (!plan.isEmpty()){
			System.out.print(plan.pop().print() + ", ");
		}
		System.out.println();
		ArrayList<Action> heroActions = new ArrayList<Action>();
		while (!checkGoal()){
			heroActions = new ArrayList<Action>(getAvailableActions());
			for (int i = 0; i < heroActions.size(); i++) {
				System.out.print(i + ": " + heroActions.get(i).print() + ", ");
			}
			System.out.println();
			int option = in.nextInt();
			if (option > -1 && option < heroActions.size()){
				worldState = planner.populateState(worldState, heroActions.get(option).getEffects());
			}
			/*
			if (heroActions.get(option).equals(stack.peek())){
				System.out.println("VYPISUJEM AKCIU ZO STACKU: " + stack.pop().print());
			}*/
		}
		System.out.println("Jupiiiiii!!!");
	}
	
}
