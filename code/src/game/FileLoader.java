package game;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class FileLoader {
	private ArrayList<String> friends, monsters, riddlers, places, items;
	private ArrayList<ArrayList<String>> loadedObjects;
	
	public FileLoader(){
		loadedObjects = new ArrayList<ArrayList<String>>();
	}
	
	public ArrayList<ArrayList<String>> loadEntities() throws FileNotFoundException{
		loadFriends();
		loadMonsters();
		loadRiddlers();
		loadPlaces();
		loadItems();
		return loadedObjects;
	}
	
	private void loadFriends() throws FileNotFoundException{
		friends = new ArrayList<String>();
		Scanner scan = new Scanner(new FileReader("friends.txt"));
		while (scan.hasNextLine()){
			String line = scan.nextLine();
			friends.add(line);
		}
		loadedObjects.add(friends);
		scan.close();
	}
	
	private void loadMonsters() throws FileNotFoundException{
		monsters = new ArrayList<String>();
		Scanner scan = new Scanner(new FileReader("monsters.txt"));
		while (scan.hasNextLine()){
			String line = scan.nextLine();
			monsters.add(line);
		}
		loadedObjects.add(monsters);
		scan.close();
	}
	
	private void loadRiddlers() throws FileNotFoundException{
		riddlers = new ArrayList<String>();
		Scanner scan = new Scanner(new FileReader("riddlers.txt"));
		while (scan.hasNextLine()){
			String line = scan.nextLine();
			riddlers.add(line);
		}
		loadedObjects.add(riddlers);
		scan.close();
	}
	
	private void loadPlaces() throws FileNotFoundException{
		places = new ArrayList<String>();
		Scanner scan = new Scanner(new FileReader("places.txt"));
		while (scan.hasNextLine()){
			String line = scan.nextLine();
			places.add(line);
		}
		loadedObjects.add(places);
		scan.close();
	}
	
	private void loadItems() throws FileNotFoundException{
		items = new ArrayList<String>();
		Scanner scan = new Scanner(new FileReader("items.txt"));
		while (scan.hasNextLine()){
			String line = scan.nextLine();
			items.add(line);
		}
		loadedObjects.add(items);
		scan.close();
	}
	
	public HashMap<String, Object> loadState(String file) throws FileNotFoundException{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Scanner fileIn = new Scanner(new FileReader(file));
		String json = fileIn.next();
		
		Type collectionType = new TypeToken<HashMap<String, Object>>(){}.getType();
		HashMap<String, Object> state = gson.fromJson(json, collectionType);
		fileIn.close();
		
		return state;
	}
	
	public ArrayList<ArrayList<String>> getLoadedObjects(){
		return loadedObjects;
	}
}
