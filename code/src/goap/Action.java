package goap;

import java.util.*;

public abstract class Action {
	
	private HashMap<String, Object> preconditions;
	private HashMap<String, Object> effects;
	
	public int interestCost = 1;
	
	public Action(){
		preconditions = new HashMap<String, Object>();
		effects = new HashMap<String, Object>();
	}
    
	public abstract boolean checkPreconditions(HashMap<String, Object> state);
	
	public abstract HashMap<String, Object> execute(HashMap<String, Object> state);
	
    public abstract String print();
	
	public void addPrecondition(String key, Object val){
		preconditions.put(key, val);
	}
	
	public void removePrecondition(String key){
		preconditions.remove(key);
	}
	
	public void addEffect(String key, Object val){
		effects.put(key, val);
	}
	
	public void removeEffect(String key){
		effects.remove(key);
	}

	public HashMap<String, Object> getPreconditions() {
		return preconditions;
	}

	public HashMap<String, Object> getEffects() {
		return effects;
	}
}
