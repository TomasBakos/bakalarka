package actions;

import java.util.ArrayList;
import java.util.HashMap;

import goap.Action;

public class PickUp extends Action {
	
	private String being, object;
	
	public PickUp(String being, String object){
		this.being = being;
		this.object = object;
		this.interestCost = 2;
	}
	
	@Override
	public boolean checkPreconditions(HashMap<String, Object> state) {
		boolean preconditions = true;
		if (!state.get(being).equals("alive")) {
			preconditions = false;
		}
		//System.out.println(state.get(object));
		if (preconditions && !state.get(object).equals("placed")){
			preconditions = false;
		}
		//System.out.println(state.get(object+"place"));
		if (preconditions && !state.get(object+"place").equals(state.get(being+"place"))){
			preconditions = false;
		}
		return preconditions;
	}

	@Override
	public HashMap<String, Object> execute(HashMap<String, Object> state) {
		HashMap<String, Object> newState = new HashMap<String, Object>(state);
		newState.put(object, "notplaced");
		
		ArrayList<String> inventory = new ArrayList<String>();
		if (state.get(being+"holds") != null){
			inventory = new ArrayList<String>((ArrayList<String>) state.get(being+"holds"));
		}
		inventory.add(object);
		newState.put(being+"holds", inventory);
		
		return newState;
	}
	
	public String getObject(){
		return object;
	}
	
	@Override
	public String print() {
		return being + " picks up " + object;
	}
}
