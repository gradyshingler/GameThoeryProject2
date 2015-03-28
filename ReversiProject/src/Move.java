

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
	public Move(int p, int x1, int y1) {
		xPos = x1;
		yPos = y1;
		player = p;
		Flips = new TreeMap<Direction, Integer>();
	}
	
	public void addFlips(Direction dir, int num){
		Flips.put(dir, num);
	}
	public void addBoardChange(int x, int y, int sc1, int sc2){
		BoardChanges.add("x,y,sc1,sc2");
	}
	public void Execute(){
		for (Direction dir : Flips.keySet()){
			if(dir.equals(Direction.N))
			{
				for(int i = 0 ; i< Flips.get(dir); i++)
				{
					//Board.board[x][y-1] need to flip in opposite direction of x and y destination coordinates
				}
			}else if(dir.equals(Direction.NE))
			{
				for(int i = 0 ; i< Flips.get(dir); i++)
				{
					
				}
			}else if(dir.equals(Direction.E))
			{
				for(int i = 0 ; i< Flips.get(dir); i++)
				{
					
				}
			}else if(dir.equals(Direction.SE))
			{
				for(int i = 0 ; i< Flips.get(dir); i++)
				{
					
				}
			}else if(dir.equals(Direction.S))
			{
				for(int i = 0 ; i< Flips.get(dir); i++)
				{
					
				}
			}else if(dir.equals(Direction.SW))
			{
				for(int i = 0 ; i< Flips.get(dir); i++)
				{
					
				}
			}else  if(dir.equals(Direction.W))
			{
				for(int i = 0 ; i< Flips.get(dir); i++)
				{
					
				}
			}else if(dir.equals(Direction.NW))
			{
				for(int i = 0 ; i< Flips.get(dir); i++)
				{
					
				}
			}else{
				System.out.printf("Direction Error in move.execute");
			}
		}
	}
	public void Undo(){
		
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

	//This should be used to print the one and final move to submit
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
