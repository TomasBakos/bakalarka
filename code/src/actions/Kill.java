package actions;

import java.util.ArrayList;
import java.util.HashMap;

import goap.Action;

public class Kill extends Action {

	private String killer, victim, item;
	
	public Kill(String killer, String victim, HashMap<String, Object> state){
		this.killer = killer;
		this.victim = victim;
		this.item = (String) state.get(victim+"vuln");
		
		addPrecondition(killer, "alive");
		addPrecondition(victim, "alive");
		addPrecondition(victim+"place", state.get(killer+"place"));
		addPrecondition(killer+"holds", state.get(victim+"vuln"));
		
		addEffect(victim, "dead");
		
		ArrayList<String> placeList = new ArrayList<String>();
		if (state.get("from" + state.get(victim+"place") + "to") != null){
			placeList = new ArrayList<String>((ArrayList<String>) state.get("from" + state.get(victim+"place") + "to"));
		}
		placeList.add((String) state.get(victim+"blocks"));
		addEffect("from" + state.get(victim+"place") + "to", placeList);
		
		ArrayList<String> blocksList = new ArrayList<String>();
		if (state.get("from" + state.get(victim+"blocks") + "to") != null){
			blocksList = new ArrayList<String>((ArrayList<String>) state.get("from" + state.get(victim+"blocks") + "to"));
		}
		blocksList.add((String) state.get(victim+"place"));
		addEffect("from" + state.get(victim+"blocks") + "to", blocksList);
	}

	@Override
	public String print() {
		return killer + " kills " + victim + " with " + item;
	}
	
	@Override
	public void setState(HashMap<String, Object> state){
		this.item = (String) state.get(victim+"vuln");
		
		removePrecondition(victim+"place");
		addPrecondition(victim+"place", state.get(killer+"place"));
		
		removePrecondition(killer+"holds");
		addPrecondition(killer+"holds", state.get(victim+"vuln"));
		
		removeEffect("from" + state.get(victim+"place") + "to");
		ArrayList<String> placeList = new ArrayList<String>();
		if (state.get("from" + state.get(victim+"place") + "to") != null){
			placeList = new ArrayList<String>((ArrayList<String>) state.get("from" + state.get(victim+"place") + "to"));
		}
		placeList.add((String) state.get(victim+"blocks"));
		addEffect("from" + state.get(victim+"place") + "to", placeList);
		
		removeEffect("from" + state.get(victim+"blocks") + "to");
		ArrayList<String> blocksList = new ArrayList<String>();
		if (state.get("from" + state.get(victim+"blocks") + "to") != null){
			blocksList = new ArrayList<String>((ArrayList<String>) state.get("from" + state.get(victim+"blocks") + "to"));
		}
		blocksList.add((String) state.get(victim+"place"));
		addEffect("from" + state.get(victim+"blocks") + "to", blocksList);
	}
}
