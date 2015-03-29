

import java.util.List;
import java.util.TreeMap;

public class Move {
	int row;
	int col;
	int PrevScore;
	int CurrScore;
	int player;// 1 is player1, 2 is player2
	List<String> BoardChanges;
	public TreeMap<Direction, Integer> flips;
	
	public static enum Direction{
		N(0), NE(1), E(2), SE(3), S(4), SW(5), W(6), NW(7);
		
		public int dir;

		Direction(int n) {
			dir = n;
		}
		
		public static Direction getDir(int i) {
			switch (i) {
			case 0:
				return Direction.N;
			case 1:
				return Direction.NE;
			case 2:
				return Direction.E;
			case 3:
				return Direction.SE;
			case 4:
				return Direction.S;
			case 5:
				return Direction.SW;
			case 6:
				return Direction.W;
			case 7:
				return Direction.NW;
			default:
				return Direction.N;
			}
		}

	};
	public Move(int p, int r, int c) {
		row = r;
		col= c;
		player = p;
		flips = new TreeMap<Direction, Integer>();
	}
	
	public void addFlips(Direction dir, int num){
		flips.put(dir, num);
	}
	public void addBoardChange(int r, int c, int sc1, int sc2){
		BoardChanges.add(r + "," + c + "," + sc1 + "," + sc2);
	}
	private void flip(){
		for (Direction dir : flips.keySet()){
			if(dir.equals(Direction.N))
			{
				for(int i = 1 ; i< flips.get(dir); i++)
				{
					Board.board[row-i][col].flip();
					//Board.board[row][col-i] need to flip in opposite direction of x and y destination coordinates
				}
			}else if(dir.equals(Direction.NE))
			{
				for(int i = 1 ; i< flips.get(dir); i++)
				{
					Board.board[row-i][col-i].flip();
				}
			}else if(dir.equals(Direction.E))
			{
				for(int i = 1 ; i< flips.get(dir); i++)
				{
					Board.board[row][col+i].flip();
				}
			}else if(dir.equals(Direction.SE))
			{
				for(int i = 1 ; i< flips.get(dir); i++)
				{
					Board.board[row+i][col-i].flip();
				}
			}else if(dir.equals(Direction.S))
			{
				for(int i = 1 ; i< flips.get(dir); i++)
				{
					Board.board[row+i][col].flip();
				}
			}else if(dir.equals(Direction.SW))
			{
				for(int i = 1 ; i< flips.get(dir); i++)
				{
					Board.board[row+i][col+i].flip();
				}
			}else  if(dir.equals(Direction.W))
			{
				for(int i = 1 ; i< flips.get(dir); i++)
				{
					Board.board[row][col+i].flip();
				}
			}else if(dir.equals(Direction.NW))
			{
				for(int i = 1 ; i< flips.get(dir); i++)
				{
					Board.board[row-i][col+i].flip();
				}
			}else{
				System.out.printf("Direction Error in move.execute");
			}
		}
	}
	public void Execute(){
		if(player == 1){
			Disk newDisk = new Disk(col, row, Cell.MINE);
			Board.board[row][col] = newDisk;
			Board.blackDisks.add(newDisk);
		}else{
			Disk newDisk = new Disk(col, row, Cell.OPPONENT);
			Board.board[row][col] = newDisk;
			Board.whiteDisks.add(newDisk);
		}
		flip();
	}
	public void Undo(){
		Disk oldDisk = Board.board[row][col];
		Board.board[row][col] = null;
		if(player == 1){
			Board.blackDisks.remove(oldDisk);
		}else{
			Board.whiteDisks.remove(oldDisk);
		}
		flip();
	}
	public int getConvertedRow() {
		return row;

	}

	public int getConvertedCol() {
		if (row <= 4)
			return col + (row - 4);
		else
			return col - ((row - 5));
	}

	//This should be used to print the one and final move to submit
	public void printMove() {
		System.out.println(getConvertedRow() + " " + getConvertedCol());
	}
	
	public int getFlipScore(){
		int toReturn = 0;
		for(int curr: flips.values()){
			toReturn+=curr;
		}
		return toReturn;
	}

	public boolean equals(Object o) {
		if (o instanceof Move) {
			Move tempMove = (Move) o;
			return (col == tempMove.col && row == tempMove.row);
		} else {
			return false;
		}
	}

	public String toString() {
		return "("+row + "," + col+"):"+getFlipScore();
	}
}
