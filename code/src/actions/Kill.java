package actions;

import java.util.ArrayList;
import java.util.HashMap;

import goap.Action;

public class Kill extends Action {

	private String killer, victim, item;
	
	public Kill(String killer, String victim, String item, HashMap<String, Object> state){
		this.killer = killer;
		this.victim = victim;
		this.item = item;
		addPrecondition(killer, "alive");
		addPrecondition(victim, "alive");
		addPrecondition(victim+"place", state.get(killer+"place"));
		addPrecondition(victim+"vuln", item);
		addPrecondition(killer+"holds", item);
		ArrayList<String> toList = new ArrayList<String>();
		if (state.get("from" + state.get(victim+"place") + "to") != null){
			toList = new ArrayList<String>((ArrayList<String>) state.get("from" + state.get(victim+"place") + "to"));
		}
		toList.add((String) state.get(victim+"blocks"));
		addEffect("from" + state.get(victim+"place") + "to", toList);
		addEffect(victim, "dead");
	}
	
	@Override
	public boolean perform(Object agent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String print() {
		return killer + " kills " + victim + " with " + item;
	}
	
	@Override
	public void setState(HashMap<String, Object> state){
		removePrecondition(victim+"place");
		addPrecondition(victim+"place", state.get(killer+"place"));
	}
}
