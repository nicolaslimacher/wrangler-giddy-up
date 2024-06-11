package com.mygdx.game.Board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.mygdx.game.BoardUI.MoveConfirmationMenu;
import com.mygdx.game.BoardUI.MoveSelectButtonMenu;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.Utils.Constants;
import com.mygdx.game.Utils.CoordinateBoardPair;

public class Board extends Group {

    public int[][] boardGrid; // boardGrid[r][c] is the contents of row r, column c.
    public float screenWidth, screenHeight;
    public int boardRows, boardColumns;
    public MoveSet selectedMoveSet = null;
    //menus
    public MoveSelectButtonMenu menuTable;
    public MoveConfirmationMenu confirmationMenu;

    //constructor for Board
    public Board(int rows, int columns) {
        boardGrid = new int[rows][columns];
        this.boardRows = rows;
        this.boardColumns = columns;
        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();
        this.setSize(screenWidth*0.8f, screenHeight*0.8f);
        sizeAndAddBoardTiles();
    }//end constructor

    //using right 4/5s of screen to leave room for menus
    private float GetTileXPosition (int columnIndex, int columnTotal){
        float columnWidth = (screenWidth*Constants.SCREEN_BOARD_RATION *.8f) / (float)columnTotal;
        return ((screenWidth*Constants.SCREEN_BOARD_RATION *.2f) + (1-Constants.SCREEN_BOARD_RATION)/2*screenWidth) + (columnWidth * columnIndex) + (columnWidth / 2f) - (Constants.TILE_SIZE /2f);
    }
    private float GetTileYPosition (int rowIndex, int rowTotal){
        float rowHeight = (screenHeight*Constants.SCREEN_BOARD_RATION) / (float)rowTotal;
        return ((1-Constants.SCREEN_BOARD_RATION)/2*screenHeight) + (rowHeight * rowIndex) + (rowHeight / 2f) - (Constants.TILE_SIZE /2f);
    }

    public Vector2 GetBoardTilePosition (CoordinateBoardPair CoordinateBoardPair){
        //get tile
        Vector2 tileCoordinates = new Vector2();
        tileCoordinates.x = GetTileXPosition(CoordinateBoardPair.x, boardColumns);
        tileCoordinates.y = GetTileYPosition(CoordinateBoardPair.y, boardRows);
        return tileCoordinates;
    }

    //TODO: research viewport keep ratio and add minimum window size (can i stop desktop user from making it too small?)
    public void sizeAndAddBoardTiles(){
        this.clearChildren(); //removes already drawn board tiles
        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();
        for (int row = 0; row < boardGrid.length; ++row) {
            for (int column = 0; column < boardGrid[row].length; ++column) {
                float tilePositionX = GetTileXPosition(column, boardColumns);
                float tilePositionY = GetTileYPosition(row, boardRows);
                BoardTile boardTile = new BoardTile(tilePositionX, tilePositionY, new CoordinateBoardPair(row, column));
                boardTile.setName("tile" + boardTile.CoordinateBoardPair.GetX() +","+ boardTile.CoordinateBoardPair.GetY());
                this.addActor(boardTile);
            }
        }
    }
}//end Board class
