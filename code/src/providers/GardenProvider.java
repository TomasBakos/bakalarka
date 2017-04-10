package providers;

import java.util.HashMap;

public class GardenProvider extends Provider {

	@Override
	public HashMap<String, Object> getWorldState() {
		HashMap<String, Object> worldData = new HashMap<String, Object>();
		worldData.put("place", "garden");
		worldData.put("dragon", true);
		return worldData;
	}

	@Override
	public HashMap<String, Object> createGoalState() {
		HashMap<String, Object> goal = new HashMap<String, Object>();
		goal.put("princess", "saved");
		return goal;
	}

}
