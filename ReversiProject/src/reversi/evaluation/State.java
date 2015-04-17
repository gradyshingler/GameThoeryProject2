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
	/*Constructor for the actual state of the game where we will give the state its move options as the form of
	State[] children*/
	public State(State[] children){
		this.children = children;
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
	public void trueScore(){
		//LEAF STATE
		if(children == null){
			//Not sure??? I need this position Score to be relative to player 1...
			this.score = this.move.positionScore;
		}
		//PLAYER 1 trying to maximize
		if(true){
			int best = 0;
			for(int i = 0; i < 4; i++){
				int s;
				if(children[i] != null){
					s = children[i].getScore();
				}else{
					s = 0;
				}
				if(s>best){
					best = s;
				}
			}
			this.score = best;	
		}else{//PLAYER 2 trying to minimize
			int best = Integer.MAX_VALUE;
			for(int i = 0; i < 4; i++){
				int s;
				if(children[i] != null){
					s = children[i].getScore();
				}else{
					s = Integer.MAX_VALUE;
				}
				if(s<best){
					best = s;
				}
			}
			this.score = best;	
		}
	}
	
}
