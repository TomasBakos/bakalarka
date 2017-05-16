package game;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import actions.Save;
import goap.*;

public class GameController {
	private GameView view;
	private GameWorld world;
	private Planner planner;
	
	public GameController() throws FileNotFoundException{
		world = new GameWorld();
		planner = new Planner(true);
	}
	
	private HashSet<Action> getAvailableActions(HashMap<String, Object> state){
		HashSet<Action> availableActions = new HashSet<Action>();
		for (Action a : world.getActions()){
			if (a.checkPreconditions(state)){
				availableActions.add(a);
			}
		}
		return availableActions;
	}
	
	private HashSet<Action> getAvailableHeroActions(HashMap<String, Object> state){
		HashSet<Action> availableActions = new HashSet<Action>();
		for (Action a : world.getHeroActions()){
			if (a.checkPreconditions(state)){
				if (a instanceof Save && !((Save) a).getVictim().equals("princess")){
				} else {
					availableActions.add(a);
				}
			}
		}
		return availableActions;
	}
	
	private boolean checkGoal(HashMap<String, Object> state){
		return planner.inState(world.getGoal(), state);
	}
	
	public void play() throws UnsupportedEncodingException, IOException{
		Scanner in = new Scanner(System.in);
		System.out.print("1: Generate and Play, ");
		System.out.print("2: Generate to File, ");
		System.out.println("3: Play from File");
		int playType = in.nextInt();

		if (playType == 2){
			generateToFile(in);
		} else {
			
			if (playType == 1){
				generateAndPlay(in);
			} else if (playType == 3){
				playFromFile(in);
			}

			if (world.getRating() == Integer.MIN_VALUE){
				System.out.println("World was unable to generate.");
				in.close();
				return;
			}

			System.out.println("PLAN IS:");
			for (Action a : world.getPlan()){
				System.out.print(a.print() + ", ");
			}
			System.out.println();
			System.out.println("RATING: " + world.getRating());
			System.out.println("SEED: " + world.getSeed());

			HashMap<String, Object> playState = world.getWorldState();

			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();

			view = new GameView(world.getFriends(), world.getMonsters(), world.getRiddlers(), world.getGoal());
			view.printStartGame();

			in.nextLine();

			ArrayList<Action> availableActions = new ArrayList<Action>();
			while (!checkGoal(playState)){
				availableActions = new ArrayList<Action>(getAvailableHeroActions(playState));
				view.printTurn(playState, availableActions);

				System.out.println();
				int option = in.nextInt() - 1;
				if (option > -1 && option < availableActions.size()){
					playState = availableActions.get(option).execute(playState);
				}
			}
			view.printEnding();
			in.nextLine();
		}
		in.close();
	}
	
	private void generateAndPlay(Scanner in) throws FileNotFoundException{
		System.out.print("1: Generate random world and play, ");
		System.out.println("2: Generate world from seed and play ");
		int option = in.nextInt();
		if (option == 1){
			System.out.println("GENERATING WORLDS!!!");
			ArrayList<GameWorld> worlds = new ArrayList<GameWorld>();
			for (int i = 0; i < 1000; i++) {
				world = new GameWorld();
				world.createNewWorld(true, 0);
				world.createPlan();
				world.rateWorld();
				worlds.add(world);
			}
			
			for(GameWorld gw : worlds){
				if (gw.getRating() > world.getRating()){
					world =  gw;
				}
			}
			
		} else if (option == 2){
			System.out.println("Put in seed to generate from:");
			long seed = in.nextLong();
			world = new GameWorld();
			world.createNewWorld(false, seed);
			world.createPlan();
			world.rateWorld();
		}
	}
	
	private void generateToFile(Scanner in) throws UnsupportedEncodingException, IOException{
		System.out.print("1: Generate random world(s) to file(s), ");
		System.out.println("2: Generate world from seed to file ");
		int option = in.nextInt();
		System.out.println("Generate pretty print file? (true / false)");
		boolean pretty = in.nextBoolean();
		if (option == 1){
			System.out.println("Put in number of worlds to generate: ");
			int count = in.nextInt();
			
			System.out.println("GENERATING WORLDS!!!");
			ArrayList<GameWorld> worlds = new ArrayList<GameWorld>();
			for (int i = 0; i < count; i++) {
				world = new GameWorld();
				world.createNewWorld(true, 0);
				world.createPlan();
				world.rateWorld();
				worlds.add(world);
			}
			Gson gson = new Gson();
			Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
			for (int i = 0; i < worlds.size(); i++) {
				Writer writer = new BufferedWriter(new OutputStreamWriter( new FileOutputStream("worlds/world" + i + ".json")));
				String json = gson.toJson(worlds.get(i).getWorldState(), type);
				writer.write(json);
				writer.close();
				if (pretty){
					Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
					Writer writerPretty = new BufferedWriter(new OutputStreamWriter( new FileOutputStream("worlds/world" + i + "_pretty.json")));
					String jsonPretty = gsonPretty.toJson(worlds.get(i).getWorldState(), type);
					writerPretty.write(jsonPretty);
					writerPretty.close();
				}
			}
			
		} else if (option == 2){
			System.out.println("Put in seed to generate from: ");
			long seed = in.nextLong();
			world = new GameWorld();
			world.createNewWorld(false, seed);
			world.createPlan();
			world.rateWorld();
			Gson gson = new Gson();
			Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
			Writer writer = new BufferedWriter(new OutputStreamWriter( new FileOutputStream("worlds/custom_world.json")));
			String json = gson.toJson(world.getWorldState(), type);
			writer.write(json);
			writer.close();
			if (pretty){
				Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
				Writer writerPretty = new BufferedWriter(new OutputStreamWriter( new FileOutputStream("worlds/custom_world_pretty.json")));
				String jsonPretty = gsonPretty.toJson(world.getWorldState(), type);
				writerPretty.write(jsonPretty);
				writerPretty.close();
			}
		}
	}
	
	private void playFromFile(Scanner in) throws FileNotFoundException{
		System.out.println("Put in relative address to file: ");
		String file = in.next();
		world = new GameWorld();
		world.createWorldFromFile(file);
		world.createPlan();
		world.rateWorld();
		
		if (world.getPlan() == null){
			System.out.println("This world doesnt have suitable story to play");
		}
		
	}
	
	
	public void testing(){
		
	}
	
	
}
