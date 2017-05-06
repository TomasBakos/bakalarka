package actions;

import java.util.ArrayList;
import java.util.HashMap;

import goap.Action;

public class PickUp extends Action {
	
	private String being, object;
	
	public PickUp(String being, String object, HashMap<String, Object> state){
		this.being = being;
		this.object = object;
		this.interestCost = 2;
		
		addPrecondition(being, "alive");
		addPrecondition(object+"place", state.get(being+"place"));
		addPrecondition(object, "placed");
		
		ArrayList<String> toList = new ArrayList<String>((ArrayList<String>)state.get(being+"holds"));
		toList.add(object);
		addEffect(object, "notplaced");
		addEffect(being+"holds", toList);
	}
	
	public String getObject(){
		return object;
	}
	
	@Override
	public String print() {
		return being + " picks up " + object;
	}
	
	@Override
	public void setState(HashMap<String, Object> state){
		removePrecondition(object+"place");
		addPrecondition(object+"place", state.get(being+"place"));
		
		removeEffect(being+"holds");
		ArrayList<String> toList = new ArrayList<String>((ArrayList<String>)state.get(being+"holds"));
		toList.add(object);
		addEffect(being+"holds", toList);
	}

}
