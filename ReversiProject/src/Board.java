import java.util.ArrayList;
import java.util.List;



public class Board {
	static Disk[][] board = new Disk[10][16];
	ArrayList<Move> possibleMoves = new ArrayList<Move>();
	ArrayList<Disk> blackDisks = new ArrayList<Disk>(); //defined as my disks
	ArrayList<Disk> whiteDisks = new ArrayList<Disk>(); //defines as opponent disks

	/*
	 * Creates a new board from an int array
	 */
	public Board(int[][] array) {
		parseToBoardObject(array);
		computePossibleMoves();
	}

	public void makeMove() {
		// (TODO) - I believe here is where we will be doing alot of the strategic planning
		// Right now it just outputs whatever the first move in the possibleMoves array
		possibleMoves.get(0).printMove();
	}
	
	public int getDiskCount(){
		return blackDisks.size()+whiteDisks.size();
	}

	public Disk getDisk(int x, int y) {
		return board[x][y];
	}

	private void computePossibleMoves() {
		for(Disk currDisk: blackDisks){
			updatePossibleMoves(currDisk);
		}
		for (int i = 1; i < 9; i++) {
			for (int j = 1; j < 15; j++) {
				if (canMoveHere(i, j))
					possibleMoves.add(new Move(1, i, j));
			}
		}
	}
	
	private void updatePossibleMoves(Disk disk){
		for(int i=0; i<8;i++){
			addMoveFromDirection(disk, i);
		}
	}

	private void addMoveFromDirection(Disk disk, int dir){
		int x = disk.getxPos();
		int y = disk.getyPos();
		
		int dx = 0;
		int dy = 0;
		if (dir == 0) { dx = 1;	} // East
		else if (dir == 1) {dx = 1;	dy = 1;	} // South-East
		else if (dir == 2) {dx = 0;	dy = 1;	} // South
		else if (dir == 3) {dx = -1; dy = 1; } // South-West
		else if (dir == 4) {dx = -1; dy = 0; } // West
		else if (dir == 5) {dx = -1; dy = -1; } // North-West
		else if (dir == 6) {dx = 0;	dy = -1; } // North
		else if (dir == 7) {dx = 1;	dy = -1; } // North-East

		if (getDisk(x + dy, y + dx).getCell() != Cell.OPPONENT)
			return; //No possible move this way so don't add anything
		else {
			for (int i = 2; i < 15; i++) {
				Cell tempCell = getDisk(x + (dy * i), y + (dx * i)).getCell();
				if (tempCell == Cell.OPPONENT) {
					continue;
				} else if (tempCell == Cell.MINE) {
					//Moving this way is possible so add move to possible move list or update if its already there
					Move newMove = new Move(1, x + (dy * i), y + (dx * i));
					if(possibleMoves.contains(newMove)){
						//get Move and add direction and int to it
						newMove = possibleMoves.get(possibleMoves.indexOf(newMove));
					} else {
						//insert Move to possible Moves
						possibleMoves.add(newMove);
					}
					//Add directional stuff to move
					newMove.addFlips(Move.Direction.NE, i-1);
					return;
				} else {
					return; //No possible move this way so don't add anything
				}
			}
		}
		return; //No possible move this way so don't add anything
	}
	
	private boolean canMoveHere(int x, int y) {
		if (getDisk(x, y).getCell() != Cell.EMPTY)
			return false;
		for (int i = 0; i < 8; i++) {// i stands for direction: 0 = east, 1=
										// south east 2=south etc...
			if (DirectionCheck(x, y, i))
				return true;
		}
		return false;
	}

	private boolean DirectionCheck(int x, int y, int dir) {// 0 = east, 1= south, east 2=south, etc...
		int dx = 0;
		int dy = 0;
		if (dir == 0) {
			dx = 1;
		} // East
		else if (dir == 1) {
			dx = 1;
			dy = 1;
		} // South-East
		else if (dir == 2) {
			dx = 0;
			dy = 1;
		} // South
		else if (dir == 3) {
			dx = -1;
			dy = 1;
		} // South-West
		else if (dir == 4) {
			dx = -1;
			dy = 0;
		} // West
		else if (dir == 5) {
			dx = -1;
			dy = -1;
		} // North-West
		else if (dir == 6) {
			dx = 0;
			dy = -1;
		} // North
		else if (dir == 7) {
			dx = 1;
			dy = -1;
		} // North-East

		if (getDisk(x + dy, y + dx).getCell() != Cell.OPPONENT)
			return false;
		else {
			for (int i = 2; i < 15; i++) {
				Cell tempCell = getDisk(x + (dy * i), y + (dx * i)).getCell();
				if (tempCell == Cell.OPPONENT)
					continue;
				else if (tempCell == Cell.MINE)
					return true;
				else
					return false;
			}
		}
		return false;
	}
	
	//Obsolete function... I kept around for reference to special for loop syntax
	public void printPossibleMoves() {
		for (Move curr : possibleMoves) {
			System.out.print(curr.toString());
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
		System.out.println("     | a b c d e f g h i j k l m n o p");
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
