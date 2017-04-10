package actions;

import goap.Action;

public class KillDragon extends Action {

	public KillDragon() {
		addPrecondition("place", "cave");
		addPrecondition("dragon", true);
		addEffect("dragon", false);
	}

	@Override
	public boolean perform(Object agent) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String print() {
		return "Kill Dragon";
	}

}
