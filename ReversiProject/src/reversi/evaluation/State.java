package reversi.evaluation;
import reversi.move.Move;


public class State {
	public State previous;
	int score;
	public State[] children = new State[4];
	private Move move;
	private boolean player1;
	
	//Constructor
	public State(Move move){
		this.move = move;
	}
	public State getPrevious(){
		return previous;
	}
	public int getScore(){
		return score;
	}
	public Move getMove(){
		return move;
	}
	public State[] getChlildren(){
		return this.children;
	}
	public int trueScore(){
		//LEAF STATE
		if(children == null){
			return this.score;
		}
		//PLAYER 1
		if(player1 == true){
			int best = 0;
			for(int i = 0; i < 4; i++){
				int s;
				if(children[i] != null){
					s = children[i].trueScore();
				}else{
					s = 0;
				}
				if(s>best){
					best = s;
				}
			}
			return best;	
		}else{//PLAYER 2
			int best = Integer.MAX_VALUE;
			for(int i = 0; i < 4; i++){
				int s;
				if(children[i] != null){
					s = children[i].trueScore();
				}else{
					s = Integer.MAX_VALUE;
				}
				if(s<best){
					best = s;
				}
			}
			return best;	
		}
	}
	
}
