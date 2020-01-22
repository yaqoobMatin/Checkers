package com.checkers.graphics;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import com.checkers.domain.Cell;
import com.checkers.domain.ManualPlayer;
import com.checkers.domain.Move;
import com.checkers.domain.Piece;
import com.checkers.domain.PieceType;
import com.checkers.domain.Player;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class represents a GUI for cell of checkers board
 * 
 *
 */
public class CellGUI extends Button {

	/**
	 * This property represents corresponding board cell.
	 */
	Cell myBoardCell;

	/**
	 * BoardGUI object
	 */
	BoardGUI boardGUI;
		
	
	public CellGUI(BoardGUI boardGUI, int row, int col){
		myBoardCell = boardGUI.board.getCell(row, col);
		this.boardGUI = boardGUI;
		setPiece();
		setOnAction(new EventHandler<ActionEvent>() {			
			@Override
			public void handle(ActionEvent arg0) {	            
				if(myBoardCell.piece == null){
					if(boardGUI.board.isAnyCellSelected() && myBoardCell.isAValidDestination()){
						Player player = new ManualPlayer(boardGUI.board);
						Move move = findMove(boardGUI.board.selectedCell, myBoardCell);
						player.move(move);
						if(boardGUI.board.winner!=null){
							boardGUI.turnLabel.setText(boardGUI.board.winner+" won the game.");
							Alert a = new Alert(AlertType.NONE); 
							a.setContentText(boardGUI.board.winner+" won the game!"); 
							a.getDialogPane().getButtonTypes().add(ButtonType.OK);
							a.show(); 
						}else{
							String statusLabel = "";
							if(boardGUI.board.currentTurn == PieceType.BLACK){								
								statusLabel = "BLACK TURN";
								List<Move> moveList = boardGUI.board.findAllValidMoves(PieceType.BLACK);
								if(moveList!=null && !moveList.isEmpty()){
									if(moveList.get(0).isCapturingMove()){
										statusLabel = statusLabel + " [CAPTURE MOVE]";
									}
								}
							}else if(boardGUI.board.currentTurn == PieceType.WHITE){
								statusLabel="WHITE TURN";															
								List<Move> moveList = boardGUI.board.findAllValidMoves(PieceType.WHITE);
								if(moveList!=null && !moveList.isEmpty()){
									if(moveList.get(0).isCapturingMove()){
										statusLabel = statusLabel + " [CAPTURE MOVE]";
									}
								}
							}
							if(!boardGUI.board.moveRecorder.getMoveHistory().isEmpty()){
								statusLabel=statusLabel+ "\n Last Move: "+boardGUI.board.moveRecorder.getMoveHistory().get(boardGUI.board.moveRecorder.getMoveHistory().size()-1);
							}
							boardGUI.turnLabel.setText(statusLabel);
						}
						boardGUI.clearHighlighting();
					}
				}else{
					if(myBoardCell.piece != null && myBoardCell.piece.pieceType == boardGUI.board.currentTurn){
						highlightAsSelected();
					}
				}
			}
		});
	}
	
	private Move findMove(Cell from, Cell to){
		List<Move> validMoves =  boardGUI.board.findValidMoves(from);
		for (Move currentMove : validMoves) {
			if(currentMove.getTo() == to){
				return currentMove;
			}
		}
		return null;
	}
	
	/**
	 * Change the cell graphics to show the piece passed here as argument
	 * 
	 * @param piece
	 */
	public void setPiece() {
		Piece piece = myBoardCell.piece;
		if (piece == null) {
			return;
		}
		try {
			String fileName = "resources/images/WhitePiece-King.png";
			if (piece.pieceType == PieceType.BLACK) {
				if (piece.isCrowned) {
					fileName = "resources/images/BlackPiece-King.png";
				} else {
					fileName = "resources/images/BlackPiece.png";
				}
			} else {
				if (piece.isCrowned) {
					fileName = "resources/images/WhitePiece-King.png";
				} else {
					fileName = "resources/images/WhitePiece.png";
				}
			}
			FileInputStream input = new FileInputStream(fileName);
			Image image = new Image(input);
			ImageView imageView = new ImageView(image);
			setGraphic(imageView);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * This method highlights the current cell like it is selected by user
	 */
	public void highlightAsSelected() {
		String fileName = "resources/images/WhitePiece-Highlighted.png";
		if(myBoardCell.piece==null){
			return;
		}
		boardGUI.clearHighlighting();
		myBoardCell.setSelected(true);
		boardGUI.board.selectedCell = myBoardCell;
		if(myBoardCell.piece.pieceType==PieceType.WHITE){
			if(!myBoardCell.piece.isCrowned){
				fileName = "resources/images/WhitePiece-Highlighted.png";					
			}else{
				fileName = "resources/images/WhitePiece-King-Highlighted.png";					
			}
		}else{
			if(!myBoardCell.piece.isCrowned){
				fileName = "resources/images/BlackPiece-Highlighted.png";					
			}else{
				fileName = "resources/images/BlackPiece-King-Highlighted.png";					
			}				
		}
		
		try {
			setStyle("-fx-background-color: #7591ff; -fx-border-color: #7591ff");
			FileInputStream input = new FileInputStream(fileName);
			Image image = new Image(input);
			ImageView imageView = new ImageView(image);
			setGraphic(imageView);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		
		List<Cell> validDestinations =  boardGUI.board.findValidDestinations(myBoardCell);
		for (Cell cell : validDestinations) {
			boardGUI.cells[cell.getMyRow()][cell.getMyCol()].highlightAsDestination();
		}
	}

	/**
	 * This method highlight the cell like this can be used as a destinationCell
	 */
	public void highlightAsDestination() {
		myBoardCell.setAValidDestination(true);
		setStyle("-fx-background-color: #757575; -fx-border-color: #757575");
	}
	
	/**
	 * This method is used to clear highlighting of the cell
	 */
	public void clearHighlighting(){
		int i = myBoardCell.getMyRow();
		int j = myBoardCell.getMyCol();
		if ((i + j) % 2 == 0) {
			setStyle("-fx-background-color: #ffffff; -fx-border-color: #000000");
		} else {
			setStyle("-fx-background-color: #000000; -fx-border-color: #000000");
		}		
		setGraphic(null);
		setPiece();
		myBoardCell.setSelected(false);
		myBoardCell.setAValidDestination(false);
		boardGUI.board.selectedCell = null;
	}
	
}
