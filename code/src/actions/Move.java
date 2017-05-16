package actions;

import java.util.ArrayList;
import java.util.HashMap;
import goap.Action;

public class Move extends Action {
	
	private String who,to;
	
	public Move(String who, String to){
		this.who = who;
		this.to = to;
		this.interestCost = 3;
	}
	
	@Override
	public boolean checkPreconditions(HashMap<String, Object> state) {
		boolean preconditions = true;
		if (!state.get(who).equals("alive")) {
			preconditions = false;
		}
		ArrayList<String> placesTo = new ArrayList<String>();
		if (state.get("from" + state.get(who+"place") + "to") != null){
			placesTo = new ArrayList<>((ArrayList<String>) state.get("from" + state.get(who+"place") + "to"));
		}
		if (preconditions && !placesTo.contains(to)){
			preconditions = false;
		}
		return preconditions;
	}

	@Override
	public HashMap<String, Object> execute(HashMap<String, Object> state) {
		HashMap<String, Object> newState = new HashMap<String, Object>(state);
		newState.put(who+"place", to);
		return newState;
	}
	
	public String getTo(){
		return to;
	}
	
	@Override
	public String print() {
		return "Move " + who + " To " + to;
	}
	
}
