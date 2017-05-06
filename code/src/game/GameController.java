package game;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import actions.*;
import goap.*;

public class GameController {
	private final int HUB_COUNT = 4;
	private ArrayList<ArrayList<String>> hubs;
	private ArrayList<String> beings, friends, monsters, places, items, placedItems;
	private HashSet<Action> actions;
	private HashSet<Action> heroActions;
	private HashMap<String, Object> worldState;
	private HashMap<String, Object> goal;
	private Planner planner, aStarPlanner;
	private Random rnd;
	private Scanner in;
	
	public GameController() throws FileNotFoundException{
		planner = new Planner(false);
		aStarPlanner = new Planner(true);
		goal = new HashMap<String, Object>();
		goal.put("princess", "saved");
		beings = new ArrayList<String>();
		rnd = new Random();
		beings.add("prince");
		beings.add("princess");
		loadEntities();
		generateState();
		generateActions("prince");
	}
	
	private void loadEntities() throws FileNotFoundException{
		in = new Scanner(System.in);
		loadFriends();
		loadMonsters();
		loadPlaces();
		loadItems();
	}
	
	private void loadFriends() throws FileNotFoundException{
		friends = new ArrayList<String>();
		Scanner scan = new Scanner(new FileReader("friends.txt"));
		while (scan.hasNextLine()){
			String line = scan.nextLine();
			friends.add(line);
		}
		scan.close();
	}
	
	private void loadMonsters() throws FileNotFoundException{
		monsters = new ArrayList<String>();
		Scanner scan = new Scanner(new FileReader("monsters.txt"));
		while (scan.hasNextLine()){
			String line = scan.nextLine();
			monsters.add(line);
		}
		scan.close();
	}
	
	private void loadPlaces() throws FileNotFoundException{
		places = new ArrayList<String>();
		Scanner scan = new Scanner(new FileReader("places.txt"));
		while (scan.hasNextLine()){
			String line = scan.nextLine();
			places.add(line);
		}
		scan.close();
	}
	
	private void loadItems() throws FileNotFoundException{
		items = new ArrayList<String>();
		Scanner scan = new Scanner(new FileReader("items.txt"));
		while (scan.hasNextLine()){
			String line = scan.nextLine();
			items.add(line);
		}
		scan.close();
	}
	
	private void generatePaths(){
		for (int l = 0; l < hubs.size(); l++) {
			for (int i = 0; i < hubs.get(l).size() - 1; i++) {
				for (int j = i + 1; j < hubs.get(l).size(); j++) {
					if (rnd.nextInt(3) == 0){
						if (!worldState.containsKey("from" + hubs.get(l).get(i) + "to")){
							worldState.put("from" + hubs.get(l).get(i) + "to", new ArrayList<String>());
						}
						if (!worldState.containsKey("from" + hubs.get(l).get(j) + "to")){
							worldState.put("from" + hubs.get(l).get(j) + "to", new ArrayList<String>());
						}
						ArrayList<String> toListFrom = (ArrayList<String>) worldState.get("from" + hubs.get(l).get(i) + "to");
						ArrayList<String> toListTo = (ArrayList<String>) worldState.get("from" + hubs.get(l).get(j) + "to");
						if(hubs.get(l).get(i).equals("bridge") && toListFrom.size() >= 2){
							break;
						}
						if(hubs.get(l).get(j).equals("bridge") && toListTo.size() >= 2){
							break;
						}
						if(toListFrom.size() >= 3){
							break;
						}
						toListFrom.add(hubs.get(l).get(j));
						toListTo.add(hubs.get(l).get(i));
					}
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
	
	private void generateFriends(){
		ArrayList<String> friendsCopy = new ArrayList<String>(friends);
		for (int i = 0; i < hubs.size(); i++) {
			String friend = friendsCopy.get(rnd.nextInt(friendsCopy.size()));
			worldState.put(friend, "alive");
			
			String friendWants = placedItems.get(rnd.nextInt(placedItems.size()));
			worldState.put(friend+"wants", friendWants);
			goal.put(friend+"wants", "");
			
			String friendPlace = places.get(rnd.nextInt(places.size()));
			worldState.put(friend+"place", friendPlace);
			
			String itemPlace = places.get(rnd.nextInt(places.size()));
			worldState.put(friendWants+"place", itemPlace);
			
			worldState.put(friend+"holds", new ArrayList<String>());
			beings.add(friend);
			
			placedItems.remove(friendWants);
			friendsCopy.remove(friend);
		}
		for (int i = 0; i < friendsCopy.size(); i++) {
			worldState.put(friendsCopy.get(i), "dead");
			worldState.put(friendsCopy.get(i)+"holds", new ArrayList<String>());
		}
	}
	
	private void generateBeings(){
		worldState.put("prince", "alive");
		worldState.put("princeplace", hubs.get(0).get(rnd.nextInt(hubs.get(0).size())));
		worldState.put("princeholds", new ArrayList<String>());
		worldState.put("princess", "alive");
		worldState.put("princessplace", hubs.get(hubs.size() - 1).get(rnd.nextInt(hubs.get(hubs.size() - 1).size())));
		worldState.put("princessholds", new ArrayList<String>());
		generateFriends();
		generateMonsters();
	}
	
	private void generateItems(){
		ArrayList<String> itemsCopy = new ArrayList<String>(items);
		placedItems = new ArrayList<String>();
		for (int i = 0; i < (2 * HUB_COUNT) - 1; i++) {
			String item = itemsCopy.get(rnd.nextInt(itemsCopy.size()));
			worldState.put(item, "placed");
			placedItems.add(item);
			itemsCopy.remove(item);
		}
		worldState.put("coins", 0); 
	}
	
	private void generateHubs(){
		hubs = new ArrayList<ArrayList<String>>();
		ArrayList<String> placesCopy = new ArrayList<String>(places);
		for (int i = 0; i < HUB_COUNT; i++) {
			hubs.add(new ArrayList<String>());
			String place = placesCopy.get(rnd.nextInt(placesCopy.size()));
			hubs.get(i).add(place);
			placesCopy.remove(place);
		}
		for (int i = 0; i < placesCopy.size(); i++) {
			hubs.get(rnd.nextInt(HUB_COUNT)).add(placesCopy.get(i));
		}
		for (int i = 0; i < hubs.size(); i++) {
			System.out.println("HUB " + i + ": " +hubs.get(i));
		}
	}
	
	private void generateMonsters(){
		ArrayList<String> monstersCopy = new ArrayList<String>(monsters);
		for (int i = 0; i < hubs.size() - 1; i++) {
			String monster = monstersCopy.get(rnd.nextInt(monstersCopy.size()));
			worldState.put(monster, "alive");
			goal.put(monster, "dead");
			
			String monsterVulnTo = placedItems.get(rnd.nextInt(placedItems.size()));
			worldState.put(monster+"vuln", monsterVulnTo);
			
			String monsterPlace = hubs.get(i).get(rnd.nextInt(hubs.get(i).size()));
			worldState.put(monster+"place", monsterPlace);
			
			String itemPlace = hubs.get(i).get(rnd.nextInt(hubs.get(i).size()));
			worldState.put(monsterVulnTo+"place", itemPlace);
			
			String monsterBlocks = hubs.get(i+1).get(rnd.nextInt(hubs.get(i+1).size()));
			worldState.put(monster+"blocks", monsterBlocks);
			
			worldState.put(monster+"holds", new ArrayList<String>());
			beings.add(monster);
			
			placedItems.remove(monsterVulnTo);
			monstersCopy.remove(monster);
		}
		for (int i = 0; i < monstersCopy.size(); i++) {
			worldState.put(monstersCopy.get(i), "dead");
			worldState.put(monstersCopy.get(i)+"holds", new ArrayList<String>());
		}
	}
	
	private void generateState(){
		worldState = new HashMap<String, Object>();
		goal = new HashMap<String, Object>();
		goal.put("princess", "saved");
		generateHubs();
		generatePaths();
		generateItems();
		generateBeings();
	}
	
	private void generateActions(String hero){
		heroActions = new HashSet<Action>();
		actions = new HashSet<Action>();
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
			for (int j = 0; j < monsters.size(); j++) {
				if (i != j){
					if (beings.get(i).equals(hero)){
						heroActions.add(new Kill(beings.get(i), monsters.get(j), worldState));
					}
					actions.add(new Kill(beings.get(i), monsters.get(j), worldState));
				}
			}
		}
		//save
		for (int i = 0; i < beings.size(); i++) {
			for (int j = 0; j < beings.size(); j++) {
				if (i != j){
					if (beings.get(i).equals(hero)){
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
		//trade
		for (int i = 0; i < beings.size(); i++) {
			for (int j = 0; j < friends.size(); j++) {
				if (i != j){
					if (beings.get(i).equals(hero)){
						heroActions.add(new Trade(beings.get(i), friends.get(j), worldState));
					}
					actions.add(new Trade(beings.get(i), friends.get(j), worldState));
				}
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
	
	private void printHolds(String being){
		for (Map.Entry<String, Object> tEntry : worldState.entrySet()){
			if (tEntry.getKey().contains("holds") && tEntry.getKey().contains(being)){
				System.out.println(tEntry.getKey() + " holds " + tEntry.getValue());
			}
		}
	}
	
	private void printItems(){
		for (String item : items){
			System.out.println(item);
		}
	}
	
	public void play(){
		System.out.println("STARTING GAME!!!");
		/*long normalTime = 0;
		HashMap<String,Object> planningState = new HashMap<String, Object>(worldState);
		System.out.println("STARTING NORMAL PLANNING");
		long startTime = System.nanoTime();
		Stack<Action> plan = planner.plan(heroActions, planningState, goal);
		normalTime = (System.nanoTime() - startTime);*/
		
		long aStarTime = 0;
		HashMap<String,Object> aStarPlanningState = new HashMap<String, Object>(worldState);
		System.out.println("STARTING A-STAR PLANNING");
		long startTime = System.nanoTime();
		ArrayList<Action> aStarPlan = aStarPlanner.plan(heroActions, aStarPlanningState, goal);
		aStarTime = (System.nanoTime() - startTime);
		
		while(aStarPlan == null){			
			rnd = new Random();
		
			generateState();
			generateActions("prince");
			
			/*planningState = new HashMap<String, Object>(worldState);
			System.out.println("STARTING NORMAL PLANNING");
			startTime = System.nanoTime();
			plan = planner.plan(heroActions, planningState, goal);
			normalTime = (System.nanoTime() - startTime);*/
			
			aStarPlanningState = new HashMap<String, Object>(worldState);
			System.out.println("STARTING A-STAR PLANNING");
			startTime = System.nanoTime();
			aStarPlan = aStarPlanner.plan(heroActions, aStarPlanningState, goal);
			aStarTime = (System.nanoTime() - startTime);
		}
		
		
		/*System.out.println("NORMAL PLAN: " + normalTime);
		while (!plan.isEmpty()){
			System.out.print(plan.pop().print() + ", ");
		}
		System.out.println();
		
		System.out.println("------------------");*/
		
		System.out.println("A STAR PLAN: " + aStarTime);
		System.out.println(aStarPlan.size());
		for (Action a : aStarPlan){
			System.out.print(a.print() + ", ");
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
				System.out.println("Invenroty: " + worldState.get("princeholds"));
			}
			/*
			if (heroActions.get(option).equals(stack.peek())){
				System.out.println("VYPISUJEM AKCIU ZO STACKU: " + stack.pop().print());
			}*/
		}
		System.out.println("Yaaaaaay!!!");
	}
	
	
	public void testing(){
		HashMap<String, Object> a = new HashMap<String, Object>();
		HashMap<String, Object> b = new HashMap<String, Object>();
		a.put("coins", 0);
		a.put("prince", "alive");
		a.put("holds", new ArrayList<String>());
		
		b.put("coins", 0);
		b.put("prince", "alive");
		b.put("holds", new ArrayList<String>());
		ArrayList<String> haha = (ArrayList<String>) b.get("holds");
		haha.add("kok");
		System.out.println(planner.inState(a, b));
	}
	
	
}
