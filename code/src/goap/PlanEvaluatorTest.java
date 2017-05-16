package goap;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import actions.*;


public class PlanEvaluatorTest {
	
	@Test
	public void ratePlanMoveOnlyTest(){
		ArrayList<Action> plan = new ArrayList<Action>();
		plan.add(new Move("a", "b"));
		plan.add(new Move("a", "b"));
		plan.add(new Move("a", "b"));
		plan.add(new Save("a", "b"));

		assertEquals(98, PlanEvaluator.ratePlan(plan));
	}
	
	@Test
	public void ratePlanMoveOnlyTest2(){
		ArrayList<Action> plan = new ArrayList<Action>();
		plan.add(new Move("a", "b"));
		plan.add(new Move("a", "b"));
		plan.add(new Kill("a", "b"));
		plan.add(new Move("a", "b"));
		plan.add(new Move("a", "b"));
		plan.add(new Move("a", "b"));
		plan.add(new Save("a", "b"));

		assertEquals(97, PlanEvaluator.ratePlan(plan));
	}
	
	@Test
	public void ratePlanKillTest(){
		ArrayList<Action> plan = new ArrayList<Action>();
		
		plan.add(new Kill("a", "b"));
		plan.add(new Move("a", "b"));
		plan.add(new Kill("a", "b"));
		//plan.add(new Kill("a", "b", new HashMap<String, Object>()));
		plan.add(new Save("a", "b"));

		assertEquals(95, PlanEvaluator.ratePlan(plan));
	}

	@Test
	public void ratePlanSolveTest(){
		ArrayList<Action> plan = new ArrayList<Action>();
		
		plan.add(new Solve("a", "b"));
		plan.add(new Move("a", "b"));
		plan.add(new Solve("a", "b"));
		//plan.add(new Kill("a", "b", new HashMap<String, Object>()));
		plan.add(new Save("a", "b"));

		assertEquals(90, PlanEvaluator.ratePlan(plan));
	}
	
	@Test
	public void ratePlanPickUpTest(){
		ArrayList<Action> plan = new ArrayList<Action>();
		HashMap<String, Object> state = new HashMap<String, Object>();
		
		state.put("aholds", new ArrayList<String>());
		state.put("coins", 0);
		
		plan.add(new PickUp("a", "b"));
		plan.add(new PickUp("a", "b"));
		plan.add(new Save("a", "b"));

		assertEquals(97, PlanEvaluator.ratePlan(plan));
	}
	
	@Test
	public void ratePlanSolveTradeTest(){

		ArrayList<Action> plan = new ArrayList<Action>();
		HashMap<String, Object> state = new HashMap<String, Object>();
		
		state.put("aholds", new ArrayList<String>());
		state.put("coins", 0);
		
		
		plan.add(new Kill("a", "b"));
		plan.add(new Move("a", "b"));
		plan.add(new Solve("a", "b"));
		plan.add(new Trade("a", "b"));
		//plan.add(new Kill("a", "b", new HashMap<String, Object>()));
		plan.add(new Save("a", "b"));

		assertEquals(95, PlanEvaluator.ratePlan(plan));
	}
	
	@Test
	public void ratePlanComplex(){
		ArrayList<Action> plan = new ArrayList<Action>();
		HashMap<String, Object> state = new HashMap<String, Object>();
		
		state.put("aholds", new ArrayList<String>());
		state.put("coins", 0);
		
		plan.add(new Move("a", "b"));
		plan.add(new Move("a", "b"));
		plan.add(new Move("a", "b"));
		plan.add(new PickUp("a", "b"));
		plan.add(new Move("a", "b"));
		plan.add(new Move("a", "b"));
		plan.add(new Kill("a", "b"));
		plan.add(new Move("a", "b"));
		plan.add(new Move("a", "b"));
		plan.add(new Move("a", "b"));
		plan.add(new Solve("a", "b"));
		plan.add(new Move("a", "b"));
		plan.add(new PickUp("a", "b"));
		plan.add(new PickUp("a", "b"));
		plan.add(new Trade("a", "b"));
		plan.add(new Move("a", "b"));
		plan.add(new Move("a", "b"));
		plan.add(new Move("a", "b"));
		plan.add(new Move("a", "b"));
		plan.add(new Solve("a", "b"));
		plan.add(new Trade("a", "b"));
		plan.add(new Trade("a", "b"));
		plan.add(new Move("a", "b"));
		plan.add(new Move("a", "b"));
		plan.add(new Save("a", "b"));

		assertEquals(68, PlanEvaluator.ratePlan(plan));
	}
	
}
