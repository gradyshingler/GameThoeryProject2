import java.util.ArrayList;


public class ReadFuture {
private State[] Future;//Serves as our "tree" but in array form
	
	public ReadFuture(ArrayList<Move> possibleMoves, int choices, int MovesDeep, Board board)
	{
		/*FOR SIZE: This is how you calculate the size of full k-ary tree... (K^n - 1)/(K-1) ... we'll 
		 * probably use 4 here for K, but I made it so it work for any k-ary tree of varying height so we
		 *  can change things if needed.   
		 */
		int size = (int)(    (Math.pow(choices, MovesDeep) - 1)   /   (choices - 1)      );
		Future =new State[size];
		
		/******************************Discovering Future States*****************************/
		/*Steps
		 *  1) Choose Top k states at every level
		 *  2) Only leaves will hold the final scores
		 *  3)Bubble up the best choice for P1 by alternating between choosing children
		 *  that are best for either P1 or P2 all the way until P1 decides
		 *  
		 *  Explanation: leaves contain a score which shows how good that state is for 
		 *  P1. Higher, the better. Let's assume last row, the leaves, is P2's turn. P2 would 
		 *  want the smallest score value. So every parent node of a leave assumes the score
		 *  of the lowest score value. Then, we move up and it's P1's turn for this row,
		 *  and every parent of of this assumes the highest score it's children. This down back
		 *  and forth between P1 and P2 until P1 gets the final choice of choosing the initial 
		 *  moves we were given to choose from for P1's turn.  
		 *  
		 *  We're essentially choosing between
		 *  the best future assuming P2 is just as rational as us. If P2 ever messes up, and does 
		 *  something irrational like not correctly choosing a move that makes the score smaller, 
		 *  we'll only end up with a better score.
		 */
		
		/*CHOOSE INITIAL TOP 4*/
		
		//Only need 
		int numParents = (int)(Math.pow(choices, MovesDeep-1));
		
	}
	
	//Returns numeric value of the state the move creates
	private int Evaluate(Move m)
	{
		return 0;
	}

}
