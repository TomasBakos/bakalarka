package actions;

import java.util.HashMap;

import goap.Action;

public class Save extends Action {
	
	private String savior, victim;
	
	public Save(String savior, String victim, HashMap<String, Object> state){
		this.savior = savior;
		this.victim = victim;
		addPrecondition(savior, "alive");
		addPrecondition(victim, "alive");
		addPrecondition(victim+"place", state.get(savior+"place"));
		addPrecondition(savior+"holds", "sword");
		addEffect(victim, "saved");
	}
	
	@Override
	public boolean perform(Object agent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String print() {
		return savior + " saved " + victim;
	}

	@Override
	public void setState(HashMap<String, Object> state){
		removePrecondition(victim+"place");
		addPrecondition(victim+"place", state.get(savior+"place"));
	}
}
