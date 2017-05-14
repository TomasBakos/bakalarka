package game;

import java.util.*;
import actions.*;
import goap.Action;

public class ActionGenerator {
	private HashMap<String, Object> worldState;
	private HashSet<Action> actions, heroActions;
	private ArrayList<String> beings, friends, monsters, riddlers, places, items;
	
	public ActionGenerator(ArrayList<ArrayList<String>> gameObjects, HashMap<String, Object> worldState){
		this.worldState = worldState;
		beings = gameObjects.get(0);
		friends = gameObjects.get(1);
		monsters = gameObjects.get(2);
		riddlers = gameObjects.get(3);
		places = gameObjects.get(4);
		items = gameObjects.get(5);
	}
	
	private void generateMoveActions(String hero){
		for (int i = 0; i < places.size(); i++) {
			for (int j = 0; j < beings.size(); j++) {
				if (beings.get(j).equals(hero)){
					heroActions.add(new Move(beings.get(j), places.get(i), worldState));
				}
				actions.add(new Move(beings.get(j), places.get(i), worldState));
			}
		}
	}
	
	private void generateKillActions(String hero){
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
	}
	
	private void generateSolveActions(String hero){
		for (int i = 0; i < beings.size(); i++) {
			for (int j = 0; j < riddlers.size(); j++) {
				if (i != j){
					if (beings.get(i).equals(hero)){
						heroActions.add(new Solve(beings.get(i), riddlers.get(j), worldState));
					}
					actions.add(new Solve(beings.get(i), riddlers.get(j), worldState));
				}
			}
		}
	}
	
	private void generateSaveActions(String hero){
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
	}
	
	private void generatePickUpActions(String hero){
		for (int i = 0; i < items.size(); i++) {
			for (int j = 0; j < beings.size(); j++) {
				if (beings.get(j).equals(hero)){
					heroActions.add(new PickUp(beings.get(j), items.get(i), worldState));
				}
				actions.add(new PickUp(beings.get(j), items.get(i), worldState));
			}
		}
	}
	
	private void generateTradeActions(String hero){
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
	
	public void generateActions(String hero){
		heroActions = new HashSet<Action>();
		actions = new HashSet<Action>();
		generateMoveActions(hero);
		generateKillActions(hero);
		generateSolveActions(hero);
		generateSaveActions(hero);
		generatePickUpActions(hero);
		generateTradeActions(hero);
	}
	
	public ArrayList<HashSet<Action>> getActions(){
		ArrayList<HashSet<Action>> generatedActions = new ArrayList<HashSet<Action>>();
		generatedActions.add(actions);
		generatedActions.add(heroActions);
		return generatedActions;
	}
}
