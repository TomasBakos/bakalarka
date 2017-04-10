package actions;

import goap.Action;

public class MoveToCastleFromVillage extends Action {
	
	public MoveToCastleFromVillage() {
		addPrecondition("place", "village");
		addEffect("place", "castle");
	}
	
	@Override
	public boolean perform(Object agent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String print() {
		return "From Village To Castle";
	}

}
