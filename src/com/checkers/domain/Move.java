package com.checkers.domain;

/**
 * This class represents a single move during the game
 *  
 *
 */
public class Move {

	/**
	 * The cell from where player is moving
	 */
	Cell from;
	
	/**
	 * The cell to where player is moving
	 */
	Cell to;
	
	/**
	 * This property will tell if the move is a capturing move
	 */
	boolean isCapturingMove;
	
	/**
	 * This property will contain the cell which has been captured if this move is a capturing move else it will contain null.
	 */
	Cell capturedCell;
	
	/**
	 * This property will be used only for maintaining history.
	 */
	PieceType whoPlayed;
	

	/**
	 * Constructor
	 * @param from
	 * @param to
	 * @param isCapturingMove
	 * @param capturedCell
	 */
	public Move(Cell from, Cell to, boolean isCapturingMove, Cell capturedCell) {
		super();
		this.from = from;
		this.to = to;
		this.isCapturingMove = isCapturingMove;
		this.capturedCell = capturedCell;
	}

	/**
	 * Constructor
	 * @param from
	 * @param to
	 */
	public Move(Cell from, Cell to) {
		super();
		this.from = from;
		this.to = to;
	}

	//Getters and Setters
	public Cell getFrom() {
		return from;
	}

	public void setFrom(Cell from) {
		this.from = from;
	}

	public Cell getTo() {
		return to;
	}

	public void setTo(Cell to) {
		this.to = to;
	}

	public boolean isCapturingMove() {
		return isCapturingMove;
	}

	public void setCapturingMove(boolean isCapturingMove) {
		this.isCapturingMove = isCapturingMove;
	}

	public Cell getCapturedCell() {
		return capturedCell;
	}

	public void setCapturedCell(Cell capturedCell) {
		this.capturedCell = capturedCell;
	}
	
	@Override
	public String toString(){
		String result=  whoPlayed+": "+ from.getMyRow()+""+from.getMyCol()+" -> "+to.getMyRow()+""+to.getMyCol();
		if(isCapturingMove){
			result  = result + " : CAPTURED A PIECE";
		}
		return result;
	}
	
	
}
