package providers;

import java.util.*;
import goap.IGoap;

public abstract class Provider implements IGoap{
	
	public HashMap<String, Object> getWorldState(){
		return null;
	}
	
	public abstract HashMap<String, Object> createGoalState();
}
