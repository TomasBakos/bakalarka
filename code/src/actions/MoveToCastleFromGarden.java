package actions;

import goap.Action;

public class MoveToCastleFromGarden extends Action {
	
	public MoveToCastleFromGarden() {
		addPrecondition("place", "garden");
		addEffect("place", "castle");
	}

	@Override
	public boolean perform(Object agent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String print() {
		return "From Garden To Castle";
	}

}
