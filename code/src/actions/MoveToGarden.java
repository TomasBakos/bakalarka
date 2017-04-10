package actions;

import goap.Action;

public class MoveToGarden extends Action {
	
	public MoveToGarden() {
		addPrecondition("place", "castle");
		addEffect("place", "garden");
	}

	@Override
	public boolean perform(Object agent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String print() {
		return "From Castle to Garden";
	}

}
