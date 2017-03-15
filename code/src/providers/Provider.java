package providers;

import java.util.*;

public abstract class Provider{
	
	public abstract HashMap<String, Object> getWorldState();
	public abstract HashMap<String, Object> createGoalState();
}
