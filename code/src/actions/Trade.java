package actions;

import java.util.ArrayList;
import java.util.HashMap;

import goap.Action;
//TODO: mozno prerobit na akciu bez itemu
public class Trade extends Action {
	
	private String hero, trader, item;
	
	public Trade(String hero, String trader, HashMap<String, Object> state){
		this.hero = hero;
		this.trader = trader;
		item = (String) state.get(trader+"wants");
		
		addPrecondition(hero, "alive");
		addPrecondition(trader, "alive");
		addPrecondition(trader+"place", state.get(hero+"place"));
		addPrecondition(hero+"holds", state.get(trader+"wants"));
		
		addEffect(trader+"wants", "");
		ArrayList<String> itemList = new ArrayList<String>((ArrayList<String>) state.get(hero+"holds"));
		itemList.remove((String) state.get(trader+"wants"));
		addEffect(hero+"holds", itemList);
		int coins = (int) state.get("coins");
		addEffect("coins", coins+1);
	}
	
	public String getTrader(){
		return trader;
	}
	
	@Override
	public String print() {
		return hero + " trades " + item + " with " + trader;
	}

	@Override
	public void setState(HashMap<String, Object> state) {
		item = (String) state.get(trader+"wants");
		removePrecondition(trader+"place");
		addPrecondition(trader+"place", state.get(hero+"place"));
		
		removePrecondition(hero+"holds");
		addPrecondition(hero+"holds", state.get(trader+"wants"));
		
		removeEffect("coins");
		int coins = (int) state.get("coins");
		addEffect("coins", coins+1);
		
		removeEffect(hero+"holds");
		ArrayList<String> itemList = new ArrayList<String>((ArrayList<String>) state.get(hero+"holds"));
		itemList.remove((String) state.get(trader+"wants"));
		addEffect(hero+"holds", itemList);
	}
	
}
