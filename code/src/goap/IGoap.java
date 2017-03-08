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
	 * Zadava ake stavy su potrebne pre akcie aby mohli bezat.
	 */
	HashMap<String,Object> getWorldState();

	/**
	 * Dava planovacu novy ciel aby mohol najst akcie
	 * ktorymi sa da splnit.
	 */
	HashMap<String,Object> createGoalState();

}