package game;

import java.util.*;

public class WorldGenerator {
	private final int HUB_COUNT = 2;
	private ArrayList<ArrayList<String>> hubs;
	private HashMap<String, Object> worldState;
	private HashMap<String, Object> goal;
	private ArrayList<String> beings, friends, monsters, riddlers, places, items, placedItems, placedItemsCopy;
	private Random rnd;
	
	public WorldGenerator(ArrayList<ArrayList<String>> loadedObjects){
		friends = loadedObjects.get(0);
		monsters = loadedObjects.get(1);
		riddlers = loadedObjects.get(2);
		places = loadedObjects.get(3);
		items = loadedObjects.get(4);
	}
	
	
	private void generatePaths(){
		for (int l = 0; l < hubs.size(); l++) {
			for (int i = 0; i < hubs.get(l).size() - 1; i++) {
				for (int j = i + 1; j < hubs.get(l).size(); j++) {
					if (rnd.nextInt(3) == 0){
						if (!worldState.containsKey("from" + hubs.get(l).get(i) + "to")){
							worldState.put("from" + hubs.get(l).get(i) + "to", new ArrayList<String>());
						}
						if (!worldState.containsKey("from" + hubs.get(l).get(j) + "to")){
							worldState.put("from" + hubs.get(l).get(j) + "to", new ArrayList<String>());
						}
						ArrayList<String> toListFrom = (ArrayList<String>) worldState.get("from" + hubs.get(l).get(i) + "to");
						ArrayList<String> toListTo = (ArrayList<String>) worldState.get("from" + hubs.get(l).get(j) + "to");
						if(hubs.get(l).get(i).equals("bridge") && toListFrom.size() >= 2){
							break;
						}
						if(hubs.get(l).get(j).equals("bridge") && toListTo.size() >= 2){
							break;
						}
						if(toListFrom.size() >= 3){
							break;
						}
						toListFrom.add(hubs.get(l).get(j));
						toListTo.add(hubs.get(l).get(i));
					}
				}
			}
		}
	}
	
	private void generatePathsFull(){
		for (int i = 0; i < places.size(); i++) {
			for (int j = 0; j < places.size(); j++) {
				if (i != j){
					if (!worldState.containsKey("from" + places.get(i) + "to")){
						worldState.put("from" + places.get(i) + "to", new ArrayList<String>());
					}
						ArrayList<String> toList = (ArrayList<String>) worldState.get("from" + places.get(i) + "to");
						toList.add(places.get(j));
				}
			}
		}
	}
	
	private void generateFriends(){
		ArrayList<String> friendsCopy = new ArrayList<String>(friends);
		ArrayList<String> placesCopy = new ArrayList<String>(places);
		for (int i = 0; i < hubs.size(); i++) {
			String friend = friendsCopy.get(rnd.nextInt(friendsCopy.size()));
			worldState.put(friend, "alive");
			
			String friendWants = placedItems.get(rnd.nextInt(placedItems.size()));
			worldState.put(friend+"wants", friendWants);
			goal.put(friend+"wants", "");
			
			String friendPlace = placesCopy.get(rnd.nextInt(placesCopy.size()));
			worldState.put(friend+"place", friendPlace);
			placesCopy.remove(friendPlace);
			
			String itemPlace = placesCopy.get(rnd.nextInt(placesCopy.size()));
			worldState.put(friendWants+"place", itemPlace);
			placesCopy.remove(itemPlace);
			
			worldState.put(friend+"holds", new ArrayList<String>());
			
			beings.add(friend);
			
			placedItems.remove(friendWants);
			friendsCopy.remove(friend);
		}
		for (int i = 0; i < friendsCopy.size(); i++) {
			worldState.put(friendsCopy.get(i), "dead");
			worldState.put(friendsCopy.get(i)+"holds", new ArrayList<String>());
		}
	}
	
	private void generateBeings(){
		beings = new ArrayList<String>();
		beings.add("prince");
		beings.add("princess");
		worldState.put("prince", "alive");
		worldState.put("princeplace", hubs.get(0).get(rnd.nextInt(hubs.get(0).size())));
		worldState.put("princeholds", new ArrayList<String>());
		worldState.put("princess", "alive");
		worldState.put("princessplace", hubs.get(hubs.size() - 1).get(rnd.nextInt(hubs.get(hubs.size() - 1).size())));
		worldState.put("princessholds", new ArrayList<String>());
		generateFriends();
		generateBlockingEntities();
	}
	
	private void generateItems(){
		ArrayList<String> itemsCopy = new ArrayList<String>(items);
		placedItems = new ArrayList<String>();
		placedItemsCopy = new ArrayList<String>();
		for (int i = 0; i < (2 * HUB_COUNT) - 1; i++) {
			String item = itemsCopy.get(rnd.nextInt(itemsCopy.size()));
			worldState.put(item, "placed");
			placedItems.add(item);
			placedItemsCopy.add(item);
			itemsCopy.remove(item);
		}
		worldState.put("coins", 0); 
	}
	
	private void generateHubs(){
		hubs = new ArrayList<ArrayList<String>>();
		ArrayList<String> placesCopy = new ArrayList<String>(places);
		for (int i = 0; i < HUB_COUNT; i++) {
			hubs.add(new ArrayList<String>());
			String place = placesCopy.get(rnd.nextInt(placesCopy.size()));
			hubs.get(i).add(place);
			placesCopy.remove(place);
		}
		for (int i = 0; i < placesCopy.size(); i++) {
			hubs.get(rnd.nextInt(HUB_COUNT)).add(placesCopy.get(i));
		}
	}
	
	private void generateMonster(ArrayList<String> monstersCopy, int hubIndex){
		String monster = monstersCopy.get(rnd.nextInt(monstersCopy.size()));
		worldState.put(monster, "alive");
		goal.put(monster, "dead");
		
		String monsterVulnTo = placedItems.get(rnd.nextInt(placedItems.size()));
		worldState.put(monster+"vuln", monsterVulnTo);
		
		String monsterPlace = hubs.get(hubIndex).get(rnd.nextInt(hubs.get(hubIndex).size()));
		worldState.put(monster+"place", monsterPlace);
		
		String itemPlace = hubs.get(hubIndex).get(rnd.nextInt(hubs.get(hubIndex).size()));
		worldState.put(monsterVulnTo+"place", itemPlace);
		
		String monsterBlocks = hubs.get(hubIndex+1).get(rnd.nextInt(hubs.get(hubIndex+1).size()));
		worldState.put(monster+"blocks", monsterBlocks);
		
		worldState.put(monster+"holds", new ArrayList<String>());
		beings.add(monster);
		
		placedItems.remove(monsterVulnTo);
		monstersCopy.remove(monster);
	}
	
	private void generateRiddler(ArrayList<String> riddlersCopy, int hubIndex){
		String riddler = riddlersCopy.get(rnd.nextInt(riddlersCopy.size()));
		worldState.put(riddler, "alive");
		goal.put(riddler, "vanished");

		String riddlerPlace = hubs.get(hubIndex).get(rnd.nextInt(hubs.get(hubIndex).size()));
		worldState.put(riddler+"place", riddlerPlace);

		String riddlerBlocks = hubs.get(hubIndex+1).get(rnd.nextInt(hubs.get(hubIndex+1).size()));
		worldState.put(riddler+"blocks", riddlerBlocks);

		worldState.put(riddler+"holds", new ArrayList<String>());
		beings.add(riddler);
		
		String notUsedItem = placedItems.get(rnd.nextInt(placedItems.size()));
		String itemPlace = hubs.get(hubIndex).get(rnd.nextInt(hubs.get(hubIndex).size()));
		worldState.put(notUsedItem+"place", itemPlace);
		
		placedItems.remove(notUsedItem);
		riddlersCopy.remove(riddler);
	}
	
	private void generateBlockingEntities(){
		ArrayList<String> monstersCopy = new ArrayList<String>(monsters);
		ArrayList<String> riddlersCopy = new ArrayList<String>(riddlers);
		for (int i = 0; i < hubs.size() - 1; i++) {
			if (rnd.nextBoolean()){
				generateMonster(monstersCopy, i);
			} else {
				generateRiddler(riddlersCopy, i);
			}
		}
		for (int i = 0; i < monstersCopy.size(); i++) {
			worldState.put(monstersCopy.get(i), "dead");
			worldState.put(monstersCopy.get(i)+"holds", new ArrayList<String>());
		}
		for (int i = 0; i < riddlersCopy.size(); i++) {
			worldState.put(riddlersCopy.get(i), "vanished");
			worldState.put(riddlersCopy.get(i)+"holds", new ArrayList<String>());
		}
	}
	
	public long generateState(boolean randomSeed, long setSeed){
		worldState = new HashMap<String, Object>();
		goal = new HashMap<String, Object>();
		rnd = new Random();
		long seed = setSeed;
		if (randomSeed){
			seed = rnd.nextLong();
		}
		rnd.setSeed(seed);
		goal.put("princess", "saved");
		goal.put("coins", HUB_COUNT);
		generateHubs();
		generatePaths();
		generateItems();
		generateBeings();
		return seed;
	}
	
	public ArrayList<ArrayList<String>> getGameObjects(){
		ArrayList<ArrayList<String>> gameObjects = new ArrayList<ArrayList<String>>();
		gameObjects.add(beings);
		gameObjects.add(friends);
		gameObjects.add(monsters);
		gameObjects.add(riddlers);
		gameObjects.add(places);
		gameObjects.add(placedItemsCopy);
		return gameObjects;
	}
	
	public HashMap<String, Object> getWorldState(){
		return worldState;
	}
	
	public HashMap<String, Object> getGoal(){
		return goal;
	}
	
}
