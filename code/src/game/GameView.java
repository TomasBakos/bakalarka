package game;

import java.util.*;

import goap.Action;
import actions.*;

public class GameView {
	
	private HashMap<String, Object> goal;
	private ArrayList<String> friends, monsters, riddlers;
	
	public GameView(ArrayList<String> friends, ArrayList<String> monsters, ArrayList<String> riddlers, HashMap<String, Object> goal){
		this.friends = friends;
		this.monsters = monsters;
		this.riddlers = riddlers;
		this.goal = goal;
	}
	
	public void printStartGame(HashMap<String, Object> worldState){
		System.out.println("One night you have a bad dream of bandits capturing princess and");
		System.out.println("taking her somewhere into the world.");
		System.out.println("In the morning you realize the dream was not just a dream.");
		System.out.println("Princess is nowhere to be found and there is note on your table");
		System.out.println("Note reads:");
		System.out.println("We have your beloved princess, find us and bring us " + goal.get("coins") + " coins");
		System.out.println("or you will never see her again.");
		System.out.println();
		System.out.println("Bloody bandits!!!");
	}
	
	public void printEnding(){
		System.out.println("You were able to find the bandits and pay ransom for the princess.");
		System.out.println("GG");
		System.out.println();
	}
	
	public void printTurn(HashMap<String, Object> worldState, ArrayList<Action> actions){
		ArrayList<String> actionPlaces = new ArrayList<String>();
		ArrayList<String> actionItems = new ArrayList<String>();
		ArrayList<String> actionTraders = new ArrayList<String>();
		ArrayList<String> actionMonsters = new ArrayList<String>();
		ArrayList<String> actionRiddlers = new ArrayList<String>();
		ArrayList<String> actionSaveVictims = new ArrayList<String>();
		for (Action a : actions){
			if (a instanceof Move){
				actionPlaces.add(((Move) a).getTo());
			}
			if (a instanceof PickUp){
				actionItems.add(((PickUp) a).getObject());
			}
		}
		for (Map.Entry<String, Object> entry : worldState.entrySet()){
			if (entry.getValue().equals(worldState.get("princeplace"))){
				String entity = entry.getKey().substring(0, entry.getKey().length() - 5);
				if (monsters.contains(entity)){
					actionMonsters.add(entity);
				}
				if (riddlers.contains(entity)){
					actionRiddlers.add(entity);
				}
				if (friends.contains(entity)){
					actionTraders.add(entity);
				}
				if (entity.equals("princess")){
					actionSaveVictims.add("princess");
				}
			}
		}
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("Your location is: " + worldState.get("princeplace"));
		if (actionSaveVictims.size() > 0){
			System.out.print("You have found the princess! ");
			if ((int) worldState.get("coins") == (int) goal.get("coins")){
				System.out.println("You have enough coins to pay the bandits and save her");
			} else {
				System.out.println("Unfortunatelly you don't have enough coins to pay the bandits");
			}
		}
		
		System.out.print("From where you are standing you can see path(s) leading to: ");
		for (String place : actionPlaces) {
			System.out.print(place + ", ");
		}
		System.out.println();
		
		if (actionMonsters.size() > 0 && worldState.get(actionMonsters.get(0)).equals("alive")){
			String monster = actionMonsters.get(0);
			String blockedPlace = (String) worldState.get(actionMonsters.get(0)+"blocks");
			String vulnerability = (String) worldState.get(actionMonsters.get(0)+"vuln");
			System.out.println("Additionally you see path to " + blockedPlace + ", but it is blocked by " + monster);
			System.out.println("The monster seems to be vulnerable to " + vulnerability);
		}
		if (actionRiddlers.size() > 0 && worldState.get(actionRiddlers.get(0)).equals("alive")){
			String riddler = actionRiddlers.get(0);
			String blockedPlace = (String) worldState.get(actionRiddlers.get(0)+"blocks");
			System.out.println("Additionally you see path to " + blockedPlace + ", but it is blocked by " + riddler);
			System.out.println("It seems you need to solve a riddle before " + riddler + " will let you through");
		}
		if (actionTraders.size() > 0 && !worldState.get(actionTraders.get(0)+"wants").equals("")){
			String trader = actionTraders.get(0);
			String wants = (String) worldState.get(actionTraders.get(0)+"wants");
			System.out.println("You can also see a friendly " + trader + ", who is looking for " + wants);
			System.out.println("You will surely get a reward if you bring " + wants);
		}
		if (actionItems.size() > 0){
			System.out.print("You look around some more and you find some ");
			for (String item : actionItems){
				System.out.print(item + ", ");
			}
			System.out.println();
		}
		System.out.println("Your inventory: " + worldState.get("princeholds"));
		System.out.println("You have " + worldState.get("coins") + " coins");
		System.out.println("What do you do?");
		for (int i = 0; i < actions.size(); i++) {
			System.out.print((i+1) + ": " + actions.get(i).print() + ", ");
		}
	}
	
	
}
