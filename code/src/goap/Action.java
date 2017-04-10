package goap;

import java.util.*;

public abstract class Action {
	
	private HashMap<String, Object> preconditions;
	private HashMap<String, Object> effects;
	
	public float interest = 1;
	
	public Action(){
		preconditions = new HashMap<String, Object>();
		effects = new HashMap<String, Object>();
	}
	
	/**
	 * Vykona danu akciu.
	 * Vrati true ak sa akcia vykonala uspesne alebo false
	 * ak nieco zlyhalo. V tomto pripade by akcia mala byt
	 * odstranena z fronty a ciel je nedosiahnutelny.
	 */
	public abstract boolean perform(Object agent);
    
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
