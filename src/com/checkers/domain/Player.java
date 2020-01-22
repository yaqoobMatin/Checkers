package com.checkers.domain;

import java.util.List;

/**
 * This class represents a Player playing the game
 * 
 *
 */
public abstract class Player {

	/**
	 * The board on which game is going on
	 */
	Board board;

	/**
	 * Constructor
	 * 
	 * @param board
	 */
	public Player(Board board) {
		this.board = board;
	}

	/**
	 * This method is used to move a piece from one cell to another.
	 * 
	 * @param move
	 */
	public void move(Move move) {
		move.whoPlayed = move.from.piece.pieceType;
		//Code to crown a piece as KING
		boolean crownAsKing = false;
		if (move.from.piece.pieceType == PieceType.BLACK) {
			if (move.to.getMyRow() == 0) {
				crownAsKing = true;
			}
		} else {
			if (move.to.getMyRow() == Board.SIZE - 1) {
				crownAsKing = true;
			}
		}

		Piece piece = move.from.piece;
		move.from.piece = move.to.piece;
		move.to.piece = piece;
		if (crownAsKing) {
			move.to.piece.isCrowned = true;
		}
		if (board.currentTurn == PieceType.BLACK) {
			board.currentTurn = PieceType.WHITE;
		} else {
			board.currentTurn = PieceType.BLACK;
		}
		// Regicide (If a normal piece manages to capture a king, it is itself
		// upgraded to a king)
		if (move.isCapturingMove) {
			if (move.capturedCell.piece != null
					&& move.capturedCell.piece.isCrowned) {
				move.to.piece.isCrowned = true;
			}
			move.capturedCell.piece = null;
		}
		if (board.multipleMoveSource != null) {
			board.multipleMoveSource = null;
		}

		PieceType winner = checkWin();

		if (winner != null) {
			board.currentTurn = null;
			board.winner = winner;
		} else {
			// Code for checking multiple moves
			if (move.isCapturingMove()) {
				List<Move> validNewMoves = board.findValidMoves(move.getTo());
				boolean isAnyCapture = false;
				for (Move m : validNewMoves) {
					if (m.isCapturingMove()) {
						isAnyCapture = true;
						break;
					}
				}
				if (isAnyCapture) {
					board.currentTurn = move.getTo().piece.pieceType;
					board.multipleMoveSource = move.getTo();
				}
			}
		}

		board.moveRecorder.recordMove(move);
		if (board.isAIOn && board.currentTurn == board.aiPlayer) {
			AIPlayer aiPlayer = new AIPlayer(board);
			Move nextMove = aiPlayer.findMove();
			if (nextMove != null) {
				Move newMove = new Move(board.getCell(nextMove.from.getMyRow(),
						nextMove.from.getMyCol()), board.getCell(
						nextMove.to.getMyRow(), nextMove.to.getMyCol()));
				newMove.isCapturingMove = nextMove.isCapturingMove;
				if (nextMove.capturedCell != null) {
					newMove.capturedCell = board.getCell(
							nextMove.capturedCell.getMyRow(),
							nextMove.capturedCell.getMyCol());
				}
				move(newMove);
			}
			// System.out.println(board);
		}
	}

	/**
	 * This function checks if any player has won the game.
	 */
	public PieceType checkWin() {
		List<Move> validMoves = board.findAllValidMoves(board.currentTurn);
		if (validMoves == null || validMoves.isEmpty()) {
			return board.opponent(board.currentTurn);
		}
		return null;
	}

	/**
	 * This method is used to move a piece. Argument for this method is in form
	 * of a string such as "52 43".
	 * 
	 * @param moveString
	 */
	public void move(String moveString) {
		Cell fromCell = board.getCell(
				Integer.parseInt(moveString.charAt(0) + ""),
				Integer.parseInt(moveString.charAt(1) + ""));
		Cell toCell = board.getCell(
				Integer.parseInt(moveString.charAt(3) + ""),
				Integer.parseInt(moveString.charAt(4) + ""));
		move(new Move(fromCell, toCell));
	}

}
