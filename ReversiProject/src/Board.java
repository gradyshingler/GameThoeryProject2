import java.util.ArrayList;
import java.util.TreeMap;

public class Board {
	Disk[][] board = new Disk[10][16];
	ArrayList<Disk> blackDisks = new ArrayList<Disk>(); //defined as my disks
	ArrayList<Disk> whiteDisks = new ArrayList<Disk>(); //defines as opponent disks
	ArrayList<Move> possibleMoves = new ArrayList<Move>();
	Score scoreChart;

	/*
	 * Creates a new board from an int array
	 */
	public Board(int[][] array) {
		scoreChart = new Score();
		parseToBoardObject(array);
		possibleMoves = computePossibleMoves(1);
	}

	public void makeMove() {
		// (TODO) - I believe here is where we will be doing a lot of the strategic planning
		
		/**********************************************************
		 * Testing Execute Move and flips as well as undoing flips
		 **********************************************************/
		for(int i=0;i<possibleMoves.size();i++){
			System.out.println(" - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
			System.out.println("Executing: "+possibleMoves.get(i).toString());
			System.out.println("flips: "+possibleMoves.get(i).getFlips().toString());
			execute(possibleMoves.get(i));
			showBoard();
			System.out.println("Undoing: "+possibleMoves.get(i).toString());
			undo(possibleMoves.get(i));
			showBoard();
		}
		
		possibleMoves.get(0).printMove();
	}
	
	public int getDiskCount(){
		return blackDisks.size()+whiteDisks.size();
	}

	public Disk getDisk(int x, int y) {
		return board[x][y];
	}
	
	public void flipDisk(Disk disk){
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
	
	public void execute(Move move){
		int row = move.getRow();
		int col = move.getCol();
		if(move.getPlayer() == 1){
			Disk newDisk = new Disk(row, col, Cell.MINE);
			board[row][col] = newDisk;
			blackDisks.add(newDisk);
		} else {
			Disk newDisk = new Disk(row, col, Cell.OPPONENT);
			board[row][col] = newDisk;
			whiteDisks.add(newDisk);
		}
		executeFlips(move);
	}
	
	public void undo(Move move){
		int row = move.getRow();
		int col = move.getCol();
		int player = move.getPlayer();
		Disk oldDisk = board[row][col];
		board[row][col] = new Disk(row,col,Cell.EMPTY);
		if(player == 1){
			blackDisks.remove(oldDisk);
		}else{
			whiteDisks.remove(oldDisk);
		}
		executeFlips(move);
	}
	
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
	
	public ArrayList<Move> getPossibleMoves() {
		return possibleMoves;
	}
	
	/*
	 * Prints the board for testing purposes
	 */
	public void print() {
		System.out.println("Board: disk_Count: " + getDiskCount());
		System.out.println("Black Disks: "+blackDisks.toString());
		System.out.println("White Disks: "+whiteDisks.toString());
		System.out.println("Possible Moves: "+ possibleMoves.toString());
		System.out.println("     | 0 1 2 3 4 5 6 7 8 9 A B C D E F");
		for (int i = 0; i < board.length; i++) {
			System.out.print("Row " + i + "| ");
			for (int j = 0; j < board[i].length; j++) {
				if (possibleMoves.contains(new Move(1, i, j))) {
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
	
	public void parseToBoardObject(int[][] array){
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
