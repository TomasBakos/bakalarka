package actions;

import java.util.HashMap;
import java.util.Map;

import goap.Action;

public class Save extends Action {
	
	private String savior, victim;
	
	public Save(String savior, String victim){
		this.savior = savior;
		this.victim = victim;
	}
	
	@Override
	public boolean checkPreconditions(HashMap<String, Object> state) {
		boolean preconditions = true;
		if (!state.get(savior).equals("alive")) {
			preconditions = false;
		}
		if (preconditions && !state.get(victim).equals("alive")){
			preconditions = false;
		}
		if (preconditions && !state.get(victim+"place").equals(state.get(savior+"place"))){
			preconditions = false;
		}
		int coins = (int) state.get("coins");
		if (preconditions && coins < 2){
			preconditions = false;
		}
		return preconditions;
	}

	private int countTraders(HashMap<String, Object> state){
		int count = 0;
		for (Map.Entry<String, Object> entry : state.entrySet()){
			if (entry.getValue() == "alive"){
				count++;
			}
		}
		return count / 2;
	}
	
	@Override
	public HashMap<String, Object> execute(HashMap<String, Object> state) {
		HashMap<String, Object> newState = new HashMap<String, Object>(state);
		newState.put(victim, "saved");
		return newState;
	}
	
	public String getVictim(){
		return victim;
	}
	
	@Override
	public String print() {
		return savior + " saved " + victim;
	}

}
