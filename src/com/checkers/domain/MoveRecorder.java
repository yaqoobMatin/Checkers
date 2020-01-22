package com.checkers.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to maintain a history of all moves played yet 
 *
 */
public class MoveRecorder {

	/**
	 * This list contains all the moves played yet
	 */
	private List<Move> moveHistory;
	
	/**
	 * Constructor
	 */
	public MoveRecorder(){
		moveHistory = new ArrayList<Move>();		
	}

	/**
	 * This method is used to record a move
	 * @param move
	 */
	public void recordMove(Move move){
		moveHistory.add(move);
	}
	
	//Getters and Setters
	public List<Move> getMoveHistory() {
		return moveHistory;
	}
	
	
	
}
