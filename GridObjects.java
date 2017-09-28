package battleship;

/**
 * This class is used to store information in the grid, it doesn't actually DO much, per se.
 * @author Michael Rogers, Nicole Parmentier
 *
 */

/**
 * 
 * @author Mike/Nicole  Objects of this class are stored in array locations of the class Grid. Objects supply information on type(e.g grenade), player(e.g human) and if a location has been attacked. 
 *
 */

public class GridObjects {

	public enum type {GRENADE, SHIP, NONE};				
	public enum playerOrComputer {HUMAN, COMPUTER, NOBODY};
		
	
	private boolean hit;
	private type typeAtLocation;
	private playerOrComputer playerType;

	
	
	
	
	/**
	 * Constructor for GridObjects, used to initialise a blank playing grid in the Grid() constructor
	 */
	public GridObjects(){
		hit = false;
		typeAtLocation = type.NONE;
		playerType = playerOrComputer.NOBODY;
	}
	
	/**
	 * 
	 * @return true if the location has been attacked
	 */
	public boolean getHit() {
		return hit;
	}
	
	/**
	 * Sets the value of hit. True if it has been attacked, false otherwise
	 * @param hit Sets the value true if the location is hit, false otherwise.
	 */
	public void setHit(boolean hit) {
		this.hit = hit;
	}
	
	/**
	 * 
	 * @return Will return the type(e.g ship)
	 */
	public type getType() {
		return typeAtLocation;
	}
	
	/**
	 * 
	 * @param typeToSet Will set the type(e.g ship)
	 */
	public void setType(type typeToSet) {
		this.typeAtLocation = typeToSet;
	}

	/**
	 * 
	 * @return Will return playerOrComputer tag
	 */
	public playerOrComputer getPlayerType() {
		return playerType;
	}
	
	/**
	 * 
	 * @param playerType will set the playerOrComputer tag
	 */
	public void setPlayerType(playerOrComputer playerType) {
		this.playerType = playerType;
	} 
	
	
	
}