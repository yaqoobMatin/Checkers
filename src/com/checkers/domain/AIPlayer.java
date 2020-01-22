package com.checkers.domain;

import java.util.List;
import java.util.Random;




/**
 * This class represents AI player playing the game
 *   
 *
 */
public class AIPlayer extends Player{

	
	/**
	 * Constructor
	 * @param board
	 */
	public AIPlayer(Board board) {
		super(board);
		myPieceType = board.aiPlayer;
		depth = board.gameLevel;
	}

	/**
	 * Heuristically determined maximum search depth
     *  based on characteristics of BOARD. *
	 */
	public int depth;

	private PieceType myPieceType = PieceType.WHITE;

    /** The move found by the last call to one of the ...FindMove methods
     *  below. */
    private Move _lastFoundMove;
	
    /** My pseudo-random number generator. */
    private Random _randGen = new Random();
    
    
    /** Return a move for me from the current position, assuming there
     *  is a move. */
    public Move findMove() {
        Board b = new Board(board);
        _lastFoundMove = null;        
        successor(b, depth, true, 1, Integer.MAX_VALUE, Integer.MIN_VALUE);
        if(_lastFoundMove == null){
          List<Move> legalMoves = b.findAllValidMoves(myPieceType);    
          if(legalMoves!=null && !legalMoves.isEmpty()){
          	int randmomIndex = randInt(legalMoves.size());
          	_lastFoundMove = legalMoves.get(randmomIndex);
          }        
        }	
        return _lastFoundMove;
    }
    
    /** Find a move from position BOARD and return its value, recording
     *  the move found in _lastFoundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _lastMoveFound. */
    private int successor(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {
    	if(depth==0){    		
    		return staticScore(board);
    	}
        List<Move> legalMoves = board.findAllValidMoves(board.currentTurn);          
    	Move bestMove = null;
        for (Move move : legalMoves) {
    		Board tempBoard = new Board(board);
    		Player player = new ManualPlayer(tempBoard);
    		Move tempBoardMove = new Move(tempBoard.getCell(move.from.getMyRow(), move.from.getMyCol()),tempBoard.getCell(move.to.myRow, move.to.myCol));
    		tempBoard.isAIOn = false;
    		player.move(tempBoardMove);
    		tempBoard.isAIOn = true;
    		int staticScore = staticScore(tempBoard);  
    		if(sense == 1){
    			if(tempBoard.currentTurn == myPieceType){
    				sense = 1;
    			}else{
    				sense=-1;
    			}
    			int currentScore = successor(tempBoard, depth-1, false, sense, alpha, staticScore>beta?staticScore:beta);    			
    			if(currentScore > beta){
    				beta = currentScore;
    				bestMove = move;
    			}
    		}
    		if(sense == -1){
    			if(tempBoard.currentTurn == myPieceType){
    				sense = 1;
    			}else{
    				sense=-1;
    			}
    			int currentScore = successor(tempBoard, depth-1, false, sense==1?-1:1, staticScore < alpha? staticScore:alpha, beta);    			    			
    			if(currentScore < alpha){
    				alpha = currentScore;
    				bestMove = move;
    			}
    		}    		
		}
    	if(saveMove){
    		if(bestMove!=null){
    			_lastFoundMove = bestMove;
    		}
    	}
    	if(sense==1){
    		return beta;
    	}else{
    		return alpha;
    	}        
    }
    
    
    /** Return a random integer in the range 0 inclusive to U, exclusive.
     *  Available for use by AIs that use random selections in some cases.
     *  Once setRandomSeed is called with a particular value, this method
     *  will always return the same sequence of values. */
    int randInt(int U) {
        return _randGen.nextInt(U);
    }
    
    /** Re-seed the pseudo-random number generator that supplies randInt
     *  with the value SEED. Identical seeds produce identical sequences.
     *  Initially, the PRNG is randomly seeded. */
    void setSeed(long seed) {
        _randGen.setSeed(seed);
    }
    
    /** Return a heuristic value for BOARD. */
    private int staticScore(Board board) {
    	int noOfMyPieces=0, noOfOpponentPieces=0;    	
		for(int i=0;i<Board.SIZE;i++){
			for(int j=0;j<Board.SIZE;j++){
				if(board.cells[i][j].piece!=null && board.cells[i][j].piece.pieceType==myPieceType){
					noOfMyPieces++;
					if(board.cells[i][j].piece.isCrowned){
						noOfMyPieces++;
					}
				}else if(board.cells[i][j].piece!=null && board.cells[i][j].piece.pieceType==board.opponent(myPieceType)){
					noOfOpponentPieces++;
					if(board.cells[i][j].piece.isCrowned){
						noOfOpponentPieces++;
					}					
				}
			}
		}
    	if(board.winner==myPieceType){
    		return 1000;
    	}else if(board.winner== board.opponent(myPieceType)){
    		return -1000;
    	}
        return noOfMyPieces-noOfOpponentPieces;  
    }    
	
}
