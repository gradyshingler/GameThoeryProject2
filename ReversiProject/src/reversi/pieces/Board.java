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

import reversi.evaluation.Score;
import reversi.move.Direction;
import reversi.move.Move;
import reversi.move.MoveComparator;

public class Board {
	private final int NO_MOVE_SCORE = 20;
	private final int CUT_VAL = 4;
	
	Disk[][] board = new Disk[10][16];
	public ArrayList<Disk> blackDisks = new ArrayList<Disk>(); //defined as my disks
	public ArrayList<Disk> whiteDisks = new ArrayList<Disk>(); //defines as opponent disks
	List<Move> possibleMoves = new ArrayList<Move>();
	static Score scoreChart;

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
		
		/*
		 * Testing Execute Move and flips as well as undoing flips
		 */
		/*for(int i=0;i<possibleMoves.size();i++){
			System.out.println(" - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
			int total = getMoveScore(possibleMoves.get(i));
			System.out.println("getMoveScore: "+ possibleMoves.get(i).toString()+" = "+total);
			System.out.println("Executing: "+possibleMoves.get(i).toString());
			System.out.println("flips: "+possibleMoves.get(i).getFlips().toString());
			execute(possibleMoves.get(i));
			showBoard();
			System.out.println("Undoing: "+possibleMoves.get(i).toString());
			undo(possibleMoves.get(i));
			showBoard();
		}*/
		
		if(possibleMoves.size()!=0){
			for(int i=0;i<possibleMoves.size();i++){
				getMoveScore(possibleMoves.get(i));
			}
			Collections.sort(possibleMoves, new MoveComparator());
			int small = Math.min(CUT_VAL, possibleMoves.size());
			calculateConsequencesIt(possibleMoves.subList(0, small), 6, 0, 1);
			Collections.sort(possibleMoves, new MoveComparator());
			
			possibleMoves.get(0).printMove();
		} else {
			System.out.println("Pass!");
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
				
				/*if(player == 1) minMaxMove = nextMoves.get(0);
				else if(player == 2) minMaxMove = nextMoves.get(0);
				else throw new IllegalStateException();*/
				//p("  but player<"+player+">'s best move is: "+minMaxMove,pruneVal);
				Move minMaxMove = nextMoves.get(0);
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
	
	private void p(String msg, int tabSpace){
		for(int i=0; i<=tabSpace;i++)
			System.out.print("  ");
		System.out.println(msg);
	}
	
	/**********************************************************
	 * computePossibleMoves Method
	 **********************************************************/
	private ArrayList<Move> computePossibleMoves(int player) {
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
		if(move.getPlayer() == 1){
			Disk newDisk = board[row][col];//Disk newDisk = new Disk(row, col, Cell.MINE);
			newDisk.setCell(Cell.MINE);//board[row][col] = newDisk;
			blackDisks.add(newDisk);
		} else {
			Disk newDisk = board[row][col]; //Disk newDisk = new Disk(row, col, Cell.OPPONENT);
			newDisk.setCell(Cell.OPPONENT);//board[row][col] = newDisk;
			whiteDisks.add(newDisk);
		}
		scoreChart.adjustChart(row, col);
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
		scoreChart.adjustChart(row, col);
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
	private int getMoveScore(Move move){
		TreeMap<Direction, Integer> flips = move.getFlips();
		int row = move.getRow();
		int col = move.getCol();
		scoreChart.adjustChart(row, col);
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
		scoreChart.adjustChart(row, col);
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
