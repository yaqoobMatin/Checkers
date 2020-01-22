package com.checkers.launcher;

import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import com.checkers.domain.Board;
import com.checkers.domain.Move;
import com.checkers.graphics.BoardGUI;

public class Launcher extends Application {

	/**
	 * Drop down which contains the various level options
	 */
	private ChoiceBox<String> levelsBox;

	/**
	 * Drop down which contains the various modes of game
	 */
	private ChoiceBox<String> modeBox;

	private BorderPane root;

	/**
	 * Board object
	 */
	Board board;

	BoardGUI boardGUI;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void init() throws Exception {
		board = new Board();
		board.isAIOn = true;
		boardGUI = new BoardGUI(board);
System.out.println(board);
		levelsBox = new ChoiceBox<String>();
		modeBox = new ChoiceBox<String>();
		ToolBar optionsBar = createOptionsBar();
		root = new BorderPane();
		root.setTop(optionsBar);
		root.setCenter(boardGUI);
		root.setBottom(boardGUI.statusBar);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Scene scene = new Scene(root, 390, 470, Color.LIGHTPINK);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Checkers");
		primaryStage.setResizable(false);
		primaryStage.show();

	}

	public ToolBar createOptionsBar(){

		Pane menuBarFiller1 = new Pane();
		Pane menuBarFiller2 = new Pane();
		HBox.setHgrow( menuBarFiller1, Priority.ALWAYS);
		HBox.setHgrow( menuBarFiller2, Priority.ALWAYS);

		levelsBox = new ChoiceBox<>();
		levelsBox.getItems().add("Level 1 (Easy)");
		levelsBox.getItems().add("Level 2");
		levelsBox.getItems().add("Level 3");
		levelsBox.getItems().add("Level 4");
		levelsBox.getItems().add("Level 5");
		levelsBox.getItems().add("Level 6");
		levelsBox.getItems().add("Level 7 (Hard)");

		levelsBox.setValue("Level 1 (Easy)");
		levelsBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
		      @Override
		      public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
		    	String s = levelsBox.getItems().get((Integer) number2);
		    	int level = Integer.parseInt(s.charAt(6)+"");
		    	try {
					board.initialize();					
					board.gameLevel = level;
					boardGUI.initialize();
				} catch (Exception e) {
					e.printStackTrace();
				}
		      }			
		});
		
		modeBox.getItems().add("Single Player");
		modeBox.getItems().add("Two Player");
		modeBox.setValue("Single Player");
		modeBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
		      @Override
		      public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
			    	String s = modeBox.getItems().get((Integer) number2);
			    	if(s.equalsIgnoreCase("Single Player")){
						board.initialize();					
						board.isAIOn = true;
						boardGUI.initialize();
						levelsBox.setDisable(false);
			    	}else{			    		
						board.initialize();					
						board.isAIOn = false;
						boardGUI.initialize();			    		
						levelsBox.setDisable(true);						
			    	}
		      }
		});
		
		Button historyBtn = new Button("History");
		historyBtn.setOnAction(new EventHandler<ActionEvent>() {			
			@Override
			public void handle(ActionEvent arg0) {
		        TextArea textArea = new TextArea();
		        
		        List<Move> moves = board.moveRecorder.getMoveHistory();
		        String str="Moves are in format: \n<From RowNum><From ColumnNumber> -> <TO RowNum><To ColumnNumber>\n"
		        		+ "Row and columns numbers are started from 0 to 7 from left top corner of board.\n";
		        int i=1;
		        for (Move move : moves) {
		        	str=str+i+". "+move+"\n";
		        	i++;
				}
				textArea.setText(str);
		        VBox vbox = new VBox(textArea);				
				Scene secondScene = new Scene(vbox, 500, 200);
	            Stage newWindow = new Stage();
	            newWindow.setTitle("Moves History");
	            newWindow.setScene(secondScene);
	            newWindow.show();				
			}
		});

		Button helpBtn = new Button("Help");
		helpBtn.setOnAction(new EventHandler<ActionEvent>() {			
			@Override
			public void handle(ActionEvent arg0) {
		        Label textArea = new Label();
		        String str="In Checkers, Black moves first and play proceeds alternately. From their initial positions,\n"
		        		+ " checkers may only move forward. There are two types of moves that can be made, \ncapturing moves and "
		        		+ "non-capturing moves. Non-capturing moves are simply a diagonal \nmove forward from one square to an adjacent square. "
		        		+ "(Note that the white squares are \nnever used.) Capturing moves occur when a player jumps an opposing piece. "
		        				+ "This is also \ndone on the diagonal and can only happen when the square behind (on the same diagonal)\n is also open. "
		        				+ "This means that you may not jump an opposing piece around a corner. \nOn a capturing move, a piece may make multiple "
		        				+ "jumps. If after a jump a player is in a \nposition to make another jump then he may do so. This means that a player may "
		        				+ "make \nseveral jumps in succession, capturing several pieces on a single turn.\nWhen a player is in a position to make "
		        				+ "a capturing move, he must make a capturing move. When he has more than one capturing \nmove to choose from he may take "
		        				+ "whichever move suits him.";
				textArea.setText(str);
		        VBox vbox = new VBox(textArea);				
				Scene secondScene = new Scene(vbox, 500, 220);
	            Stage newWindow = new Stage();
	            newWindow.setTitle("Help");
	            newWindow.setScene(secondScene);
	            newWindow.show();				
			}
		});
		
		return new ToolBar(
				new Label( "Level"), levelsBox, historyBtn, helpBtn, new Label( "Mode"), modeBox);
	}

}
