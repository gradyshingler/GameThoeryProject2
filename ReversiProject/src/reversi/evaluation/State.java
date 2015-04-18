package reversi.evaluation;
import reversi.move.Move;


public class State {
	public State previous;
	int score;
	public State[] children;
	private Move move;
	private boolean player1;
	public int choices;
	//Constructor
	public State(Move move, int choices){
		this.move = move;
		this.choices = choices;
		
	}
	/*Constructor for the actual state of the game where we will give the state its move options as the form of
	State[] children*/
	public State(int choices){
		//Do nothing;
		this.choices = choices;
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
			System.out.println("null children in trueScore");
			return;
		}
			
			int best = Integer.MIN_VALUE;
			for(int i = 0; i < this.choices; i++){
				int s;
				State child= children[i];
				if(child != null){
					s = (children[i].move.positionScore - children[i].move.consequenceScore)*-1;
				}else{
					continue;
				}
				if(s>best){
					best = s;
				}
			}
			this.move.consequenceScore = best;
			System.out.println(best );
	}
	
}
