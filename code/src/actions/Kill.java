package actions;

import java.util.HashMap;

import goap.Action;

public class Kill extends Action {

	private String killer, victim;
	
	public Kill(String killer, String victim, HashMap<String, Object> state){
		this.killer = killer;
		this.victim = victim;
		addPrecondition(killer, "alive");
		addPrecondition(victim+"place", state.get(killer+"place"));
		addPrecondition(victim, "alive");
		addEffect(victim, "dead");
	}
	
	@Override
	public boolean perform(Object agent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String print() {
		return killer + " kills " + victim;
	}
	
	@Override
	public void setState(HashMap<String, Object> state){
		removePrecondition(victim+"place");
		addPrecondition(victim+"place", state.get(killer+"place"));
	}
}
