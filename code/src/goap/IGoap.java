package goap;

import java.util.*;

/**
 * Kazdy agent, ktory chce pouzit GOAP musi implementovat
 * tento interface. Poskytuje informacie GOAP planovacu
 * aby mohol planovat, ktore akcie pouzit.
 * 
 * Tiez poskytuje interface pre planovac aby mohol davat
 * spatnu odozvu Agentovi a hlasit zlyhanie.
 */
public interface IGoap {
	/**
	 * Pociatocny stav Agenta a sveta.
	 * Zadaj ake stavy su potrebne pre akcie aby mohli bezat.
	 */
	HashMap<String,Object> getWorldState();

	/**
	 * Zadaj planovacu novy ciel aby mohol najst akcie
	 * ktorymi sa da splnit.
	 */
	HashMap<String,Object> createGoalState();

	/**
	 * Ziadna postupnost akcii sa nedala najst pre zadany ciel.
	 * Musis skusit iny ciel.
	 */
	void planFailed (HashMap<String,Object> failedGoal);

	/**
	 * Nasiel sa plan pre zadany ciel.
	 * Tieto akcie v tomto poradi Agent vykona.
	 */
	void planFound (HashMap<String,Object> goal, Queue<Action> actions);

	/**
	 * Vsetky akcie sa ukoncili a bol dosiahnuty ciel.
	 */
	void actionsFinished ();

	/**
	 * Jedna z akcii sposobila zlyhanie planu.
	 * Vracia tuto akciu.
	 */
	void planAborted (Action aborter);

	/**
	 * Vola sa pocas Update-u. Hybe Agenta smerom k cielovemu objektu
	 * aby sa mohla vykonat nasledujuca akcia.
	 * Vracia true ak je Agent v cielovom objekte a moze sa vykonat dalsia akcia.
	 * False ak tam este nie je.
	 */
	boolean moveAgent(Action nextAction);
}
