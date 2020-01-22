package com.checkers.domain;

/**
 * Each square of the board is represented by this class
 *   
 *
 */
public class Cell {

	/**
	 * The piece currently on cell. If the cell is empty then this property will be null.
	 */
	public Piece piece;
	
	/**
	 * The row index of board at which this cell is present
	 */
	int myRow;

	/**
	 * The columns index of board at which this cell is present
	 */
	int myCol;

	/**
	 * This property tells us if the current cell is selected in order to move
	 */
	boolean isSelected;
	
	/**
	 * This property informs us if the current cell is a valid destination
	 */
	boolean isAValidDestination;
	
	/**
	 * Constructor
	 * @param myRow
	 * @param myCol
	 */
	public Cell(int myRow, int myCol) {
		this.myRow = myRow;
		this.myCol = myCol;
	}

	public int getMyRow() {
		return myRow;
	}

	public void setMyRow(int myRow) {
		this.myRow = myRow;
	}

	public int getMyCol() {
		return myCol;
	}

	public void setMyCol(int myCol) {
		this.myCol = myCol;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean isAValidDestination() {
		return isAValidDestination;
	}

	public void setAValidDestination(boolean isAValidDestination) {
		this.isAValidDestination = isAValidDestination;
	}
		
	//Getters and Setters
	
	
}
