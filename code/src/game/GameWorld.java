package game;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import goap.*;

public class GameWorld {
	private FileLoader fileLoader;
	private WorldGenerator worldGen;
	private ActionGenerator actionGen;
	private ArrayList<String> beings, friends, monsters, riddlers, places, items;
	private ArrayList<ArrayList<String>> gameObjects, loadedObjects;
	private HashSet<Action> actions;
	private HashSet<Action> heroActions;
	private HashMap<String, Object> worldState;
	private HashMap<String, Object> goal;
	private Planner planner;
	private ArrayList<Action> plan;
	private int rating;
	
	private void storeGameObjects(){
		beings = gameObjects.get(0);
		friends = gameObjects.get(1);
		monsters = gameObjects.get(2);
		riddlers = gameObjects.get(3);
		places = gameObjects.get(4);
		items = gameObjects.get(5);
	}
	
	public void printState(HashMap<String, Object> state){
		for (Map.Entry<String, Object> tEntry : state.entrySet()){
			System.out.println(tEntry.getKey() + " ukazuje na " + tEntry.getValue());
		}
	}
	
	public void printWorldStatePaths(){
		for (Map.Entry<String, Object> tEntry : worldState.entrySet()){
			if (tEntry.getKey().contains("from")){
				System.out.println(tEntry.getKey() + " ukazuje na " + tEntry.getValue());
			}
		}
	}
	
	public void printHolds(String being){
		for (Map.Entry<String, Object> tEntry : worldState.entrySet()){
			if (tEntry.getKey().contains("holds") && tEntry.getKey().contains(being)){
				System.out.println(tEntry.getKey() + " holds " + tEntry.getValue());
			}
		}
	}
	
	public void printItems(){
		for (String item : items){
			System.out.println(item);
		}
	}
	
	public void createNewWorld() throws FileNotFoundException{
		fileLoader = new FileLoader();
		loadedObjects = fileLoader.loadEntities();
		
		worldGen = new WorldGenerator(loadedObjects);
		worldGen.generateState();
		worldState = worldGen.getWorldState();
		goal = worldGen.getGoal();
		gameObjects = worldGen.getGameObjects();
		storeGameObjects();
		
		actionGen = new ActionGenerator(gameObjects, worldState);
		actionGen.generateActions("prince");
		ArrayList<HashSet<Action>> genActions = actionGen.getActions();
		actions = genActions.get(0);
		heroActions = genActions.get(1);
		
	}

	public ArrayList<Action> createPlan(){
		planner = new Planner(true);
		HashMap<String,Object> planningState = new HashMap<String, Object>(worldState);
		plan = planner.plan(heroActions, planningState, goal);
		return plan;
	}
	
	public int rateWorld(){
		rating = planner.ratePlan(plan);
		return rating;
	}
	
	public ArrayList<String> getBeings() {
		return beings;
	}

	public ArrayList<String> getFriends() {
		return friends;
	}

	public ArrayList<String> getMonsters() {
		return monsters;
	}

	public ArrayList<String> getRiddlers() {
		return riddlers;
	}

	public ArrayList<String> getPlaces() {
		return places;
	}

	public ArrayList<String> getItems() {
		return items;
	}

	public HashSet<Action> getActions() {
		return actions;
	}

	public HashSet<Action> getHeroActions() {
		return heroActions;
	}

	public HashMap<String, Object> getWorldState() {
		return worldState;
	}

	public HashMap<String, Object> getGoal() {
		return goal;
	}

	public ArrayList<Action> getPlan() {
		return plan;
	}

	public int getRating() {
		return rating;
	}
}
