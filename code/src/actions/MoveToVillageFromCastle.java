package actions;

import goap.Action;

public class MoveToVillageFromCastle extends Action {

	public MoveToVillageFromCastle() {
		addPrecondition("place", "castle");
		addEffect("place", "village");
	}

	@Override
	public boolean perform(Object agent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String print() {
		return "From Castle To Village";
	}

}
