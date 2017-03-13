package actions;

import goap.Action;

public class MoveToGarden extends Action {
	
	public MoveToGarden() {
		addPrecondition("place", "castle");
		addEffect("place", "garden");
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
		return "From Castle to Garden";
	}

}
