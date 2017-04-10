package actions;

import goap.Action;

public class MoveToCave extends Action {
	
	public MoveToCave() {
		addPrecondition("place", "village");
		addEffect("place", "cave");
	}

	@Override
	public boolean perform(Object agent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String print() {
		return "From Village To Cave";
	}

}
