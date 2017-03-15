package game;

import java.util.ArrayList;

import goap.Agent;
import providers.*;

public class GameController {
	private Agent hero;
	private ArrayList<Provider> providers;
	
	public GameController(){
		hero = new Agent();
		providers = new ArrayList<Provider>();
	}
	
	public void loadProviders(){
		providers.add(new JailProvider());
		providers.add(new CaveProvider());
		providers.add(new GardenProvider());
	}
	
	public void setNewAgentGoal(){
		//add random
		hero.setDataProvider(providers.get(2));
		hero.findNewGoal();
	}

	public Agent getHero() {
		return hero;
	}
	
}
