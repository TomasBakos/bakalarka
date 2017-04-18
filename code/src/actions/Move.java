package actions;

import java.util.HashMap;
import goap.Action;

public class Move extends Action {
	
	private String who,to;
	private HashMap<String, Object> state;
	
	public Move(String who, String to, HashMap<String, Object> state){
		this.who = who;
		this.to = to;
		this.state = state;
		addPrecondition(who, "alive");
		addPrecondition("from" + state.get(who+"place") + "to", to);
		addEffect(who+"place", to);
	}

	@Override
	public boolean perform(Object agent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String print() {
		return "Move " + who + " To " + to;
	}
	
	@Override
	public void setState(HashMap<String, Object> state){
		removePrecondition("from" + this.state.get(who+"place") + "to");
		this.state = state;
		addPrecondition("from" + state.get(who+"place") + "to", to);
	}
}
