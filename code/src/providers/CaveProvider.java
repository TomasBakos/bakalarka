package providers;

import java.util.HashMap;

public class CaveProvider extends Provider {

	@Override
	public HashMap<String, Object> getWorldState() {
		HashMap<String, Object> worldData = new HashMap<String, Object>();
		worldData.put("place", "cave");
		return worldData;
	}

	@Override
	public HashMap<String, Object> createGoalState() {
		HashMap<String, Object> goal = new HashMap<String, Object>();
		goal.put("place", "castle");
		return goal;
	}

}
