package actions;

import goap.Action;

public class MoveToJail extends Action {
	
	public MoveToJail() {
		addPrecondition("place", "castle");
		addEffect("place", "jail");
	}

	@Override
	public boolean perform(Object agent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String print() {
		return "From Castle To Jail";
	}

}
