/**********************************************************
 * Move Class:
 * 		Holds data representing a typical move on a reversi board
 * 			row
 * 			col
 * 			player
 * 			flips
 **********************************************************/

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
	
	/**********************************************************
	 * Constructor
	 **********************************************************/
	public Move(int p, int r, int c) {
		row = r;
		col= c;
		player = p;
		flips = new TreeMap<Direction, Integer>();
	}
	
	/**********************************************************
	 * Getters, Setters, toString and equals
	 **********************************************************/
	public void addFlips(Direction dir, int num){
		flips.put(dir, num);
	}
	
	public void addBoardChange(int r, int c, int sc1, int sc2){
		BoardChanges.add(r + "," + c + "," + sc1 + "," + sc2);
	}
	
	public int getPlayer(){
		return player;
	}
	
	public TreeMap<Direction, Integer> getFlips(){
		return flips;
	}
	
	public int getRow(){
		return row;
	}
	
	public int getCol(){
		return col;
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
	
	public String toString() {
		return "("+row + "," + col+"):"+getFlipScore();
	}
	
	public boolean equals(Object o) {
		if (o instanceof Move) {
			Move tempMove = (Move) o;
			return (col == tempMove.col && row == tempMove.row);
		} else {
			return false;
		}
	}

	/**********************************************************
	 * Prints the final move to be received by the game server
	 **********************************************************/
	public void printMove() {
		System.out.println(getConvertedRow() + " " + getConvertedCol());
	}
	
	/**********************************************************
	 * returns the amount of flips this move generates
	 **********************************************************/
	public int getFlipScore(){
		int toReturn = 0;
		for(int curr: flips.values()){
			toReturn+=curr;
		}
		return toReturn;
	}
}
