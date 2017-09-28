package battleship;
/**
 * The driver for our battleship game: sets the grid size, allows the human to place their ships and grenades,
 *  and plays the computer's turn until a winner is determined!
 * @author Michael Rogers, Nicole Parmentier
 *
 */
import java.util.Scanner;

public class BattleshipDriver {

public static final int GRID_SIZE = 8;
	
	public static void main(String[] args) {
		
		Scanner keyboard = new Scanner(System.in);
		Grid gameGrid = new Grid(GRID_SIZE);
		
		//Start game
		System.out.println("Hi, let's play battleship!\n");
		
		//Human enters ship coordinates
		while(gameGrid.getHumanShipsPlaced() < PlayerMoves.SHIPS_TO_PLACE)
		{
			System.out.print("Enter coordinates for " + GridObjects.type.SHIP + " #" + (gameGrid.getHumanShipsPlaced() + 1) + ": ");	
			PlayerMoves.humanPlaceShipOrGrenade(GridObjects.type.SHIP, gameGrid, keyboard);
		}
		
		//Human enters grenade coordinates
		while(gameGrid.getHumanGrenadesPlaced() < PlayerMoves.GRENADES_TO_PLACE)
		{
			System.out.print("Enter coordinates for " + GridObjects.type.GRENADE + " #" + (gameGrid.getHumanGrenadesPlaced() + 1) + ": ");	
			PlayerMoves.humanPlaceShipOrGrenade(GridObjects.type.GRENADE, gameGrid, keyboard);
		}
		
		//Computer places ships and grenades
		PlayerMoves.computerPlaceShipsAndGrenades(gameGrid);


		//The attack sequence continues until either the computer or human has all their ships hit. 
		boolean humanTurn = true;
		while(!(gameGrid.allHit(GridObjects.playerOrComputer.COMPUTER)||gameGrid.allHit(GridObjects.playerOrComputer.HUMAN)))
		{
			//Human attack attempt
			while(humanTurn)
			{
				boolean goodAttack = false;
				while(!goodAttack)
				{
					goodAttack = PlayerMoves.humanAttack(gameGrid, keyboard);
				}
					
				gameGrid.displayGrid();
				if(PlayerMoves.computerHitGrenade == true && PlayerMoves.humanHitGrenade == true){
					PlayerMoves.computerHitGrenade = false;
					PlayerMoves.humanHitGrenade = false;
					humanTurn = true;
				}
				else if(PlayerMoves.computerHitGrenade == true){
					humanTurn = true;
					PlayerMoves.computerHitGrenade = false;
			
				}
				else
					humanTurn = false;
			}
			
			//Computer attack attempt
			while(!humanTurn)
			{
				PlayerMoves.computerAttack(gameGrid);
				gameGrid.displayGrid();
				
				if(PlayerMoves.computerHitGrenade == true && PlayerMoves.humanHitGrenade == true){
					PlayerMoves.computerHitGrenade = false;
					PlayerMoves.humanHitGrenade = false;
					humanTurn = false;
				}
				else if(PlayerMoves.humanHitGrenade == true){
					humanTurn = false;
					PlayerMoves.humanHitGrenade = false;
				}
				else
					humanTurn = true;
			}
		}
		
		//Turns end and a winner is declared
		System.out.println("\nTHE END is upon us.");
		//Human Wins
		if(gameGrid.allHit(GridObjects.playerOrComputer.COMPUTER))
				{
					System.out.println("Congratulations! You won the Game!");
				}
		//Computer Wins
		else
			System.out.println("Sad days! You lost :(");
		System.out.println("You hit " + gameGrid.getGrenadeHitCount(GridObjects.playerOrComputer.COMPUTER) + " grenades and the computer hit " + gameGrid.getGrenadeHitCount(GridObjects.playerOrComputer.HUMAN) + ".");

		System.out.println("\nFor your viewing pleasure, this is the grid in all its glory, showing you where the ships were hidden:\n");
		
		//The grid is revealed to the player so they can see where objects were placed.
		gameGrid.displayRevealedGrid();

	}
	
}