package actions;

import java.util.ArrayList;
import java.util.HashMap;

import goap.Action;

public class Solve extends Action {
	
private String solver, riddler;
	
	public Solve(String solver, String riddler){
		this.solver = solver;
		this.riddler = riddler;
	}
	
	@Override
	public boolean checkPreconditions(HashMap<String, Object> state) {
		boolean preconditions = true;
		if (!state.get(solver).equals("alive")) {
			preconditions = false;
		}
		if (preconditions && !state.get(riddler).equals("alive")){
			preconditions = false;
		}
		if (preconditions && !state.get(riddler+"place").equals(state.get(solver+"place"))){
			preconditions = false;
		}
		return preconditions;
	}

	@Override
	public HashMap<String, Object> execute(HashMap<String, Object> state) {
		HashMap<String, Object> newState = new HashMap<String, Object>(state);
		newState.put(riddler, "vanished");
		
		ArrayList<String> placeList = new ArrayList<String>();
		if (state.get("from" + state.get(riddler+"place") + "to") != null){
			placeList = new ArrayList<String>((ArrayList<String>) state.get("from" + state.get(riddler+"place") + "to"));
		}
		placeList.add((String) state.get(riddler+"blocks"));
		newState.put("from" + state.get(riddler+"place") + "to", placeList);
		
		ArrayList<String> blocksList = new ArrayList<String>();
		if (state.get("from" + state.get(riddler+"blocks") + "to") != null){
			blocksList = new ArrayList<String>((ArrayList<String>) state.get("from" + state.get(riddler+"blocks") + "to"));
		}
		blocksList.add((String) state.get(riddler+"place"));
		newState.put("from" + state.get(riddler+"blocks") + "to", blocksList);
		
		return newState;
	}
	
	public String getRiddler(){
		return riddler;
	}
	
	@Override
	public String print() {
		return solver + " solves " + riddler + "'s riddle";
	}
}
