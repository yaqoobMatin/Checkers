package com.checkers.graphics;

import com.checkers.domain.Board;

import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;

/**
 * This class represents GUI for checkers Board
 *
 */
public class BoardGUI extends GridPane {

	CellGUI[][] cells = new CellGUI[Board.SIZE][Board.SIZE];
	
	Board board;
	
	public ToolBar statusBar;
	Label turnLabel;
	
	/**
	 * Constructor
	 */
	public BoardGUI(Board board) {
		this.board = board;
		initialize();
	}

	public void initialize(){
		turnLabel = new Label("BLACK TURN");
		statusBar = new ToolBar(turnLabel);		
		for (int i = 0; i < Board.SIZE; i++) {
			for (int j = 0; j < Board.SIZE; j++) {
				cells[i][j] = new CellGUI(this, i, j);
				cells[i][j].setMinHeight(400 / 8);
				cells[i][j].setMinWidth(400 / 8);
				cells[i][j].setMaxHeight(400 / 8);
				cells[i][j].setMaxHeight(400 / 8);
				if ((i + j) % 2 == 0) {
					cells[i][j]
							.setStyle("-fx-background-color: #ffffff; -fx-border-color: #000000");
				} else {
					cells[i][j]
							.setStyle("-fx-background-color: #000000; -fx-border-color: #000000");
				}
				this.add(cells[i][j], j, i);
			}
		}
		cells[5][4].highlightAsSelected();
		setGridLinesVisible(true);		
	}
	
	/**
	 * This method is used to clear the highlighting
	 */
	public void clearHighlighting(){
		for (int i = 0; i < Board.SIZE; i++) {
			for (int j = 0; j < Board.SIZE; j++) {
				cells[i][j].clearHighlighting();
			}
		}		
	}
	
	
	

}
