package battleship;

/**
 * The class which controls and performs legal moves for the human player, and determines how the computer plays
 * @author Nicole Parmentier, Michael Rogers
 *
 */
import java.util.Scanner;

//Made abstract to avoid instantiation PlayerMoves objects since that means nothing
public abstract class PlayerMoves {
	
	public static final int SHIPS_TO_PLACE = 6, GRENADES_TO_PLACE = 4;
	public static boolean humanHitGrenade = false, computerHitGrenade = false;
	public enum outcome {HIT, MISS, HIT_GRENADE};



	/** Ensures that coordinates entered by the user when placing objects at the start of the game are valid 
	 * Uses isOccupied() and coordinatesOutOfBounds to verify location.
	 * @param xAxisChar Takes a character, uses a-h, or A-H 
	 * @param yAxis Takes an integer 0-7. 
	 * @param mainGrid Takes any object of class Grid.
	 * @return true if the location is valid, false otherwise
	 */
	private static boolean checkLocation(int xAxis, int yAxis, Grid gameGrid)
	{
		if(coordinatesOutOfBounds(xAxis, yAxis, gameGrid))
			return false;
		else if(gameGrid.isOccupied(xAxis, yAxis))
			return false;
		else
			return true;
	}

	/**
	 * Used as a helper method within the class to check if a location is within the bounds of the grid.
	 * @param xAxis the X coordinate to check
	 * @param yAxis the Y coordinate to check
	 * @param gameGrid the Grid to check
	 * @return 
	 */
	private static boolean coordinatesOutOfBounds(int xAxis, int yAxis, Grid gameGrid){
		if(xAxis == -1)
			return true;
		else if(yAxis<0||yAxis>gameGrid.getGridSize()-1)
			return true;
		return false;
	}
	
	
	/** Converts the character coordinate to an integer for convenient use in-program. Currently only compatible with characters A-H, upper or lower case
	 * 
	 * @param toChange takes an appropriate character and converts it to an integer 
	 * @return returns 0-7 for a-h or A-H, -1 otherwise
	 */
	private static int changeChar(char toChange)
	{
		switch(toChange)
		{
		case 'A':
		case 'a':
			return 0;
		case 'B':
		case 'b':
			return 1;
		case 'C':
		case 'c':
			return 2;
		case 'D':
		case 'd':
			return 3;
		case 'E':
		case 'e':
			return 4;
		case 'F':
		case 'f':
			return 5;
		case 'G':
		case 'g':
			return 6;
		case 'H':
		case 'h':
			return 7;
		default:
			return -1;
		
		}
	}
	
	
	/** Allows the human to enter coordinates to place their SHIPs and GRENADEs on the game grid
	 * 
	 * @param toPlace GRENADE or SHIP
	 * @param gameGrid any object of class Grid
	 * @param keyboard Takes a scanner object for user input
	 * @return true if successful, false otherwise. False is accompanied by an out-print with details.
	 */
	public static boolean humanPlaceShipOrGrenade(GridObjects.type toPlace, Grid gameGrid, Scanner keyboard)
	{
			String location = keyboard.nextLine();		//If there's time: make an enterCoordinates() method, returns either an int[] or an object with two int parameter, x and y
			int xAxis = changeChar(location.charAt(0));	//Takes the letter character of the coordinate and converts it to the equivalent integer (A=0, B=1, etc)
			int yAxis = location.charAt(1)-49;  //Subtract 49 to turn the char number into the equivalent integer value (Subtract 48 for Unicode conversion, and 1 for index 0 representing position 1)
			//System.out.println(xAxis + " " + yAxis);	//Test line, echos back the coordinates in number format where (0,0) is top-left corner
			
			if (checkLocation(xAxis, yAxis, gameGrid))	//Fails if grid is occupied or out of bounds
			{
				gameGrid.setObject(toPlace, xAxis, yAxis, GridObjects.playerOrComputer.HUMAN);
				return true;
			}
			
			else if (coordinatesOutOfBounds(xAxis, yAxis, gameGrid))	//Fails if coordinates are out of bounds, prevents passing on invalid coordinates (ArrayIndexOutOfBounds)
			{
				System.out.println("Sorry, coordinates outside the grid. Try again.");
				return false;
			}
			
			else if (gameGrid.isOccupied(xAxis, yAxis))	//Fails if grid is occupied
			{
				System.out.println("Sorry, coordinates alrady used. Try again.");
				return false;
			}
			
			System.out.println("Unspecified error.");
			return false;
	}
	
	/** Automated SHIP and GRENADE placement for the computer. After they are placed, the method also gives an out-Print message that the game has started. 
	 * 
	 * @param gameGrid the playing grid upon which the computer player shall place their objects
	 */
	public static void computerPlaceShipsAndGrenades(Grid gameGrid){
			computerPlace(gameGrid, GridObjects.type.SHIP, SHIPS_TO_PLACE);
			computerPlace(gameGrid, GridObjects.type.GRENADE, GRENADES_TO_PLACE);
			System.out.println("\nOK, the computer placed its ships and grenades at random. Let's play."); 
	}
	
	
	/**
	 * Used in placeShipOrGrenade for the computer to place an object. The method chooses a random location to place and sets ownership of that object to COMPUTER
	 * @param mainGrid Takes any Grid class.
	 * @param toPlace SHIP or GRENADE
	 * @param objectsToPlace Number of objects to place
	 */
	private static void computerPlace(Grid gameGrid, GridObjects.type toPlace, int objectsToPlace)
	{
		int numberOfObjectsPlaced = 0;
		while (numberOfObjectsPlaced < objectsToPlace)
		{
			int randomX = generateRandomAxisLocation(gameGrid); 
			int randomY = generateRandomAxisLocation(gameGrid);
			if (!coordinatesOutOfBounds(randomX, randomY, gameGrid) && !gameGrid.isOccupied(randomX, randomY))	//Sets an object if the coordinates are not out of bounds and not occupied
			{
				gameGrid.setObject(toPlace, randomX, randomY, GridObjects.playerOrComputer.COMPUTER);
				numberOfObjectsPlaced++;
			}
		}
	}
	
	/**
	 * Used in computerPlace method. 
	 * @param mainGrid Takes any Grid object
	 * @return Generates a random integer between 0 and (gridSize-1)
	 */
	private static int generateRandomAxisLocation(Grid mainGrid)
	{
		return  (int) Math.floor(Math.random()*mainGrid.getGridSize());
	}
	

	/**
	 * This method asks for a location from the user. This user must enter a character followed by a digit (e.g "A1") which must be within the grid size, else it will return false and exit.  
	 * Once a location has been targeted, the method out-prints a message depending on what was hit and weather it was previously targeted. Method then returns true. 
	 * @param gameGrid Takes an object of class Grid.
	 * @param keyboard Takes a scanner object.
	 * @return True if the attack hits a real location, false otherwise
	 */
	public static boolean humanAttack(Grid gameGrid, Scanner keyboard)
	{
		System.out.print("\nPosition of your rocket: ");
		String location = keyboard.nextLine();
		int xAxis = changeChar(location.charAt(0));
		int yAxis = location.charAt(1)-49;	

		if (!coordinatesOutOfBounds(xAxis, yAxis, gameGrid)){
			if (gameGrid.mainGrid[xAxis][yAxis].getHit())
			{
				System.out.println("Position already called.");
				return true;	//Lose your turn if the position was previously called
			}

			else 
			{
				if(gameGrid.mainGrid[xAxis][yAxis].getType() == GridObjects.type.SHIP)
					System.out.println("Ship hit.");

				else if(gameGrid.mainGrid[xAxis][yAxis].getType() == GridObjects.type.GRENADE)
				{
					System.out.println("Kaboom! Grenade.");
					humanHitGrenade = true;
				}

				else
					System.out.println("Nothing.");

				gameGrid.mainGrid[xAxis][yAxis].setHit(true); //No matter what you hit, its location's hit value is set to true
				return true;
			}
		}
		else
		{
			System.out.println("Coordinates out of bounds, try again.");
			return false;	//Enter new coordinates if the originals were out of bounds
		}
	}
	
	/**
	 * Computer's attack method used in-game. The computer will not attack a location that was previously hit, or its own objects. 
	 * Once a location has been targeted, the method out-prints a message on what was hit.
	 * @param gameGrid The game grid being played on
	 */
		public static void computerAttack(Grid gameGrid){
			
			int randomX = 0;
			int randomY = 0;
			
			do
			{
				randomX = generateRandomAxisLocation(gameGrid);
				randomY = generateRandomAxisLocation(gameGrid);
			} while (gameGrid.mainGrid[randomX][randomY].getHit() == true || gameGrid.mainGrid[randomX][randomY].getPlayerType() == GridObjects.playerOrComputer.COMPUTER);
			gameGrid.mainGrid[randomX][randomY].setHit(true);
			System.out.println("\nPosition of computer's rocket: " + intToChar(randomX) + (randomY+1));
			
			if(gameGrid.mainGrid[randomX][randomY].getType() == GridObjects.type.SHIP)
				System.out.println("Ship hit.");

			else if(gameGrid.mainGrid[randomX][randomY].getType() == GridObjects.type.GRENADE)
			{
				System.out.println("Kaboom! Grenade.");
				computerHitGrenade = true;
			}
			else
				System.out.println("Nothing.");
	}
	
	
	/**
	 * Helper method to convert computer's attack coordinates into (char,int) format
	 * @param toChange integer to be changed to the corresponding character
	 * @return character equivalent to the entered integer (A=1, B=2, etc)
	 */
	private static char intToChar(int toChange){
		switch(toChange)
		{
		case 0:
			return 'A';
		case 1:
			return 'B';
		case 2:
			return 'C';
		case 3:
			return 'D';
		case 4:
			return 'E';
		case 5:
			return 'F';
		case 6:
			return 'G';
		case 7:
			return 'H';
		default:
			return '%';	//An error char
		
		}
	}

}
	
