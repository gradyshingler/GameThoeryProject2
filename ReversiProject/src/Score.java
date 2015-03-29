

public class Score {
	int[][] board = new int[10][16];
	int rowLength = 8;
	int wallLength = 4;
	boolean top = true;
	for (int i = 0; i < 10; i++) {
		if (i == 0 || i == 9) {
			for (int j = 0; j < 16; j++) {
				board[i][j] = null;

			}
		} else {
			for (int j = 0; j < wallLength; j++) {
				board[i][j] = null;
			}
			for (int j = wallLength; j < rowLength + wallLength; j++) {
				//spaces where moves are possible
				if(i == 1){
					if(j == 4 || j == 11){
						board[i][j] == 99;
					}
					else{
						
					}
				}
				else if(i == 2){
					
				}
				else if(i == 3){
					
				}
				else if(i == 4){
					
				}
				else if(i == 5){
					
				}
				else if(i == 6){
					
				}
				else if(i == 7){
					
				}
				else if(i == 8){
					
				}
			}
			for (int j = (wallLength) + rowLength; j < 16; j++) {
				board[i][j] = null;
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