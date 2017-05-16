package actions;

import java.util.ArrayList;
import java.util.HashMap;

import goap.Action;

public class Trade extends Action {
	
	private String hero, trader;
	
	public Trade(String hero, String trader){
		this.hero = hero;
		this.trader = trader;
	}
	
	@Override
	public boolean checkPreconditions(HashMap<String, Object> state) {
		boolean preconditions = true;
		if (!state.get(hero).equals("alive")) {
			preconditions = false;
		}
		if (preconditions && !state.get(trader).equals("alive")){
			preconditions = false;
		}
		if (preconditions && !state.get(trader+"place").equals(state.get(hero+"place"))){
			preconditions = false;
		}
		ArrayList<String> inventory = (ArrayList<String>)state.get(hero+"holds");
		if (preconditions && !inventory.contains(state.get(trader+"wants"))){
			preconditions = false;
		}
		return preconditions;
	}

	@Override
	public HashMap<String, Object> execute(HashMap<String, Object> state) {
		HashMap<String, Object> newState = new HashMap<String, Object>(state);
		newState.put(trader+"wants", "");
		
		int coins = (int) state.get("coins");
		newState.put("coins", coins+1);
		
		ArrayList<String> inventory = new ArrayList<String>();
		if (state.get(hero+"holds") != null){
			inventory = new ArrayList<String>((ArrayList<String>) state.get(hero+"holds"));
		}
		inventory.remove((String) state.get(trader+"wants"));
		newState.put(hero+"holds", inventory);
		
		return newState;
	}
	
	public String getTrader(){
		return trader;
	}
	
	@Override
	public String print() {
		return hero + " trades with " + trader;
	}
}
