package game;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import actions.*;
import goap.*;
import providers.*;

public class GameController {
	private Agent hero;
	private ArrayList<Provider> providers;
	private HashSet<Action> actions;
	private HashMap<String, Object> worldState;
	private Planner planner;
	private Provider activeProvider;
	private Random rnd;
	private Scanner in;
	
	public GameController() throws FileNotFoundException{
		planner = new Planner();
		providers = new ArrayList<Provider>();
		actions = new HashSet<Action>();
		in = new Scanner(System.in);
		rnd = new Random();
		loadProviders();
		loadHeroActions();
		setRandomActiveProvider();
		worldState = activeProvider.getWorldState();
		hero = new Agent(actions);
	}
	
	public void loadProviders(){
		providers.add(new JailProvider());
		providers.add(new CaveProvider());
		providers.add(new GardenProvider());
	}
	
	public void loadHeroActions () throws FileNotFoundException{
		actions = new HashSet<Action>();
		loadMoveToActions(actions);
		actions.add(new KillDragon());
		actions.add(new SavePrincess());
	}
	
	private void loadMoveToActions(HashSet<Action> actions) throws FileNotFoundException{
		Scanner scan = new Scanner(new FileReader("MoveToActions.txt"));
		while (scan.hasNextLine()){
			String line = scan.nextLine();
			String[] fromTo = line.split(" ");
			actions.add(new MoveToAction(fromTo[0], fromTo[1]));
		}
		scan.close();
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
