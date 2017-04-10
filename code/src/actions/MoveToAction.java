package actions;

import goap.Action;

public class MoveToAction extends Action {
	
	private String from,to;
	
	public MoveToAction(String from, String to){
		this.from = from;
		this.to = to;
		addPrecondition("place", from);
		addEffect("place", to);
	}

	@Override
	public boolean perform(Object agent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String print() {
		return "From " + from + " To " + to;
	}

}
