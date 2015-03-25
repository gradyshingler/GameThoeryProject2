package cmsc474.structure;

import java.util.ArrayList;
import java.util.List;

import cmsc474.boardEnum.Cell;

public class Board {
	Cell[][] board = new Cell[10][16];
	int disk_count = 0;
	ArrayList<Move> possibleMoves = new ArrayList<Move>();
	
	/*
	 * Creates a new board from an int array
	 */
	public Board(int[][] array){
		int rowLength = 8;
		int wallLength = 4;
		boolean top = true;
		for(int i=0;i<10;i++){
			if(i==0||i==9) {
				for(int j=0;j<16;j++){
					board[i][j] = Cell.WALL;
					
				}
			} else {
				for(int j=0;j<wallLength;j++){
					board[i][j] = Cell.WALL;
				}
				for(int j = wallLength;j<rowLength+wallLength;j++){
					Cell tempCell = Cell.getCell(array[i-1][j-wallLength]);
					board[i][j] = tempCell;
					if(tempCell != Cell.EMPTY) disk_count+=1;
				}
				for(int j=(wallLength)+rowLength;j<16;j++){
					board[i][j] = Cell.WALL;
				}
				if(top){
					rowLength+=2;
					if(wallLength == 1) top = false;
					wallLength-=1;
				} if(!top) { //not top
					rowLength-=2;
					wallLength += 1;
				}
			}
		}
	}
	
	public void makeMove(){
		//(TODO)
		computePossibleMoves();
	}
	
	/*
	 * Prints the board for testing purposes
	 */
	public void print(){	
		System.out.println("Board: disk_Count: "+disk_count);
		for(int i = 0; i<board.length; i++){
			System.out.print("Row "+i+"| ");
			for(int j = 0; j<board[i].length; j++){
				System.out.print(board[i][j].getVal()+" ");
			}
			System.out.println(" ");
		}
	}
	
	public Cell getCell(int x, int y){
		return board[x][y];
	}
	
	private void computePossibleMoves(){
		//Move[] toReturn = new Move[88-disk_count];
		for(int i = 1;i<9;i++){
			for(int j=1;j<15;j++){
				if(canMoveHere(i,j)) possibleMoves.add(new Move(i,j));
			}
		}
		//return possibleMoves;
	}
	
	private boolean canMoveHere(int x, int y){
		if(getCell(x,y) != Cell.EMPTY) return false;
		for(int i=0;i<8;i++){//i stands for direction: 0 = east, 1= south east 2=south etc...
			if(DirectionCheck(x,y,i))return true;
		}
		return false;
	}
	
	private boolean DirectionCheck(int x, int y, int dir){// 0 = east, 1= south east 2=south etc...
		int dx = 0;
		int dy = 0;
		if(dir == 0){ dx=1; }
		else if(dir == 1) { dx=1;dy=1; }
		else if(dir == 2) { dx=0;dy=1; }
		//etc... etc... ran out of Time tonight will finish tomorrow
		
		
		if(getCell(x+dx,y+dy)!=Cell.OPPONENT) return false;
		else {
			int counter=2;
			for(int i=2;i<15;i++){
				Cell tempCell = getCell(x+(dx*i),y+(dx*i));
				if(tempCell == Cell.OPPONENT)
					continue;
				else if(tempCell == Cell.MINE)
					return true;
				else
					break;
			}
		}
		return false;
	}
}
