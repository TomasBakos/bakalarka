package actions;

import goap.Action;

public class MoveToCastleFromJail extends Action {
	
	public MoveToCastleFromJail() {
		addPrecondition("place", "jail");
		addEffect("place", "castle");
	}

	@Override
	public boolean perform(Object agent) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String print() {
		return "From Jail To Castle";
	}

}
