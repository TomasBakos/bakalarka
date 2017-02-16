package goap;

import java.util.*;

public abstract class Action {
	
	private HashMap<String, Object> preconditions;
	private HashMap<String, Object> effects;
	
	private boolean inRange = false;
	
	public int interest = 1;
	
	public Object target;
	
	public Action(){
		preconditions = new HashMap<String, Object>();
		effects = new HashMap<String, Object>();
	}
	
	public void doReset(){
		target = null;
		reset();
	}
	
	/**
	 * Resetuje vsetky premenne ktore treba pred dalsim planovanim.
	 */
	public abstract void reset();
	
	/**
	 * Vrati ci je akcia ukoncena.
	 */
	public abstract boolean isDone();
	
	/**
	 * Vykona danu akciu.
	 * Vrati true ak sa akcia vykonala uspesne alebo false
	 * ak nieco zlyhalo. V tomto pripade by akcia mala byt
	 * odstranena z fronty a ciel je nedosiahnutelny.
	 */
	public abstract boolean perform(Object agent);
	
	/**
     * Potrebuje byt tato akcia v dosahu cieloveho objektu?
     * Ak nie tak moveTo stav nebude potrebny pre tuto akciu.
     */
    public abstract boolean reqInRange ();
	
	/**
	 * Sme v dosahu ciela?
	 * MoveTo stav toto nastavi a zresetuje sa vzdy ked je
	 * vykonana akcia.
	 */
	public boolean isInRange(){
		return inRange;
	}
	
	public void setInRange(boolean inRange){
		this.inRange = inRange;
	}
	
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

	public HashMap<String, Object> getPredpoklady() {
		return preconditions;
	}

	public HashMap<String, Object> getDosledky() {
		return effects;
	}
	
}
