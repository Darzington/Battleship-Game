package battleship;

/**
 * The Grid where the game takes place, and on which Grid Objects are stored
 * This class facilitates placing GridObjects within the grid and determines how the grid is displayed to the user. 
 * @author Nicole Parmentier, Michael Rogers
 *
 */

public class Grid {
	
	
	GridObjects[][] mainGrid;	
								
	private int gridSize;
	private int humanShipsPlaced = 0, humanGrenadesPlaced = 0;
	
	/**
	 * This method checks to see if every ship has been hit on a particular player. 
	 * @param playerType Takes HUMAN, COMPUTER, NOBODY
	 * @return true if all ships of that player are hit
	 */
	public boolean allHit(GridObjects.playerOrComputer playerType)
	{
		int countHits =0;
		for(int i=0; i<gridSize; i++)
			for(int j=0; j<gridSize; j++)
				if(mainGrid[i][j].getHit()&&mainGrid[i][j].getPlayerType()==playerType&&mainGrid[i][j].getType()==GridObjects.type.SHIP)
					countHits++;
		if(countHits==PlayerMoves.SHIPS_TO_PLACE)
			return true;
		else
			return false;
	}
	
	/**
	 * This method returns the number of a particular player's grenades which have been hit 
	 * @param playerType checks the number hit ON a players grenade.
	 * @return an integer showing grenade hit count
	 */
	public int getGrenadeHitCount(GridObjects.playerOrComputer playerType)
	{
		int countHits =0;
		for(int i=0; i<gridSize; i++)
			for(int j=0; j<gridSize; j++)
				if(mainGrid[i][j].getHit()&&mainGrid[i][j].getPlayerType()==playerType&&mainGrid[i][j].getType()==GridObjects.type.GRENADE)
					countHits++;
		return countHits;
	}
	
	
	/**
	 * 
	 * @return The number of ships the human player has placed
	 */
	public int getHumanShipsPlaced() {
		return humanShipsPlaced;
	}

	/**
	 * 
	 * @return The number of grenades the human player has placed
	 */
	public int getHumanGrenadesPlaced() {
		return humanGrenadesPlaced;
	}
	
	/**
	* Constructor for the game grid. Sets all gridObjects in the grid to enumerated type NONE, playerType to NOBODY, and boolean hit to false (makes a clean grid to start the game on)
	* @param gridSize Determines the dimensions of the grid
	*/
	public Grid(int gridSize){
		if (gridSize > 0)
			this.gridSize = gridSize;
		else
			System.exit(0);	//gridSize must be positive, fatal error otherwise
		mainGrid = new GridObjects[gridSize][gridSize];
		
		for (int i = 0; i < mainGrid.length; i++)
			for (int j = 0; j < mainGrid.length; j++)
				mainGrid[i][j] = new GridObjects();
	}
	
	
	/**Helper method used in PlayerMoves to determine if coordinates are invalid, and why (occupied versus out of bounds)
	 * 
	 * @param xAxis X-coordinate to check
	 * @param yAxis Y-coordinate to check
	 * @return true when the space is occupied, false if the object is NONE
	 */
	public boolean isOccupied(int xAxis, int yAxis)
	{
		return mainGrid[xAxis][yAxis].getType()!= GridObjects.type.NONE;
	}
	
	/**
	 * Returns the user (PLAYER or COMPUTER) who placed in the grid at specific coordinates
	 * @param xAxis This is the column character axis
	 * @param yAxis This is the row integer axis
	 * @return A getter for the enum playerOrCOmputer type for the GridObject at the specified coordinates
	 */
	public GridObjects.playerOrComputer setBy(int xAxis, int yAxis)
	{
		return mainGrid[xAxis][yAxis].getPlayerType();
	}
	
	/**
	 * Sets an object (SHIP or GRENADE) onto the grid, and sets it as belonging to the player (HUMAN or COMPUTER) that placed it. To be used before starting the game. Could theoretically be used to overwrite a previously placed object with NONE or a different object
	 * @param shipOrGrenade GRENADE, or SHIP.
	 * @param xAxis X-coordinate where the object is to be placed on the Grid
	 * @param yAxis Y-coordinate where the object is to be placed on the Grid
	 * @param playerType Either HUMAN or COMPUTER
	 */
	public void setObject(GridObjects.type shipOrGrenade, int xAxis, int yAxis, GridObjects.playerOrComputer playerType)
	{
		this.mainGrid[xAxis][yAxis].setType(shipOrGrenade);
		this.mainGrid[xAxis][yAxis].setPlayerType(playerType);
		
		if(playerType == GridObjects.playerOrComputer.HUMAN)
			if (shipOrGrenade == GridObjects.type.SHIP)
				humanShipsPlaced++;
			else if (shipOrGrenade == GridObjects.type.GRENADE)
				humanGrenadesPlaced++;
	}
	
	/**
	 * Returns the grid size, as specified in the Grid constructor
	 * @return Grid size parameter
	 */
	public int getGridSize()
	{
		return gridSize;
	}

	/**Displays the board, to be refreshed after each move
	 */
	public void displayGrid(){
		for (int i = 0; i < gridSize; i++)
		{
			System.out.print("\n\t\t");	//Moves to the next line of the grid and distances the grid from the edge of the display	
			for (int j = 0; j < gridSize; j++)
				System.out.print(displayObject(i,j) + " ");
		}
	}
	
	/**
	 * Displays a revealed grid, to be used at the end of the game.
	 */
	public void displayRevealedGrid(){
		for (int i = 0; i < gridSize; i++)
		{
			System.out.print("\n\t\t");	//Moves to the next line of the grid and distances the grid from the edge of the display	
			for (int j = 0; j < gridSize; j++)
				System.out.print(displayObjectsOnly(i,j) + " ");
		}
	}

	/** Used by displayGrid() to determine which char to display, includes none(*). If '#', '@', or '&' are displayed, something has gone terribly awry
	 * 
	 * @param xAxis x-coordinate on grid
	 * @param yAxis y-coordinate on grid
	 * @param hit boolean array to determine if a location has been revealed (hit) yet or not
	 * @return the appropriate char to display based on which game object is revealed. Human{grenade(g), ship(s), none(*)}. Computer{grenade(G), ship(S), none(*)}
	 *
	 */	
	private char displayObject(int xAxis, int yAxis) {
		
		if(mainGrid[xAxis][yAxis].getHit() == true)
		{
			//if object was placed by HUMAN
			if (mainGrid[xAxis][yAxis].getPlayerType() == GridObjects.playerOrComputer.HUMAN)
			{
				if (mainGrid[xAxis][yAxis].getType() == GridObjects.type.SHIP)
					return 's'; //HUMAN objects are indicated in lower case
				else if (mainGrid[xAxis][yAxis].getType() == GridObjects.type.GRENADE)
					return 'g';
				return '@';	//Never actually supposed to happen!
			}

			//if object was placed by COMPUTER
			else if (mainGrid[xAxis][yAxis].getPlayerType() == GridObjects.playerOrComputer.COMPUTER)
			{
				if (mainGrid[xAxis][yAxis].getType() == GridObjects.type.SHIP)
					return 'S'; //COMPUTER objects are indicated in upper case
				else if (mainGrid[xAxis][yAxis].getType() == GridObjects.type.GRENADE)
					return 'G';
				return '&';	//Never actually supposed to happen!
			}

			//No object was placed, NONE is there placed by NOBODY
			else if (mainGrid[xAxis][yAxis].getPlayerType() == GridObjects.playerOrComputer.NOBODY)
				if (mainGrid[xAxis][yAxis].getType() == GridObjects.type.NONE)
					return '*';
		}
		//The tile has not yet been revealed/hit
		else if (mainGrid[xAxis][yAxis].getHit() == false)
			return '_';

		return '#';	//Never actually supposed to happen!
	}

	
	/**Helper method for displayRevealedGrid() to determine which char to display. If '@' or '&' are displayed, something has gone terribly awry
	 * 
	 * @param xAxis x-coordinate on grid
	 * @param yAxis y-coordinate on grid
	 * @return The corresponding character for a revealed grid. Human{grenade(g), ship(s)}. Computer{grenade(G), ship(S)}
	 */
	private char displayObjectsOnly(int xAxis, int yAxis) {
		
		//if object was placed by HUMAN
		if (mainGrid[xAxis][yAxis].getPlayerType() == GridObjects.playerOrComputer.HUMAN)
		{
			if (mainGrid[xAxis][yAxis].getType() == GridObjects.type.SHIP)
				return 's'; //HUMAN objects are indicated in lower case
			else if (mainGrid[xAxis][yAxis].getType() == GridObjects.type.GRENADE)
				return 'g';
			return '@';	//Never actually supposed to happen!
		}

		//if object was placed by COMPUTER
		else if (mainGrid[xAxis][yAxis].getPlayerType() == GridObjects.playerOrComputer.COMPUTER)
		{
			if (mainGrid[xAxis][yAxis].getType() == GridObjects.type.SHIP)
				return 'S'; //COMPUTER objects are indicated in upper case
			else if (mainGrid[xAxis][yAxis].getType() == GridObjects.type.GRENADE)
				return 'G';
			return '&';	//Never actually supposed to happen!
		}

		return ' ';	//Indicates no objects was at the location
	}

}
