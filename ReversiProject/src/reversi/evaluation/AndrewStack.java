package reversi.evaluation;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

import reversi.move.Move;
import reversi.move.MoveComparator;
import reversi.pieces.Board;

public class AndrewStack {
final int CUT_VAL = 4; 
private void calculateConsequencesIt(Board board, List<Move> moves, int depth, int pruneVal, int player){
		
		Stack<Move> currList = new Stack<Move>();
		
		Stack<Stack<Move>> stackList = new Stack<Stack<Move>>();
		for(Move currMove: moves) currList.push(currMove);
		
		stackList.add(currList);
		Stack<Move> backStack = new Stack<Move>();
		
		int currDepth = 0;
		
		while( stackList.size() > 0 ){
			while( currDepth < depth ){
				Move tempMove = stackList.peek().pop();
				currDepth++;
				System.out.println("tempMove: "+tempMove);
				board.execute(tempMove);
				backStack.push(tempMove);
				List<Move> nextMoves = board.computePossibleMoves((currDepth%2)+1);
				
				for(int i=0; i<nextMoves.size(); i++){ board.getMoveScore(nextMoves.get(i)); }
				
				Collections.sort(nextMoves, new MoveComparator());
				
				int small = Math.min(CUT_VAL, nextMoves.size());
				nextMoves = nextMoves.subList(0, small);
				
				Stack<Move> nextStack = new Stack<Move>();
				for(Move currMove: nextMoves) nextStack.push(currMove);
				stackList.push(nextStack);
				//printStackList(stackList);
			}
			currDepth--;
			System.out.println("BackList: "+ backStack);
			Move minMaxMove = stackList.pop().get(0);
			int tempCons = ((minMaxMove.positionScore+minMaxMove.consequenceScore)*-1);
			Move tempMove = backStack.pop();
			tempMove.consequenceScore = tempCons;
			board.undo(tempMove);
			//printStackList(stackList);
			//return;
		}
	}
}
