package cmsc474.structure;

public class Disk {
	int xPos, yPos;
	Cell cell;
	boolean Stable;
	
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
		if(cell == 1){
			cell == 2;
		}
		else{
			cell == 1;
		}
	}
}