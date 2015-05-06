package reversi.pieces;
/**********************************************************
 * Board Class: 
 * 		Holds all the data required for a Reversi board
 * 			Disk[][] board
 * 			blackDisks
 * 			whiteDisks
 * 			possibleMoves
 * 			scoreChart
 **********************************************************/




import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.TreeMap;

import reversi.evaluation.MoveEvaluator;
import reversi.evaluation.Score;
import reversi.move.Direction;
import reversi.move.Move;
import reversi.move.MoveComparator;

public class Board {
	private final int NO_MOVE_SCORE = 20;
	private final int CUT_VAL = 4;
	private final int DEPTH = 9;
	
	Disk[][] board = new Disk[10][16];
	public ArrayList<Disk> blackDisks = new ArrayList<Disk>(); //defined as my disks
	public ArrayList<Disk> whiteDisks = new ArrayList<Disk>(); //defines as opponent disks
	public List<Move> possibleMoves = new ArrayList<Move>();
	public static Score scoreChart;

	/**********************************************************
	 * Constructor
	 **********************************************************/
	public Board(int[][] array) {
		parseToBoardObject(array);
		scoreChart = new Score(this);
		possibleMoves = computePossibleMoves(1);
	}
	
	/**********************************************************
	 * Getters and Setters
	 **********************************************************/
	public int getDiskCount(){
		return blackDisks.size()+whiteDisks.size();
	}

	public Disk getDisk(int x, int y) {
		return board[x][y];
	}
	
	public List<Move> getPossibleMoves() {
		return possibleMoves;
	}

	public void makeMove() {
		// (TODO) - I believe here is where we will be doing a lot of the strategic planning
		
		if(possibleMoves.size()!=0){
			for(int i=0;i<possibleMoves.size();i++){
				getMoveScore(possibleMoves.get(i));
			}
			Collections.sort(possibleMoves, new MoveComparator());
			while(possibleMoves.size()>CUT_VAL) possibleMoves.remove(CUT_VAL);
			//System.out.println(possibleMoves);
			
			//calculateConsequencesFinalIt(possibleMoves, DEPTH, 0, 1);
			calculateConsequences(possibleMoves, DEPTH-1, 0, 1);// < - - - USE THIS ONE
			Collections.sort(possibleMoves, new MoveComparator());
			//System.out.println(possibleMoves);
			possibleMoves.get(0).printMove();			
		} else {
			System.out.println("Pass!");
		}
	}
	
	/**********************************************************
	 * Calculate Consequences Method Iteration
	 **********************************************************/
	private void calculateConsequencesFinalIt(List<Move> moves, int depth, int pruneVal, int player){
		List<List<Move>> tree = new ArrayList<List<Move>>();
		int[] countList = new int[depth+1];
		for(int i = 0; i < countList.length; i++){
			countList[i] = -1;
		}
		
		int currDepth = 0;
		List<Move> currList = moves;
		tree.add(currList);
		//System.out.println("set tree["+currDepth+"] as currMoveList");
		 
		while(currList != null){
			//System.out.println("Entering Outside Loop: depth("+currDepth+") player("+((currDepth%2)+1)+")");
			if(currDepth < depth){
				countList[currDepth]++;
				if(countList[currDepth] == tree.get(currDepth).size()){
					//System.out.println("Merge_Up");
					if(currDepth == 0){
						//System.out.println("I think were done");
						//printDualList(tree, countList);
						return;
					}
					Collections.sort(tree.get(currDepth),new MoveComparator());
					Move minMaxMove = tree.get(currDepth).get(0);
					tree.get(currDepth-1).get(countList[currDepth-1]).consequenceScore = ((minMaxMove.positionScore + minMaxMove.consequenceScore)*-1);
					//System.out.println("Undo old move and Delete tree[depth]");
					undo(tree.get(currDepth-1).get(countList[currDepth-1]));
					tree.remove(currDepth);
					
					countList[currDepth] = -1;
					currDepth--;
					currList = tree.get(currDepth);
				} else {
					Move currMove = currList.get(countList[currDepth]);
					execute(currMove);
					currDepth++;
					
					List<Move> nextMoves = computePossibleMoves((currDepth%2)+1);
					if(nextMoves.size() != 0){
						for(int i=0;i<nextMoves.size();i++){ getMoveScore(nextMoves.get(i)); }
						Collections.sort(nextMoves, new MoveComparator());
						while(nextMoves.size()>CUT_VAL) nextMoves.remove(CUT_VAL);
						currList = nextMoves;
						tree.add(currList);
						//System.out.println("set tree["+currDepth+"] as currMoveList");
					} else { 
						//System.out.println("Other player has no moves");
						tree.get(currDepth-1).get(countList[currDepth-1]).consequenceScore = NO_MOVE_SCORE;
						//System.out.println("Undo old move and Delete tree[depth]");
						undo(tree.get(currDepth-1).get(countList[currDepth-1]));
						//tree.remove(currDepth);
						
						countList[currDepth] = -1;
						currDepth--;
						currList = tree.get(currDepth);
					}
				}
			} else if(currDepth >= depth){
				//System.out.println("Figure out cons scores");
				//printDualList(tree, countList);
				
				//System.out.println("set first value of tree[depth] as consScore for pos counter of tree[depth-1]");
				Move minMaxMove = tree.get(currDepth).get(0);
				tree.get(currDepth-1).get(countList[currDepth-1]).consequenceScore = ((minMaxMove.positionScore + minMaxMove.consequenceScore)*-1);
				//printDualList(tree, countList);
				
				//System.out.println("Undo old move and Delete tree[depth]");
				undo(tree.get(currDepth-1).get(countList[currDepth-1]));
				tree.remove(currDepth);
				
				countList[currDepth] = -1;
				currDepth--;
				currList = tree.get(currDepth);
				//printDualList(tree, countList);
			}
		}
	}
	
	/**********************************************************
	 * Calculate Consequences New It Method
	 **********************************************************/
	private void calculateConsequencesNewIt(List<Move> moves, int depth, int pruneVal, int player){
		
		List<List<Move>> tree = new ArrayList<List<Move>>();
		int[] countList = new int[depth+2];
		
		tree.add(moves);
		countList[0] = 0;
		
		int currDepth = 0;
		int currPlayer = player;
		List<Move> currList = tree.get(currDepth);
		
		while(countList[0] < moves.size() || tree.size() > 0){			
			
			while(countList[currDepth] < currList.size()){
				execute(currList.get(countList[currDepth]));
				List<Move> nextMoves = computePossibleMoves((player%2)+1);
				if(nextMoves.size() != 0){
					for(int i=0;i<nextMoves.size();i++){ getMoveScore(nextMoves.get(i)); }
					Collections.sort(nextMoves, new MoveComparator());
					
					if(currDepth < depth){
						int small = Math.min(CUT_VAL, nextMoves.size());
						List<Move> trim = nextMoves.subList(0, small);
						tree.add(trim);
						currList = trim;
						countList[currDepth]++;
						currDepth++;
						player++;
					} else {
						Move minMaxMove = nextMoves.get(0);
						System.out.println("Best move is "+minMaxMove);
						
						int tempCons = ((minMaxMove.positionScore+minMaxMove.consequenceScore)*-1);
						currList.get(countList[currDepth]).consequenceScore = (tempCons);
						undo(currList.get(countList[currDepth]));
						countList[currDepth]++;
						currList = tree.get(currDepth);
					}
					
					printDualList(tree,countList);
					System.out.println("currDepth = "+currDepth);
					System.out.println("currList = "+currList);
					
					
				} else {
					currList.get(countList[currDepth]).consequenceScore = NO_MOVE_SCORE;
					System.out.println("NO move score");
					countList[currDepth]++;
					currDepth++;
					player++;
				}
			}
			System.out.println("Entering clean-up while loop");
			while(countList[currDepth] == CUT_VAL ){
				if(currDepth == 0) return;
				System.out.println("Condense bottom full row <"+currDepth+">");
				undo(currList.get(countList[currDepth-1]-1));
				//find best and put in above's consequence
				Collections.sort(tree.get(currDepth), new MoveComparator());
				Move minMaxMove = tree.get(currDepth).get(0);
				int tempCons = ((minMaxMove.positionScore+minMaxMove.consequenceScore)*-1);
				//System.out.println(tree.get(currDepth-1).get(countList[currDepth-1]-1)); 
				tree.get(currDepth-1).get(countList[currDepth-1]-1).consequenceScore = tempCons;
				tree.remove(currDepth);
				countList[currDepth] = 0;
				currDepth--;
				player++;
				printDualList(tree, countList);
			}			
		}
	}
	
	/**********************************************************
	 * Calculate Consequences Method
	 **********************************************************/
	private void calculateConsequences(List<Move> moves, int depth, int pruneVal, int player){
		player = (player%2)+1; //This shenanighans maps 1->2 or 2->1
		//p("newPlayer: "+player+" search depth: "+depth,pruneVal);
		pruneVal++;
		
		for(Move currMove: moves){
			execute(currMove);
			//p("If player<"+((player%2)+1)+"> moves "+currMove+" then player<"+player+"> can move:",pruneVal);
			List<Move> nextMoves = computePossibleMoves(player);
			if(nextMoves.size() != 0){
				for(int i=0;i<nextMoves.size();i++){
					getMoveScore(nextMoves.get(i));
				}
				Collections.sort(nextMoves, new MoveComparator());
				//print(nextMoves);
				if(depth > 0){
					int small = Math.min(CUT_VAL, nextMoves.size());
					nextMoves = nextMoves.subList(0, small);
					calculateConsequences(nextMoves, depth-1, pruneVal, player);
					Collections.sort(nextMoves, new MoveComparator());
				}
				Move minMaxMove = nextMoves.get(0);
				//p("  but player<"+player+">'s best move is: "+minMaxMove,pruneVal);
				int tempCons = ((minMaxMove.positionScore+minMaxMove.consequenceScore)*-1);
				//p("  and because depth is 0 we stop here and set player<"+((player%2)+1)+">'s consequence score for"+currMove+" to: "+tempCons, pruneVal);
				currMove.consequenceScore = (tempCons);
			} else {
				//p("  player<"+player+"> has no moves so we set  player<"+((player%2)+1)+">'s consequence score to:"+NO_MOVE_SCORE,pruneVal);
				currMove.consequenceScore = NO_MOVE_SCORE; 
			}
			undo(currMove);
		}
		
		//p("  To Recap: player<"+((player%2)+1)+">'s move set is: "+moves,pruneVal);
	}
	
	/**********************************************************
	 * Calculate Consequences Method Iteration
	 **********************************************************/
	private void calculateConsequencesIt(List<Move> moves, int depth, int pruneVal, int player){
		
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
				execute(tempMove);
				backStack.push(tempMove);
				List<Move> nextMoves = computePossibleMoves((currDepth%2)+1);
				
				for(int i=0; i<nextMoves.size(); i++){ getMoveScore(nextMoves.get(i)); }
				
				Collections.sort(nextMoves, new MoveComparator());
				
				int small = Math.min(CUT_VAL, nextMoves.size());
				nextMoves = nextMoves.subList(0, small);
				
				Stack<Move> nextStack = new Stack<Move>();
				for(Move currMove: nextMoves) nextStack.push(currMove);
				stackList.push(nextStack);
				printStackList(stackList);
			}
			currDepth--;
			System.out.println("BackList: "+ backStack);
			Move minMaxMove = stackList.pop().get(0);
			int tempCons = ((minMaxMove.positionScore+minMaxMove.consequenceScore)*-1);
			Move tempMove = backStack.pop();
			tempMove.consequenceScore = tempCons;
			undo(tempMove);
			printStackList(stackList);
			//return;
		}
	}
	
	private void printStackList(Stack<Stack<Move>> toPrint){
		for(Stack<Move> curr : toPrint){
			System.out.println("Player: "+curr.peek().getPlayer()+" "+curr);
		}
	}
	
	private void printDualList(List<List<Move>> toPrint, int[] array){
		System.out.println("Tree: ");
		int counter=0;
		for(List<Move> curr : toPrint){
			System.out.println("\tP<"+curr.get(0).getPlayer()+"> pos:"+array[counter]+" Depth:"+ counter++ +"\t"+curr);
		}
	}
	
	private void printArray(int[] array){
		System.out.print("[");
		for(int i=0; i< array.length;i++){
			System.out.print(" "+array[i]+" ");
		}
		System.out.println("]");
	}
	
	private void p(String msg, int tabSpace){
		for(int i=0; i<=tabSpace;i++)
			System.out.print("  ");
		System.out.println(msg);
	}
	
	/**********************************************************
	 * computePossibleMoves Method
	 **********************************************************/
	public ArrayList<Move> computePossibleMoves(int player) {
		ArrayList<Move> tempMoves = new ArrayList<Move>();
		if(player == 1){
			for(Disk currDisk: blackDisks){
				updatePossibleMoves(currDisk, tempMoves, player);
			}
		} else if(player == 2){
			for(Disk currDisk: whiteDisks){
				updatePossibleMoves(currDisk, tempMoves, player);
			}
		}
		return tempMoves;
	}
	
	
	/**********************************************************
	 * Helper methods for computing possible moves
	 **********************************************************/
	private void updatePossibleMoves(Disk disk, ArrayList<Move> moves, int player){
		for(int i=0; i<8;i++){
			addMoveFromDirection(disk, i, moves, player);
		}
	}

	private void addMoveFromDirection(Disk disk, int dir, ArrayList<Move> moves, int player){		
		int x = disk.getxPos();
		int y = disk.getyPos();

		Cell otherType = Cell.WALL;		
		if(player == 1){ otherType = Cell.OPPONENT; }
		else if(player == 2) { otherType = Cell.MINE; }
		
		int dx = 0;
		int dy = 0;		
		if (dir == 0) {dx = 0;	dy = -1; } 		// North
		else if (dir == 1) {dx = 1;	dy = -1; } 	// North-East
		else if (dir == 2) {dx = 1;	dy = 0; } 	// East
		else if (dir == 3) {dx = 1;	dy = 1;	} 	// South-East
		else if (dir == 4) {dx = 0;	dy = 1;	} 	// South
		else if (dir == 5) {dx = -1; dy = 1; } 	// South-West
		else if (dir == 6) {dx = -1; dy = 0; } 	// West
		else if (dir == 7) {dx = -1; dy = -1; } // North-West

		if (getDisk(x + dy, y + dx).getCell() != otherType)
			return; //No possible move this way so don't add anything
		else {
			for (int i = 2; i < 15; i++) {
				Cell tempCell = getDisk(x + (dy * i), y + (dx * i)).getCell();
				if (tempCell == otherType) {
					continue;
				} else if (tempCell == Cell.EMPTY) {
					//Moving this way is possible so add move to possible move list or update if its already there
					Move newMove = new Move(player, x + (dy * i), y + (dx * i));
					if(moves.contains(newMove)){
						//get Move and add direction and int to it
						newMove = moves.get(moves.indexOf(newMove));
					} else {
						//insert Move to possible Moves
						moves.add(newMove);
					}
					//Add directional stuff to move
					newMove.addFlips(Direction.getDir((dir+4)%8), i-1); //The ((dir+4)%8) changes the direction 180 for the execute methods
					return;
				} else {
					return; //No possible move this way so don't add anything
				}
			}
		}
		return; //No possible move this way so don't add anything
	}
	
	/**********************************************************
	 * Execute and Undo methods 
	 **********************************************************/
	public void execute(Move move){
		int row = move.getRow();
		int col = move.getCol();
		if(board[row][col].getCell() != Cell.EMPTY) throw new IllegalArgumentException("Executing on a non empty cell!");
		if(move.getPlayer() == 1){
			Disk newDisk = board[row][col];//Disk newDisk = new Disk(row, col, Cell.MINE);
			newDisk.setCell(Cell.MINE);//board[row][col] = newDisk;
			blackDisks.add(newDisk);
		} else {
			Disk newDisk = board[row][col]; //Disk newDisk = new Disk(row, col, Cell.OPPONENT);
			newDisk.setCell(Cell.OPPONENT);//board[row][col] = newDisk;
			whiteDisks.add(newDisk);
		}
		//scoreChart.adjustChart(row, col);
		executeFlips(move);
	}
	
	public void undo(Move move){
		int row = move.getRow();
		int col = move.getCol();
		int player = move.getPlayer();
		Disk oldDisk = board[row][col];
		if(player == 1){
			blackDisks.remove(oldDisk);
		}else{
			whiteDisks.remove(oldDisk);
		}
		//scoreChart.adjustChart(row, col);
		board[row][col].setCell(Cell.EMPTY);
		executeFlips(move);
	}
	
	
	/**********************************************************
	 * Execute and undo Helper Methods
	 **********************************************************/
	private void executeFlips(Move move){
		TreeMap<Direction, Integer> flips = move.getFlips();
		int row = move.getRow();
		int col = move.getCol();
		
		for (Direction dir : flips.keySet()){
			int dx = 0;
			int dy = 0;
			
			if (dir == Direction.N) {dx = 0;	dy = -1; } 		// North
			else if (dir == Direction.NE) {dx = 1;	dy = -1; } 	// North-East
			else if (dir == Direction.E) {dx = 1;	dy = 0; } 	// East
			else if (dir == Direction.SE) {dx = 1;	dy = 1;	} 	// South-East
			else if (dir == Direction.S) {dx = 0;	dy = 1;	} 	// South
			else if (dir == Direction.SW) {dx = -1; dy = 1; } 	// South-West
			else if (dir == Direction.W) {dx = -1; dy = 0; } 	// West
			else if (dir == Direction.NW) {dx = -1; dy = -1; } 	// North-West
			else {System.out.printf("Direction Error in Board.executeFlips"); }
			
			for(int i = 1 ; i <= flips.get(dir); i++)
			{
				flipDisk(board[row+(dy*i)][col+(dx*i)]);
			}
		}
	}
	
	private void flipDisk(Disk disk){
		if(disk.getCell() == Cell.MINE){
			blackDisks.remove(disk);
			whiteDisks.add(disk);
			disk.flip();
		} else if(disk.getCell() == Cell.OPPONENT){
			whiteDisks.remove(disk);
			blackDisks.add(disk);
			disk.flip();
		} else {
			System.out.println("Disk Type Error: expected MINE or OPPONENENT, recieved: "+disk.getCell().getVal());
			System.out.println("Disk pos = "+disk.getxPos()+","+disk.getyPos());
		}
	}
	
	/**********************************************************
	 * Compute A Moves Position Score and sets the move score to that value
	 **********************************************************/
	public int getMoveScore(Move move){
		TreeMap<Direction, Integer> flips = move.getFlips();
		int row = move.getRow();
		int col = move.getCol();
		//scoreChart.adjustChart(row, col);
		int total = scoreChart.getRawScore(row,col);
		//System.out.println("pos: "+row+","+(col)+" has a score of: "+total);
		
		for (Direction dir : flips.keySet()){
			int dx = 0;
			int dy = 0;
			
			if (dir == Direction.N) {dx = 0;	dy = -1; } 		// North
			else if (dir == Direction.NE) {dx = 1;	dy = -1; } 	// North-East
			else if (dir == Direction.E) {dx = 1;	dy = 0; } 	// East
			else if (dir == Direction.SE) {dx = 1;	dy = 1;	} 	// South-East
			else if (dir == Direction.S) {dx = 0;	dy = 1;	} 	// South
			else if (dir == Direction.SW) {dx = -1; dy = 1; } 	// South-West
			else if (dir == Direction.W) {dx = -1; dy = 0; } 	// West
			else if (dir == Direction.NW) {dx = -1; dy = -1; } 	// North-West
			else {System.out.printf("Direction Error in Board.executeFlips"); }
			
			for(int i = 1 ; i <= flips.get(dir); i++)
			{
				int tempScore = scoreChart.getRawScore(row+(dy*i), col+(dx*i));
				//System.out.println("pos: "+(row+(dy*i))+","+(col+(dx*i))+" has a score of: "+tempScore);
				total+=tempScore;
			}
		}
		//scoreChart.adjustChart(row, col);
		move.positionScore = total;
		return total;
	}
	
	/**********************************************************
	 * Various Printing constructs
	 * 	print - prints with possible moves
	 *  showBoard - prints without possible moves
	 **********************************************************/
	public void print(List<Move> posMoves) {
		System.out.println("Board: disk_Count: " + getDiskCount());
		System.out.println("Black Disks: "+blackDisks.toString());
		System.out.println("White Disks: "+whiteDisks.toString());
		System.out.println("posMoves: "+ posMoves.toString());
		System.out.println("     | 0 1 2 3 4 5 6 7 8 9 A B C D E F");
		for (int i = 0; i < board.length; i++) {
			System.out.print("Row " + i + "| ");
			for (int j = 0; j < board[i].length; j++) {
				if (posMoves.contains(new Move(1, i, j))) {
					System.out.print("X ");
				} else{
					String val = board[i][j].getCell().getVal();
					if(val.equals("0")){
						System.out.print("- ");//2 spaces
					}else if(val.equals("3")){
						System.out.print("  ");//+ and space
					}else{
						System.out.print(board[i][j].getCell().getVal() + " ");
					}
				}
			}
			System.out.println(" ");
		}
	}
	
	public void showBoard() {
		System.out.println("Board: disk_Count: " + getDiskCount());
		System.out.println("Black Disks: "+blackDisks.toString());
		System.out.println("White Disks: "+whiteDisks.toString());
		System.out.println("     | 0 1 2 3 4 5 6 7 8 9 A B C D E F");
		for (int i = 0; i < board.length; i++) {
			System.out.print("Row " + i + "| ");
			for (int j = 0; j < board[i].length; j++) {
				String val = board[i][j].getCell().getVal();
				if(val.equals("0")){
					System.out.print("- ");//2 spaces
				}else if(val.equals("3")){
					System.out.print("  ");//+ and space
				}else{
					System.out.print(board[i][j].getCell().getVal() + " ");
				}
			}
			System.out.println(" ");
		}
	}
	
	/**********************************************************
	 * Constructor helper, parses a 2d int array to the 2d disk array field
	 **********************************************************/
	private void parseToBoardObject(int[][] array){
		int rowLength = 8;
		int wallLength = 4;
		boolean top = true;
		for (int i = 0; i < 10; i++) {
			if (i == 0 || i == 9) {
				for (int j = 0; j < 16; j++) {
					board[i][j] = new Disk(i, j, Cell.WALL);

				}
			} else {
				for (int j = 0; j < wallLength; j++) {
					board[i][j] = new Disk(i, j, Cell.WALL);
				}
				for (int j = wallLength; j < rowLength + wallLength; j++) {
					Cell tempCell = Cell.getCell(array[i - 1][j - wallLength]);
					Disk diskToAdd = new Disk(i, j, tempCell); 
					board[i][j] = diskToAdd;
					if (tempCell == Cell.MINE) {
						blackDisks.add(diskToAdd);
					} else if(tempCell == Cell.OPPONENT){
						whiteDisks.add(diskToAdd);
					}
				}
				for (int j = (wallLength) + rowLength; j < 16; j++) {
					board[i][j] = new Disk(i, j, Cell.WALL);
				}
				if (top) {
					rowLength += 2;
					if (wallLength == 1)
						top = false;
					wallLength -= 1;
				}
				if (!top) { // not top
					rowLength -= 2;
					wallLength += 1;
				}
			}
		}
	}
}
