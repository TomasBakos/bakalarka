package goap;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import actions.*;


public class PlannerTest {
	
	@Test
	public void ratePlanMoveOnlyTest(){
		Planner planner = new Planner(true);
		ArrayList<Action> plan = new ArrayList<Action>();
		plan.add(new Move("a", "b", new HashMap<String, Object>()));
		plan.add(new Move("a", "b", new HashMap<String, Object>()));
		plan.add(new Move("a", "b", new HashMap<String, Object>()));
		plan.add(new Save("a", "b", new HashMap<String, Object>()));
		assertEquals(98, planner.ratePlan(plan));
	}
	
	@Test
	public void ratePlanMoveOnlyTest2(){
		Planner planner = new Planner(true);
		ArrayList<Action> plan = new ArrayList<Action>();
		plan.add(new Move("a", "b", new HashMap<String, Object>()));
		plan.add(new Move("a", "b", new HashMap<String, Object>()));
		plan.add(new Kill("a", "b", new HashMap<String, Object>()));
		plan.add(new Move("a", "b", new HashMap<String, Object>()));
		plan.add(new Move("a", "b", new HashMap<String, Object>()));
		plan.add(new Move("a", "b", new HashMap<String, Object>()));
		plan.add(new Save("a", "b", new HashMap<String, Object>()));
		assertEquals(97, planner.ratePlan(plan));
	}
	
	@Test
	public void ratePlanKillTest(){
		Planner planner = new Planner(true);
		ArrayList<Action> plan = new ArrayList<Action>();
		
		plan.add(new Kill("a", "b", new HashMap<String, Object>()));
		plan.add(new Move("a", "b", new HashMap<String, Object>()));
		plan.add(new Kill("a", "b", new HashMap<String, Object>()));
		//plan.add(new Kill("a", "b", new HashMap<String, Object>()));
		plan.add(new Save("a", "b", new HashMap<String, Object>()));
		
		assertEquals(95, planner.ratePlan(plan));
	}

	@Test
	public void ratePlanSolveTest(){
		Planner planner = new Planner(true);
		ArrayList<Action> plan = new ArrayList<Action>();
		
		plan.add(new Solve("a", "b", new HashMap<String, Object>()));
		plan.add(new Move("a", "b", new HashMap<String, Object>()));
		plan.add(new Solve("a", "b", new HashMap<String, Object>()));
		//plan.add(new Kill("a", "b", new HashMap<String, Object>()));
		plan.add(new Save("a", "b", new HashMap<String, Object>()));
		
		assertEquals(95, planner.ratePlan(plan));
	}
	
	@Test
	public void ratePlanPickUpTest(){
		Planner planner = new Planner(true);
		ArrayList<Action> plan = new ArrayList<Action>();
		HashMap<String, Object> state = new HashMap<String, Object>();
		
		state.put("aholds", new ArrayList<String>());
		state.put("coins", 0);
		
		plan.add(new PickUp("a", "b", state));
		plan.add(new PickUp("a", "b", state));
		plan.add(new Save("a", "b", new HashMap<String, Object>()));
		
		assertEquals(97, planner.ratePlan(plan));
	}
	
	@Test
	public void ratePlanSolveTradeTest(){
		Planner planner = new Planner(true);
		ArrayList<Action> plan = new ArrayList<Action>();
		HashMap<String, Object> state = new HashMap<String, Object>();
		
		state.put("aholds", new ArrayList<String>());
		state.put("coins", 0);
		
		
		plan.add(new Kill("a", "b", state));
		plan.add(new Move("a", "b", state));
		plan.add(new Solve("a", "b", state));
		plan.add(new Trade("a", "b", state));
		//plan.add(new Kill("a", "b", new HashMap<String, Object>()));
		plan.add(new Save("a", "b", state));
		
		assertEquals(95, planner.ratePlan(plan));
	}
	
	@Test
	public void ratePlanComplex(){
		Planner planner = new Planner(true);
		ArrayList<Action> plan = new ArrayList<Action>();
		HashMap<String, Object> state = new HashMap<String, Object>();
		
		state.put("aholds", new ArrayList<String>());
		state.put("coins", 0);
		
		plan.add(new Move("a", "b", state));
		plan.add(new Move("a", "b", state));
		plan.add(new Move("a", "b", state));
		plan.add(new PickUp("a", "b", state));
		plan.add(new Move("a", "b", state));
		plan.add(new Move("a", "b", state));
		plan.add(new Kill("a", "b", state));
		plan.add(new Move("a", "b", state));
		plan.add(new Move("a", "b", state));
		plan.add(new Move("a", "b", state));
		plan.add(new Solve("a", "b", state));
		plan.add(new Move("a", "b", state));
		plan.add(new PickUp("a", "b", state));
		plan.add(new PickUp("a", "b", state));
		plan.add(new Trade("a", "b", state));
		plan.add(new Move("a", "b", state));
		plan.add(new Move("a", "b", state));
		plan.add(new Move("a", "b", state));
		plan.add(new Move("a", "b", state));
		plan.add(new Solve("a", "b", state));
		plan.add(new Trade("a", "b", state));
		plan.add(new Trade("a", "b", state));
		plan.add(new Move("a", "b", state));
		plan.add(new Move("a", "b", state));
		plan.add(new Save("a", "b", state));
		
		assertEquals(83, planner.ratePlan(plan));
	}
	
}
