package actions;

import java.util.ArrayList;
import java.util.HashMap;

import goap.Action;

public class Solve extends Action {
	
private String solver, riddler;
	
	public Solve(String solver, String riddler, HashMap<String, Object> state){
		this.solver = solver;
		this.riddler = riddler;
		
		addPrecondition(solver, "alive");
		addPrecondition(riddler, "alive");
		addPrecondition(riddler+"place", state.get(solver+"place"));
		
		addEffect(riddler, "vanished");
		
		ArrayList<String> placeList = new ArrayList<String>();
		if (state.get("from" + state.get(riddler+"place") + "to") != null){
			placeList = new ArrayList<String>((ArrayList<String>) state.get("from" + state.get(riddler+"place") + "to"));
		}
		placeList.add((String) state.get(riddler+"blocks"));
		addEffect("from" + state.get(riddler+"place") + "to", placeList);
		
		ArrayList<String> blocksList = new ArrayList<String>();
		if (state.get("from" + state.get(riddler+"blocks") + "to") != null){
			blocksList = new ArrayList<String>((ArrayList<String>) state.get("from" + state.get(riddler+"blocks") + "to"));
		}
		blocksList.add((String) state.get(riddler+"place"));
		addEffect("from" + state.get(riddler+"blocks") + "to", blocksList);
	}

	public String getRiddler(){
		return riddler;
	}
	
	@Override
	public String print() {
		return solver + " solves " + riddler + "'s riddle";
	}
	
	@Override
	public void setState(HashMap<String, Object> state){
		removePrecondition(riddler+"place");
		addPrecondition(riddler+"place", state.get(solver+"place"));
		
		removeEffect("from" + state.get(riddler+"place") + "to");
		ArrayList<String> placeList = new ArrayList<String>();
		if (state.get("from" + state.get(riddler+"place") + "to") != null){
			placeList = new ArrayList<String>((ArrayList<String>) state.get("from" + state.get(riddler+"place") + "to"));
		}
		placeList.add((String) state.get(riddler+"blocks"));
		addEffect("from" + state.get(riddler+"place") + "to", placeList);
		
		removeEffect("from" + state.get(riddler+"blocks") + "to");
		ArrayList<String> blocksList = new ArrayList<String>();
		if (state.get("from" + state.get(riddler+"blocks") + "to") != null){
			blocksList = new ArrayList<String>((ArrayList<String>) state.get("from" + state.get(riddler+"blocks") + "to"));
		}
		blocksList.add((String) state.get(riddler+"place"));
		addEffect("from" + state.get(riddler+"blocks") + "to", blocksList);
	}
}
