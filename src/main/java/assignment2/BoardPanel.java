package assignment2;

import javafx.event.ActionEvent;

import java.util.*;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class BoardPanel extends GridPane implements EventHandler<ActionEvent> {

    private final View view;
    private final Board board;
//    private boolean ispieceselected = false;
    private Cell pieceselected =null;
    private Cell secondpieceselected = null;

    /**
     * Constructs a new GridPane that contains a Cell for each position in the board
     *
     * Contains default alignment and styles which can be modified
     * @param view
     * @param board
     */
    public BoardPanel(View view, Board board) {
        this.view = view;
        this.board = board;

        // Can modify styling
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: #181a1b;");
        int size = 550;
        this.setPrefSize(size, size);
        this.setMinSize(size, size);
        this.setMaxSize(size, size);

        setupBoard();
        updateCells();
    }


    /**
     * Setup the BoardPanel with Cells
     */
    private void setupBoard(){ // TODO
    	for(int i = 0; i < this.board.board.length;i++) {
    		for(int j = 0; j < this.board.board[i].length;j++) {
        		this.add(this.board.board[i][j], j, i);
        		this.board.board[i][j].setOnAction(this);
        	}
    	}
    }

    /**
     * Updates the BoardPanel to represent the board with the latest information
     *
     * If it's a computer move: disable all cells and disable all game controls in view
     *
     * If it's a human player turn and they are picking a piece to move:
     *      - disable all cells
     *      - enable cells containing valid pieces that the player can move
     * If it's a human player turn and they have picked a piece to move:
     *      - disable all cells
     *      - enable cells containing other valid pieces the player can move
     *      - enable cells containing the possible destinations for the currently selected piece
     *
     * If the game is over:
     *      - update view.messageLabel with the winner ('MUSKETEER' or 'GUARD')
     *      - disable all cells
     */
    protected void updateCells(){ // TODO
    	if(!this.view.model.isHumanTurn()) {
    		if (!(this.view.model.getCurrentAgent() instanceof HumanAgent) && this.view.model.getMovesSize() == 0 && this.view.gameMode != ThreeMusketeers.GameMode.Human) {
        		this.view.runMove();
    		}
    		for(int i = 0; i < this.board.board.length;i++) {
        		for(int j = 0; j < this.board.board[i].length;j++) {
            		this.board.board[i][j].setDisable(true);
            	}
        	}
    		if(!this.board.isGameOver()) {
	    		if(this.view.undoButton != null) {
	    			this.view.setUndoButton();
	    		}
	    		if(this.view.restartButton != null) {
	    			this.view.restartButton.setDisable(true);
	    		}
	    		if(this.view.saveButton != null) {
	    			this.view.saveButton.setDisable(true);
	    		}
	    		if(this.view.saveFileNameTextField != null) {
	    			this.view.saveFileNameTextField.setDisable(true);
	    		}
    		}
    	}
    	if(this.view.model.isHumanTurn()) {
    		if(this.view.restartButton != null) {
    			this.view.restartButton.setDisable(false);
    		}
    		if(this.view.saveButton != null) {
    			this.view.saveButton.setDisable(false);
    		}
    		if(this.view.saveFileNameTextField != null) {
    			this.view.saveFileNameTextField.setDisable(false);
    		}
    		if(pieceselected == null) {
    			// diabling all cells
    			for(int i = 0; i < this.board.board.length;i++) {
            		for(int j = 0; j < this.board.board[i].length;j++) {
                		this.board.board[i][j].setDisable(true);
                	}
            	}
    			List<Cell> possiblecells = this.board.getPossibleCells();
    			for(Cell pcells: possiblecells) {
    				pcells.setDisable(false);
    			}
    		}
    		else if(pieceselected != null) {
    			for(int i = 0; i < this.board.board.length;i++) {
            		for(int j = 0; j < this.board.board[i].length;j++) {
                		this.board.board[i][j].setDisable(true);
                	}
            	}
    			List<Cell> possiblecells = this.board.getPossibleCells();
    			for(Cell pcells: possiblecells) {
    				pcells.setDisable(false);
    			}
    			List<Cell> possibledestinations = this.board.getPossibleDestinations(pieceselected);
    			for(Cell pdcells: possibledestinations) {
    				pdcells.setDisable(false);
    			}

    		}
    	}
    	if(this.board.isGameOver()) {
    		this.view.setMessageLabel(this.board.getWinner().getType());
    		for(int i = 0; i < this.board.board.length;i++) {
        		for(int j = 0; j < this.board.board[i].length;j++) {
            		this.board.board[i][j].setDisable(true);
            	}
        	}
    	}
    }

    /**
     * Handles Cell clicks and updates the board accordingly
     * When a Cell gets clicked the following must be handled:
     *  - If it's a valid piece that the player can move, select the piece and update the board
     *  - If it's a destination for a selected piece to move, perform the move and update the board
     * @param actionEvent
     */
    @Override
    public void handle(ActionEvent actionEvent) { // TODO
    	List<Cell> possiblecells = this.board.getPossibleCells();
    	Cell source = (Cell) actionEvent.getSource();
    	if(possiblecells.contains(source)) {;
    		this.pieceselected = source;
    		this.updateCells();
    	}
    	else if(pieceselected != null &&this.board.getPossibleDestinations(pieceselected).contains(source)) {
    		secondpieceselected = source;
    		this.view.runMove();
    		if (!(this.view.model.getCurrentAgent() instanceof HumanAgent) && !this.board.isGameOver()) {
    			this.view.runMove();
    		}
    	}
    }
    public Cell getsecondpieceselected() {
    	return this.secondpieceselected;
    }
    public Cell getpieceselected() {
    	return this.pieceselected;
    }
    public void setpieceselected(Cell c) {
    	this.pieceselected = c;
    }
    public void setsecondpieceselected(Cell s) {
    	this.secondpieceselected = s;
    }
}
