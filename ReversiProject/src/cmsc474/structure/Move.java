package cmsc474.structure;

public class Move {
	int xPos;
	int yPos;
	int score;
//Andrew's better note
	public Move(int x, int y) {
		xPos = x;
		yPos = y;
		score = 0;
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
