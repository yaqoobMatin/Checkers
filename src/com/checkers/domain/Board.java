package com.checkers.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents the current state of game
 *   
 *
 */
public class Board {

	/**
	 * Two dimensional array of cells which makes a board
	 **/
	Cell[][] cells = new Cell[8][8];
	
	/**
	 * This property represents the size of the board
	 */
	public static int SIZE = 8;
	
	/**
	 * This property tells us which piece has current turn
	 */
	public PieceType currentTurn;
	
	/**
	 * This property tells us which cell is currently selected if there is any. Otherwise it is null
	 */
	public Cell selectedCell;
	
	/**
	 * This property tells the winner of the game. In the case the game is still going on, it will be null
	 */
	public PieceType winner;
	
	/**
	 * In the case of multiple move scenario, this property contains the source cell. In all other cases, it will remain null.
	 */
	public Cell multipleMoveSource;
	
	/**
	 * MoveRecorder
	 */
	public MoveRecorder moveRecorder;
	
	/*
	 * This property tells if computer is going to play from one side or not.
	 */
	public boolean isAIOn;
	
	/**
	 * This property tells us the colour of AI player.
	 */
	public PieceType aiPlayer = PieceType.WHITE;;
	
	/**
	 * This property represents the current game level
	 */
	public int gameLevel = 1;
	
	/**
	 * Constructor
	 */
	public Board(){
		initialize();
	}
	
	/**
	 * This method is used to initialize the game to initial state
	 */
	public void initialize(){
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE;j++){
				cells[i][j] = new Cell(i,j);
			}
		}
		
		cells[0][1].piece =  new Piece(PieceType.WHITE);
		cells[0][3].piece =  new Piece(PieceType.WHITE);
		cells[0][5].piece =  new Piece(PieceType.WHITE);
		cells[0][7].piece =  new Piece(PieceType.WHITE);
		cells[1][0].piece =  new Piece(PieceType.WHITE);
		cells[1][2].piece =  new Piece(PieceType.WHITE);
		cells[1][4].piece =  new Piece(PieceType.WHITE);
		cells[1][6].piece =  new Piece(PieceType.WHITE);
		cells[2][1].piece =  new Piece(PieceType.WHITE);
		cells[2][3].piece =  new Piece(PieceType.WHITE);
		cells[2][5].piece =  new Piece(PieceType.WHITE);
		cells[2][7].piece =  new Piece(PieceType.WHITE);

		cells[5][0].piece =  new Piece(PieceType.BLACK);
		cells[5][2].piece =  new Piece(PieceType.BLACK);
		cells[5][4].piece =  new Piece(PieceType.BLACK);
		cells[5][6].piece =  new Piece(PieceType.BLACK);
		cells[6][1].piece =  new Piece(PieceType.BLACK);
		cells[6][3].piece =  new Piece(PieceType.BLACK);
		cells[6][5].piece =  new Piece(PieceType.BLACK);
		cells[6][7].piece =  new Piece(PieceType.BLACK);
		cells[7][0].piece =  new Piece(PieceType.BLACK);
		cells[7][2].piece =  new Piece(PieceType.BLACK);
		cells[7][4].piece =  new Piece(PieceType.BLACK);
		cells[7][6].piece =  new Piece(PieceType.BLACK);		
		
		currentTurn = PieceType.BLACK;
		selectedCell = null;
		winner = null;
		multipleMoveSource = null;
		moveRecorder = new MoveRecorder();
	}
	
    /** Initializes a copy of MODEL. */
    Board(Board model) {
        copy(model);
    }	
	
    /** Copies MODEL into me. */
    void copy(Board model) {
    	initialize();
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE;j++){
				if(model.cells[i][j].piece!=null){
					cells[i][j].piece = new Piece(model.cells[i][j].piece.pieceType);
					if(model.cells[i][j].piece.isCrowned){
						cells[i][j].piece.isCrowned = true;
					}else{
						cells[i][j].piece.isCrowned = false;						
					}
				}else{
					cells[i][j].piece = null;
				}
			}
		}    	
		currentTurn = model.currentTurn;
		selectedCell = getCell(model.selectedCell.myRow, model.selectedCell.myCol);
		winner = model.winner;
		if(model.multipleMoveSource!=null){
			multipleMoveSource = getCell(model.multipleMoveSource.myRow, model.multipleMoveSource.myCol);
		}else{
			multipleMoveSource = null;
		}
		gameLevel = model.gameLevel;
    }

	/**
	 * This method is used to get a cell at specific row and column.
	 * @param row
	 * @param col
	 * @return
	 */
	public Cell getCell(int row, int col){
		return cells[row][col];
	}
	
	/**
	 * This function is used to find all cells where pieces of certain type (BLACK or WHITE) are present.
	 * @param pieceType
	 * @return
	 */
	public List<Cell> findPieces(PieceType pieceType){
		List<Cell> result = new ArrayList<Cell>();
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE;j++){
				if(cells[i][j].piece!=null && cells[i][j].piece.pieceType==pieceType){
					result.add(cells[i][j]);
				}
			}
		}
		return result;
	}

	
	/**
	 * This method is used to find the valid moves from a given cell
	 * @param fromCell
	 * @return
	 */
	public List<Move> findValidMoves(Cell fromCell){
		List<Move> validMoves = new ArrayList<Move>();
		if(multipleMoveSource!=null && multipleMoveSource!=fromCell){
			return validMoves;
		}
		
		boolean isAnyCapturingMove = false;
		if(fromCell.piece!=null){
			
			if(fromCell.piece.pieceType == PieceType.BLACK || fromCell.piece.isCrowned){				
				if(fromCell.myRow>0 && fromCell.myCol>0){
					Cell to1 = getCell(fromCell.myRow-1, fromCell.myCol-1);
					if(to1.piece == null){
						validMoves.add(new Move(fromCell, to1));
					}else{
						if(to1.piece.pieceType != fromCell.piece.pieceType){
							//check for capture move
							if(fromCell.myRow>1 && fromCell.myCol>1){
								Cell nextCell = getCell(fromCell.myRow-2, fromCell.myCol-2);
								if(nextCell.piece==null){
									isAnyCapturingMove = true;
									validMoves.add(new Move(fromCell, nextCell, true, to1));
								}
							}
						}						
					}
				}

				if(fromCell.myRow>0 && fromCell.myCol<Board.SIZE-1){
					Cell to1 = getCell(fromCell.myRow-1, fromCell.myCol+1);								
					if(to1.piece == null){
						validMoves.add(new Move(fromCell, to1));
					}else{
						if(to1.piece.pieceType != fromCell.piece.pieceType){
							//check for capture move
							if(fromCell.myRow>1 && fromCell.myCol<Board.SIZE-2){
								Cell nextCell = getCell(fromCell.myRow-2, fromCell.myCol+2);
								if(nextCell.piece==null){
									isAnyCapturingMove = true;									
									validMoves.add(new Move(fromCell, nextCell, true, to1));
								}
							}
						}						
					}					
				}				
			}


			if(fromCell.piece.pieceType == PieceType.WHITE || fromCell.piece.isCrowned){				
				if(fromCell.myRow<Board.SIZE-1 && fromCell.myCol<Board.SIZE-1){
					Cell to1 = getCell(fromCell.myRow+1, fromCell.myCol+1);
					if(to1.piece == null){
						validMoves.add(new Move(fromCell, to1));
					}else{
						if(to1.piece.pieceType != fromCell.piece.pieceType){
							//check for capture move
							if(fromCell.myRow<Board.SIZE-2 && fromCell.myCol<Board.SIZE-2){
								Cell nextCell = getCell(fromCell.myRow+2, fromCell.myCol+2);
								if(nextCell.piece==null){
									isAnyCapturingMove = true;									
									validMoves.add(new Move(fromCell, nextCell, true, to1));
								}
							}
						}						
					}
				}

				if(fromCell.myRow<Board.SIZE-1 && fromCell.myCol>0){
					Cell to1 = getCell(fromCell.myRow+1, fromCell.myCol-1);								
					if(to1.piece == null){
						validMoves.add(new Move(fromCell, to1));
					}else{
						if(to1.piece.pieceType != fromCell.piece.pieceType){
							//check for capture move
							if(fromCell.myRow<Board.SIZE-2 && fromCell.myCol>1){
								Cell nextCell = getCell(fromCell.myRow+2, fromCell.myCol-2);
								if(nextCell.piece==null){
									isAnyCapturingMove = true;									
									validMoves.add(new Move(fromCell, nextCell, true, to1));
								}
							}
						}						
					}					
				}				
			}			
			
			
			if(isAnyCapturingMove){
				Iterator<Move> iterator = validMoves.iterator();
				while(iterator.hasNext()){
					Move move = iterator.next();
					if(!move.isCapturingMove){
						iterator.remove();
					}
				}
			}
			
			
		}
		return validMoves;
	}
	
	/**
	 * This method is used to find valid destinations which can be reached from fromCell.
	 * @param fromCell
	 * @return
	 */
	public List<Cell> findValidDestinations(Cell fromCell){
		List<Cell> validDestinations = new ArrayList<Cell>();		
		if(multipleMoveSource!=null && multipleMoveSource!=fromCell){
			return validDestinations;
		}
		List<Move> validMoves = findAllValidMoves(fromCell.piece.pieceType);
		for (Move move : validMoves) {
			if(move.from == fromCell){
				validDestinations.add(move.to);
			}
		}
		return validDestinations;
	}
	
	/**
	 * This function is used to find all the valid for a certain piece type (i.e. BLACK or WHITE)
	 * @param pieceType
	 * @return
	 */
	public List<Move> findAllValidMoves(PieceType pieceType){
		List<Move> validMoves = new ArrayList<Move>();
		List<Cell> cells = findPieces(pieceType);
		for (Cell cell : cells) {
			validMoves.addAll(findValidMoves(cell));
		}
		boolean isAnyCapturingMove = false;
		for (Move move : validMoves) {
			if(move.isCapturingMove){
				isAnyCapturingMove = true;
				break;
			}
		}
		if(isAnyCapturingMove){
			Iterator<Move> iterator = validMoves.iterator();
			while(iterator.hasNext()){
				Move move = iterator.next();
				if(!move.isCapturingMove){
					iterator.remove();
				}
			}
		}
		
		return validMoves;
	}	

	/**
	 * This method tells if any of the cells on board is selected or not
	 * @return
	 */
	public boolean isAnyCellSelected(){
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE;j++){
				if(cells[i][j].isSelected){
					selectedCell = cells[i][j];
					return true;
				}
			}			
		}
		return false;
	}
	
	/**
	 * This method returns opponents piece type
	 * @param pieceType
	 * @return
	 */
	public PieceType opponent(PieceType pieceType){
		if(pieceType == PieceType.BLACK){
			return PieceType.WHITE;
		}
		return PieceType.BLACK;
	}
	
	@Override
	public String toString(){
		String s = "";
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE;j++){
				if(cells[i][j].piece == null){
					s = s + "_ ";					
				}
				else if(cells[i][j].piece.pieceType == PieceType.BLACK){
					s = s + "B ";
				}else{
					s = s + "W ";					
				}
			}
			s = s+ "\n";
		}
		return s;
	}
	
	
	
}
