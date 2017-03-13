package providers;

import java.util.*;
import goap.IGoap;

public abstract class Provider implements IGoap{
	
	public abstract HashMap<String, Object> createGoalState();
}
