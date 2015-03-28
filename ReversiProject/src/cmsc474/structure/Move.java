package cmsc474.structure;

import java.util.List;
import java.util.TreeMap;

public class Move {
	int xPos;
	int yPos;
	int PrevScore;
	int CurrScore;
	int player;// 1 is player1, 2 is player2
	List<String> BoardChanges;
	public enum Direction{
		N, NE, E, SE, S, SW, W, NW
	};
	public TreeMap<Direction, Integer> Flips;
	public Move(int p, int x, int y, int ps, int cs ) {
		xPos = x;
		yPos = y;
		PrevScore = ps;
		CurrScore = cs;
		player = p;
		Flips = new TreeMap<Direction, Integer>();
	}
	
	public void addFlips(Direction dir, int num){
		Flips.put(dir, num);
	}
	public void addBoardChange(int x, int y, int sc1, int sc2){
		BoardChanges.add("x,y,sc1,sc2");
	}
	public int getConvertedRow() {
		return xPos;

	}

	public int getConvertedCol() {
		if (xPos <= 4)
			return yPos + (xPos - 4);
		else
			return yPos - ((xPos - 5));
	}

	public void printMove() {
		System.out.println(getConvertedRow() + " " + getConvertedCol());
	}

	public boolean equals(Object o) {
		if (o instanceof Move) {
			Move tempMove = (Move) o;
			return (xPos == tempMove.xPos && yPos == tempMove.yPos);
		} else {
			return false;
		}

	}

	public String toString() {
		return xPos + ":" + yPos;
	}
}
