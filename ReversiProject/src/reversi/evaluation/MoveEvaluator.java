package reversi.evaluation;
import java.awt.List;
import java.util.ArrayList;
import java.util.Collections;

import reversi.move.Move;
import reversi.move.MoveComparator;
import reversi.pieces.Board;

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

public class MoveEvaluator {
	
	/*VARIALBES*/
	private State[] states;//Serves as our "tree" but in array form
	private int numParents;
	private ArrayList<Move> possibleMoves;
	private Board board;
	int MovesDeep;
	int choices;

	/*CONSTRUCTOR*/
	public MoveEvaluator(int choices, int MovesDeep, ArrayList<Move> possibleMoves,  Board board)
	{
		/*FOR SIZE: This is how you calculate the size of full k-ary tree... (K^n - 1)/(K-1) ... we'll 
		 * probably use 4 here for K, but I made it so it work for any k-ary tree of varying height so we
		 *  can change things if needed.   
		 */
		this.MovesDeep = MovesDeep;
		this.choices = choices;
		int size = (int)(    (Math.pow(choices, MovesDeep +1) - 1)   /   (choices - 1)      );
		this.states =new State[size];
		this.possibleMoves = possibleMoves;
		this.board = board;	
		
		//Need to iterate though parents later: all the states with children;
		numParents = (int)(Math.pow(choices, MovesDeep)/(choices -1));
		
	}
	private int getNumParents(){
		return this.numParents;
	}
	private int size(){
		return this.states.length;
	}
	//Returns numeric value of the state the move creates
	public Move BestMove()
	{
		if(this.possibleMoves == null){
			return null;//Ideally we'd want to create some sort of pass Move
		}
		predict();
		getScores();
		for(State child: states[0].children){
			if(states[0].score == child.score){
				return child.getMove();
			}
		}
		
		return null;
	}
	private int calcIndex(int Level){
		return (int)(    (Math.pow(choices, Level ) - 1)   /   (choices - 1)      ) ;
	}
	//reseting the board for the next state. Executes and Undo's moves;
	private void next(State current, State next){
		
		ArrayList<Move> path = new ArrayList<Move>();
		do{
			path.add(next.getMove());
			board.undo(current.getMove());
			current = current.previous;
			next = next.previous;
		}while(current!= next && current.previous!= null); 
		//Stops at common ancestor OR if next is on a new level, stops when at root.
		if(current.previous == null && next.previous != null){
			//account for next, being on next level by executing left of root.
			board.execute(current.children[0].getMove());
		}
		for(int i = path.size() -1; i >= 0; i--){
			board.execute(path.get(i));
		}
	}
	private void predict(){
		int level = 0;
		int state = 0;
		//Loop to iterate to every level of the tree
		for(int i = 0; i < this.MovesDeep; i++){
			System.out.println("level "+ i);
			//Loop for iterating through every node at that level
			for(state = state; state < calcIndex(i + 1); state ++){
				//sort possible moves so best 4 are in the front;
				Collections.sort(board.getPossibleMoves(), new MoveComparator());
				java.util.List<Move> moves = board.getPossibleMoves();
				for(int c = 0; c < 4; c ++){
					int childIndex = state * this.choices + 1;
					//place best 4 in the state.children
					State child = new State(moves.get(c));
					child.previous =this.states[state];
					this.states[state].children[c] = child;
				}
				System.out.print(state +", ");
				next(this.states[state], this.states[state + 1]);
			}
			System.out.println();
		}
	}
	private void getScores(){
			for(int state =calcIndex(this.MovesDeep)-1; state >= 0 ; state --){
					this.states[state].trueScore();
			}
	}
	
	public static void main(String[] args){
		
		//TEST THAT DATA STRUCTURE SIZE IS CORRECT
		MoveEvaluator moveEv = new MoveEvaluator(4, 4, null, null);
		System.out.println("Full Size: "+ moveEv.size());
		System.out.println("Number of parents: " + moveEv.getNumParents());
		int level = 3;
		int index = moveEv.calcIndex(level);
		System.out.println("Given the level: " + level);
		System.out.println("The starting index is: " + index);
		//moveEv.BestMove();
		moveEv.getScores();
	}

}
