//default package

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
	public String toString(){
		return "("+xPos+","+yPos+"):"+cell.getVal();
	}
	public void flip(){
		if(cell == Cell.MINE) cell = Cell.OPPONENT;
		else if(cell == Cell.OPPONENT) cell = Cell.MINE;
	}
	public void equals(Disk o){
		if(this.getxPos() == o.getxPos() && this.getyPos() == o.getyPos() &&
				this.getCell() == o.getCell()){
			return true;
		}
		return false;
	}
}