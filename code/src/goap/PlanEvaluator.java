package goap;

import java.util.ArrayList;

import actions.*;

public class PlanEvaluator {

	public static int ratePlan(ArrayList<Action> plan){
		if (plan == null){
			return Integer.MIN_VALUE;
		}
		int planRating = 100;
		
		if (plan.get(0) instanceof Solve){
			planRating -= 5;
		}
		
		for (int i = 0; i < plan.size(); i++) {
			if (plan.get(i) instanceof Move){
				int lastIndex = i+1;
				while (plan.get(lastIndex) instanceof Move){
					lastIndex++;
				}
				//System.out.println("Odcitavam za viacnasobny Move: " + (lastIndex-i-1));
				planRating -= lastIndex-i-1;
				i = lastIndex;
			}
		}
		
		for (int i = 0; i < plan.size(); i++) {
			if (plan.get(i) instanceof PickUp){
				if (plan.get(i+1) instanceof Kill || plan.get(i+1) instanceof Trade){
					//System.out.println("Odcitavam za PickUp: " + (3));
					planRating -= 5;
				}
				if (plan.get(i+1) instanceof PickUp){
					planRating -= 3;
				}
			}
		}
		
		for (int i = 0; i < plan.size(); i++) {
			if (plan.get(i) instanceof Kill || plan.get(i) instanceof Trade || plan.get(i) instanceof Solve){
				if (plan.get(i+1) instanceof Kill || plan.get(i+1) instanceof Trade || plan.get(i+1) instanceof Solve){
					//System.out.println("Odcitavam za zdvojeny Kill/Trade/Solve: " + (5));
					planRating -= 5;
				}
				if (plan.get(i+1) instanceof PickUp){
					//System.out.println("Odcitavam za PickUp po Kill/Trade/Solve: " + (3));
					planRating -= 3;
				}
			}
		}
		
		ArrayList<Action> partialPlan = new ArrayList<Action>();
		for (int i = 0; i < plan.size(); i++) {
			if (plan.get(i) instanceof Kill || plan.get(i) instanceof Solve){
				partialPlan.add(plan.get(i));
			}
		}
		
		for (int i = 0; i < partialPlan.size() - 1; i++) {
			if (partialPlan.get(i) instanceof Kill){
				int lastIndex = i+1;
				while (lastIndex < partialPlan.size() && partialPlan.get(lastIndex) instanceof Kill){
					lastIndex++;
				}
				//System.out.println("Odcitavam za vianasobny Kill: " + ((lastIndex-i-1) * 5));
				planRating -= (lastIndex-i-1) * 5;
				i = lastIndex - 1;
			} else if (partialPlan.get(i) instanceof Solve){
				int lastIndex = i+1;
				while (lastIndex < partialPlan.size() && partialPlan.get(lastIndex) instanceof Solve){
					lastIndex++;
				}
				//System.out.println("Odcitavam za vianasobny Solve: " + ((lastIndex-i-1) * 5));
				planRating -= (lastIndex-i-1) * 5;
				i = lastIndex - 1;
			}
		}
		//System.out.println("------------------------------");
		
		return planRating;
	}
}
