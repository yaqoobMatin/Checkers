package com.checkers.domain;

/**
 * This class represents a piece 
 * @author 
 *
 */
public class Piece {
	
	/**
	 * The type of piece, it may be BLACK or WHITE
	 */
	public PieceType pieceType;

	/**
	 * This property tells if a piece has been crowned or not
	 */
	public boolean isCrowned;
	
	public Piece(PieceType pieceType) {
		this.pieceType = pieceType;
	}
	
	
	
}
