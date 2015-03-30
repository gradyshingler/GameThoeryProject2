
public class Score {
	int[][] scores = new int[10][16];
	
	public Score(){
		setScoreTable();
		reversi.printMatrix(scores);
	}
	
	private void setScoreTable(){
		int rowLength = 8;
		int wallLength = 4;
		boolean top = true;
		for (int i = 0; i < 10; i++) {
			if (i == 0 || i == 9) {
				for (int j = 0; j < 16; j++) {
					scores[i][j] = 0;
	
				}
			} else {
				for (int j = 0; j < wallLength; j++) {
					scores[i][j] = 0;
				}
				for (int j = wallLength; j < rowLength + wallLength; j++) {
					//spaces where moves are possible
					if(i == 1 || i == 8){
						if(j == 4 || j == 11) scores[i][j] = 99;
						else if(j==5 || j==10) scores[i][j] = -20;
						else if(j==6 || j==9) scores[i][j] = 15;
						else if(j==7||j==8)	scores[i][j] = 10;
					}
					else if( i==2 || i==7 ){
						if(j==3 || j==12) scores[i][j] = -20;
						else if(j==4 || j==11) scores[i][j] = -25;
						else if(j==5 || j==10) scores[i][j] = -30;
						else if(j==6 || j==9) scores[i][j] = -10;
						else if(j==7 || j==8) scores[i][j] = -5;
					}
					else if(i==3 || i == 6){
						if(j==2 || j==13) scores[i][j] = -15;
						else if(j==3 || j==12) scores[i][j] = 15;
						else if(j==4 || j==11) scores[i][j] = 10;
						else if(j==5 || j==10) scores[i][j] = 12;
						else if(j==6 || j==9) scores[i][j] = 8;
						else if(j==7 || j==8) scores[i][j] = 7;
					}
					else if(i==4 || i==5){
						if(j==1 || j==14) scores[i][j] = 80;
						else if(j==2 || j==13) scores[i][j] = -30;
						else if(j==3 || j==12) scores[i][j] = 10;
						else if(j==4 || j==11) scores[i][j] = 9;
						else if(j==5 || j==10) scores[i][j] = 8;
						else if(j==6 || j==9) scores[i][j] = 7;
						else if(j==7 || j==8) scores[i][j] = 5;
					}
				}
				for (int j = (wallLength) + rowLength; j < 16; j++) {
					scores[i][j] = 0;
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