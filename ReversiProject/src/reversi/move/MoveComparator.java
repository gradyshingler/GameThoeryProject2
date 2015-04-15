package reversi.move;
import java.util.Comparator;

public class MoveComparator implements Comparator<Move> {

	public int compare(final Move one, final Move two) {	
		if(one.positionScore < two.positionScore) return 1;
		else if(one.positionScore > two.positionScore) return -1;
		else {
			if(one.row < two.row) return 1;
			else if(one.row > two.row) return -1;
			else {
				if(one.col < two.col) return 1;
				else if(one.col > two.col) return -1;
				else return 0;
			}
		}
	}
}