package game;

import java.util.*;
import actions.*;
import goap.*;
import providers.*;

public class GameController {
	private Agent hero;
	private ArrayList<Provider> providers;
	private HashSet<Action> heroActions;
	private HashMap<String, Object> worldState;
	private Planner planner;
	private Provider activeProvider;
	private Random rnd;
	private Scanner in;
	
	public GameController(){
		planner = new Planner();
		providers = new ArrayList<Provider>();
		heroActions = new HashSet<Action>();
		in = new Scanner(System.in);
		rnd = new Random();
		loadProviders();
		loadHeroActions();
		setRandomActiveProvider();
		worldState = activeProvider.getWorldState();
		hero = new Agent(heroActions);
	}
	
	public void loadProviders(){
		providers.add(new JailProvider());
		providers.add(new CaveProvider());
		providers.add(new GardenProvider());
	}
	
	public void loadHeroActions (){
		heroActions = new HashSet<Action>();
		heroActions.add(new MoveToCastleFromGarden());
		heroActions.add(new MoveToCastleFromVillage());
		heroActions.add(new MoveToGarden());
		heroActions.add(new MoveToJail());
		heroActions.add(new MoveToVillageFromCastle());
		heroActions.add(new MoveToVillageFromCave());
		heroActions.add(new MoveToCave());
		heroActions.add(new MoveToCastleFromJail());
		heroActions.add(new KillDragon());
	}
	
	public void setRandomActiveProvider(){
		activeProvider = providers.get(rnd.nextInt(providers.size()));
	}

	public void setNewAgentGoal(Agent agent){
		agent.setNewGoal(activeProvider.createGoalState());
	}
	
	public Stack<Action> findNewAgentPlan(Agent agent){
		return agent.findNewPlan(heroActions, worldState);
	}
	
	public Agent getHero() {
		return hero;
	}
	
	public void printAvailableHeroActions(){
		
	}
	
	public void play(){
		setNewAgentGoal(hero);
		Stack<Action> stack = findNewAgentPlan(hero);
		ArrayList<Action> heroActions = new ArrayList<Action>();
		while (!stack.isEmpty()){
			heroActions = hero.getAvailableActions(worldState);
			for (int i = 0; i < heroActions.size(); i++) {
				System.out.print(i + ": " + heroActions.get(i).print() + ", ");
			}
			System.out.println();
			int option = in.nextInt();
			if (option > -1 && option < heroActions.size()){
				worldState = planner.populateState(worldState, heroActions.get(option).getEffects());
			}
			if (heroActions.get(option).equals(stack.peek())){
				System.out.println("VYPISUJEM AKCIU ZO STACKU: " + stack.pop().print());
			}
		}
		System.out.println("Jupiiiiii!!!");
	}
	
}
