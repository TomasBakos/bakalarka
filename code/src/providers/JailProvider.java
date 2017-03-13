package providers;

import java.util.HashMap;

public class JailProvider extends Provider {

	@Override
	public HashMap<String, Object> getWorldState() {
		HashMap<String, Object> worldData = new HashMap<String, Object>();
		worldData.put("place", "jail");
		return worldData;
	}
	
	@Override
	public HashMap<String, Object> createGoalState() {
		HashMap<String, Object> goal = new HashMap<String, Object>();
		goal.put("place", "village");
		return goal;
	}

}
