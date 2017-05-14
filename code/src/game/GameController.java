package game;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import actions.*;
import goap.*;

public class GameController {
	private GameView view;
	private GameWorld world;
	private Planner planner, aStarPlanner;
	
	public GameController() throws FileNotFoundException{
		world = new GameWorld();
		planner = new Planner(false);
		aStarPlanner = new Planner(true);
	}
	
	private HashSet<Action> getAvailableActions(){
		HashSet<Action> availableActions = new HashSet<Action>();
		for (Action a : world.getActions()){
			a.setState(world.getWorldState());
			if (planner.inState(a.getPreconditions(), world.getWorldState())){
				availableActions.add(a);
			}
		}
		return availableActions;
	}
	
	private HashSet<Action> getAvailableHeroActions(HashMap<String, Object> state){
		HashSet<Action> availableActions = new HashSet<Action>();
		for (Action a : world.getHeroActions()){
			a.setState(state);
			if (planner.inState(a.getPreconditions(), state)){
				availableActions.add(a);
			}
		}
		return availableActions;
	}
	
	private boolean checkGoal(){
		return planner.inState(world.getGoal(), world.getWorldState());
	}
	
	
	
	public void play() throws FileNotFoundException{
		
		
		System.out.println("STARTING GAME!!!");
		/*world.createNewWorld();
		long normalTime = 0;
		HashMap<String,Object> planningState = new HashMap<String, Object>(worldState);
		System.out.println("STARTING NORMAL PLANNING");
		long startTime = System.nanoTime();
		Stack<Action> plan = planner.plan(heroActions, planningState, goal);
		normalTime = (System.nanoTime() - startTime);
		
		long aStarTime = 0;
		
		System.out.println("STARTING A-STAR PLANNING");
		long startTime = System.nanoTime();
		ArrayList<Action> aStarPlan = world.createPlan();
		aStarTime = (System.nanoTime() - startTime);
		
		while(aStarPlan == null){			
			world.createNewWorld();
			
			planningState = new HashMap<String, Object>(worldState);
			System.out.println("STARTING NORMAL PLANNING");
			startTime = System.nanoTime();
			plan = planner.plan(heroActions, planningState, goal);
			normalTime = (System.nanoTime() - startTime);
			
			System.out.println("STARTING A-STAR PLANNING");
			startTime = System.nanoTime();
			aStarPlan = world.createPlan();
			aStarTime = (System.nanoTime() - startTime);
		}
		
		System.out.println("NORMAL PLAN: " + normalTime);
		while (!plan.isEmpty()){
			System.out.print(plan.pop().print() + ", ");
		}
		System.out.println();
		
		System.out.println("------------------");
		
		System.out.println("A STAR PLAN: " + aStarTime);
		System.out.println(aStarPlan.size());
		for (Action a : aStarPlan){
			System.out.print(a.print() + ", ");
		}*/
		
		ArrayList<GameWorld> worlds = new ArrayList<GameWorld>();
		for (int i = 0; i < 1000; i++) {
			world = new GameWorld();
			world.createNewWorld();
			world.createPlan();
			world.rateWorld();
			worlds.add(world);
		}
		
		for(GameWorld gw : worlds){
			if (gw.getRating() > world.getRating()){
				System.out.println(gw.getRating());
				world =  gw;
			}
		}
		
		if (world.getRating() == Integer.MIN_VALUE){
			System.out.println("Plan was unable to generate.");
			return;
		}
		System.out.println(world.getRating());
		System.out.println("PLAN IS:");
		for (Action a : world.getPlan()){
			System.out.print(a.print() + ", ");
		}
		System.out.println();
		System.out.println("RATING: " + world.getRating());
		
		
		HashMap<String, Object> playState = world.getWorldState();
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		view = new GameView(world.getFriends(), world.getMonsters(), world.getRiddlers(), world.getGoal());
		view.printStartGame(playState);
		
		Scanner in = new Scanner(System.in);
		in.nextLine();
		
		ArrayList<Action> availableActions = new ArrayList<Action>();
		while (!checkGoal()){
			availableActions = new ArrayList<Action>(getAvailableHeroActions(playState));
			view.printTurn(playState, availableActions);
			
			System.out.println();
			int option = in.nextInt() - 1;
			if (option > -1 && option < availableActions.size()){
				playState = planner.populateState(playState, availableActions.get(option).getEffects());
			}
		}
		view.printEnding();
		in.nextLine();
		in.close();
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
