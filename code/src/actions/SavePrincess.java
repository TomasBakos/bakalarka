package actions;

import goap.Action;

public class SavePrincess extends Action {

	public SavePrincess(){
		addPrecondition("dragon", false);
		addEffect("princess", "saved");
	}
	
	@Override
	public boolean perform(Object agent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String print() {
		return "Save Princess";
	}

}
