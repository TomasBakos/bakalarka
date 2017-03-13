package actions;

import goap.Action;

public class MoveToCastleFromGarden extends Action {
	
	public MoveToCastleFromGarden() {
		addPrecondition("place", "garden");
		addEffect("place", "castle");
	}
	
	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean perform(Object agent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean reqInRange() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String print() {
		return "From Garden To Castle";
	}

}
