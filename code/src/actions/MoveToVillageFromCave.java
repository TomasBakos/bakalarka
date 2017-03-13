package actions;

import goap.Action;

public class MoveToVillageFromCave extends Action {

	@Override
	public void reset() {
		addPrecondition("place", "cave");
		addEffect("place", "village");

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
		return "From Cave To Village";
	}

}
