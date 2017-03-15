package actions;

import goap.Action;

public class KillDragon extends Action {

	public KillDragon() {
		addPrecondition("place", "cave");
		addEffect("dragon", false);
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
		return "Kill Dragon";
	}

}
