package cmsc474.project2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import cmsc474.structure.Board;

public class reversi {

	public static void main(String[] args) {
		Scanner sc;
		final boolean LOCAL_TEST = true;
		int[][] boardNums = new int[8][];
		Board myBoard = null;
		
		if(LOCAL_TEST){
			try {
				File file = new File("src/cmsc474/testFiles/testFile.readBoard");
				sc = new Scanner(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				sc = null;
				e.printStackTrace();
			}
		} else {
			sc = new Scanner(System.in);
		}
		
		boardNums[0] = new int[8];
		for(int i=0;i<8;i++){
			boardNums[0][i] = sc.nextInt();
		}
		boardNums[1] = new int[10];
		for(int i=0;i<10;i++){
			boardNums[1][i] = sc.nextInt();
		}
		boardNums[2] = new int[12];
		for(int i=0;i<12;i++){
			boardNums[2][i] = sc.nextInt();
		}
		boardNums[3] = new int[14];
		for(int i=0;i<14;i++){
			boardNums[3][i] = sc.nextInt();
		}
		boardNums[4] = new int[14];
		for(int i=0;i<14;i++){
			boardNums[4][i] = sc.nextInt();
		}
		boardNums[5] = new int[12];
		for(int i=0;i<12;i++){
			boardNums[5][i] = sc.nextInt();
		}
		boardNums[6] = new int[10];
		for(int i=0;i<10;i++){
			boardNums[6][i] = sc.nextInt();
		}
		boardNums[7] = new int[8];
		for(int i=0;i<8;i++){
			boardNums[7][i] = sc.nextInt();
		}
		
		myBoard = new Board(boardNums);
		myBoard.print();
	}
	
	private static void printMatrix(int[][] matrix){
		int rows = matrix.length;
		System.out.println("Rows:"+rows);
		int cols = matrix[0].length;
		System.out.println("Cols:"+cols);
		
		for(int i = 0; i< rows; i++){
			for(int j = 0; j<matrix[i].length; j++){
				System.out.print(matrix[i][j]+" ");
			}
			System.out.println(" ");
		}
	}

}
