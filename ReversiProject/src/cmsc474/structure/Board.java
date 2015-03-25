package cmsc474.structure;

import cmsc474.boardEnum.Cell;

public class Board {
	Cell[][] board = new Cell[10][16];
	
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
					board[i][j] = Cell.getCell(array[i-1][j-wallLength]);
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
	
	/*
	 * Prints the board for testing purposes
	 */
	public void print(){		
		for(int i = 0; i<board.length; i++){
			for(int j = 0; j<board[i].length; j++){
				System.out.print(board[i][j].getVal()+" ");
			}
			System.out.println(" ");
		}
	}
}
