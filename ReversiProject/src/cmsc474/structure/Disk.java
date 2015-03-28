package cmsc474.structure;


//GRady's note::: THIS SHOULD BE NEW

public class Disk {
	int xPos, yPos;
	Cell cell;
	boolean Stable;
	
	public Disk(int x, int y, Cell type){
		xPos = x;
		yPos = y;
		cell = type;
	}
	
	public int getxPos() {
		return xPos;
	}
	public void setxPos(int xPos) {
		this.xPos = xPos;
	}
	public int getyPos() {
		return yPos;
	}
	public void setyPos(int yPos) {
		this.yPos = yPos;
	}
	public Cell getCell() {
		return cell;
	}
	public void setCell(Cell cell) {
		this.cell = cell;
	}
	public boolean isStable() {
		return Stable;
	}
	public void setStable(boolean stable) {
		Stable = stable;
	}
	public void flip(){
		//(TODO) - switch black to white and white to black
	}
}