package actions;

import java.util.ArrayList;
import java.util.HashMap;

import goap.Action;

public class Kill extends Action {

	private String killer, victim;
	
	public Kill(String killer, String victim){
		this.killer = killer;
		this.victim = victim;
	}
	
	@Override
	public boolean checkPreconditions(HashMap<String, Object> state) {
		boolean preconditions = true;
		if (!state.get(killer).equals("alive")) {
			preconditions = false;
		}
		if (preconditions && !state.get(victim).equals("alive")){
			preconditions = false;
		}
		if (preconditions && !state.get(victim+"place").equals(state.get(killer+"place"))){
			preconditions = false;
		}
		ArrayList<String> inventory = (ArrayList<String>)state.get(killer+"holds");
		if (preconditions && !inventory.contains(state.get(victim+"vuln"))){
			preconditions = false;
		}
		return preconditions;
	}
	
	@Override
	public HashMap<String, Object> execute(HashMap<String, Object> state) {
		HashMap<String, Object> newState = new HashMap<String, Object>(state);
		newState.put(victim, "dead");
		
		ArrayList<String> placeList = new ArrayList<String>();
		if (state.get("from" + state.get(victim+"place") + "to") != null){
			placeList = new ArrayList<String>((ArrayList<String>) state.get("from" + state.get(victim+"place") + "to"));
		}
		placeList.add((String) state.get(victim+"blocks"));
		newState.put("from" + state.get(victim+"place") + "to", placeList);
		
		ArrayList<String> blocksList = new ArrayList<String>();
		if (state.get("from" + state.get(victim+"blocks") + "to") != null){
			blocksList = new ArrayList<String>((ArrayList<String>) state.get("from" + state.get(victim+"blocks") + "to"));
		}
		blocksList.add((String) state.get(victim+"place"));
		newState.put("from" + state.get(victim+"blocks") + "to", blocksList);
		
		return newState;
	}
	
	public String getVictim(){
		return victim;
	}
	
	@Override
	public String print() {
		return killer + " kills " + victim;
	}
}
