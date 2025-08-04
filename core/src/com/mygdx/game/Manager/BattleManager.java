package com.mygdx.game.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.Board.Board;
import com.mygdx.game.HUD.MoveConfirmation;
import com.mygdx.game.HUD.MoveSelectCards;
import com.mygdx.game.Command.Command;
import com.mygdx.game.EnemyAI.EnemyAI;
import com.mygdx.game.GamePiece.GamePiece;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.Screens.BattleScreen;
import com.mygdx.game.Utils.Helpers;
import com.mygdx.game.Utils.IntPair;
import com.mygdx.game.WranglerGiddyUp;

import java.util.ArrayList;
import java.util.List;

public class BattleManager extends Actor{
    private static final String TAG = BattleManager.class.getSimpleName();
    Stage stage;
    public Board board;
    final BattleScreen battleScreen;

    //background
    public TextureRegion smallStar;
    public TextureRegion mediumStar;

    //turn
    public int turnNumber = 1;
    public Team currentTurn;
    public boolean movedThisTurn = false;

    //game pieces
    public ArrayList<GamePiece> friendlyGamePieces;
    public ArrayList<GamePiece> enemyGamePieces;
    public Command latestGamePieceCommand;
    public EnemyAI enemyAI;

    //selected Moves and GamePiece
    public List<MoveSet> availableMoveSets;
    public List<MoveSet> enemyMoves = new ArrayList<>(2);
    public List<MoveSet> freeMove = new ArrayList<>(1);
    public List<MoveSet> playerMoves = new ArrayList<>(2);
    public MoveSet selectedMoveSet = null;

    //menu
    public MoveSelectCards moveSelectCards;
    public MoveConfirmation moveConfirmation;

    public BattleManager(Stage stage, Board board, List<MoveSet> availableMoveSets, BattleScreen battleScreen) {
        this.stage = stage;
        this.board = board;
        this.battleScreen = battleScreen;
        smallStar = GetAssetManager().get("texturePacks/battleTextures.atlas", TextureAtlas.class).findRegion("smallstar1");
        mediumStar = GetAssetManager().get("texturePacks/battleTextures.atlas", TextureAtlas.class).findRegion("mediumstar.png");

        this.availableMoveSets = availableMoveSets;
        AssignStartingChemicals();
        this.moveSelectCards = new MoveSelectCards(this, stage);
        stage.addActor(this.moveSelectCards);

        friendlyGamePieces = new ArrayList<>(10);
        enemyGamePieces = new ArrayList<>(10);

        this.currentTurn = Team.FRIENDLY;
        this.setName("BattleManager");
        Gdx.app.log("BattleManager", "BattleManager created.");
        ((WranglerGiddyUp) Gdx.app.getApplicationListener()).getRunManager().setCurrentBattleManager(this);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                       GamePiece Management                                 //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public Boolean IsGamePieceAtBoardLocation(IntPair coordinates) {
        return GetGamePieceAtCoordinate(coordinates) != null;
    }
    public Boolean IsTeamGamePieceAtBoardLocation(IntPair coordinates, Team team) {
        return GetGamePieceAtCoordinate(coordinates) != null && (GetGamePieceAtCoordinate(coordinates).team == team);
    }

    //todo: check enemyGamePieces and friendlyGamePieces
    public GamePiece GetGamePieceAtCoordinate(IntPair coordinateBoardPair){
        for(Actor actor:this.getStage().getActors()){
            if(actor.getClass() == GamePiece.class) {
                GamePiece gamePiece = (GamePiece) actor;
                if (gamePiece.indexOnBoard.equals(coordinateBoardPair)) {
                    return gamePiece;
                }
            }
        }
        return null;
    }

    private boolean IsValidKingLeft(Team team){
        ArrayList<GamePiece> gamePieces = (team == Team.FRIENDLY) ? friendlyGamePieces : enemyGamePieces;
        for ( GamePiece gamePiece : gamePieces) {
            if (gamePiece.isKing && gamePiece.isAlive) {
                return true;
                //this.battleScreen.SwitchScreenEndGame();
            }
        }
        return false;
    }

    public AssetManager GetAssetManager(){
        return battleScreen.GetGame().getAssetManager();
    }

    private boolean PlayerHasAValidMove(){
        for (GamePiece gamePieceToMove :  this.friendlyGamePieces) {
            if (gamePieceToMove.isAlive) {
                for (MoveSet moveSet : this.playerMoves) {
                    for (IntPair possibleMove : moveSet.possibleMoves) {
                        if (MoveManager.IsValidMove(gamePieceToMove, possibleMove) && !IsTeamGamePieceAtBoardLocation(possibleMove, Team.FRIENDLY)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean EndGameScreenIfKingsDead(Team team){
        if (IsValidKingLeft(team)){
            System.out.println("There is a valid enemy king left");

        }else{
            System.out.println("There is no valid enemy king left");
            //switch screens
            this.battleScreen.SwitchScreenEndGame();
            return true;
        }
        return false;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                       MoveSet Management                                   //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void AssignStartingChemicals () {
        enemyMoves.add(availableMoveSets.get(0));
        enemyMoves.add(availableMoveSets.get(1));
        freeMove.add(availableMoveSets.get(2));
        playerMoves.add(availableMoveSets.get(3));
        playerMoves.add(availableMoveSets.get(4));

    }

    private void ShuffleCardsAfterPlayer(MoveSet playerMoveSetUsed){
        playerMoves.remove(playerMoveSetUsed);
        MoveSet freeMoveToAdd = freeMove.get(0);
        freeMove.remove(freeMoveToAdd);
        freeMove.add(playerMoveSetUsed);
        playerMoves.add(freeMoveToAdd);

    }

    private void ShuffleCardsAfterEnemy(MoveSet enemyMoveSetUsed){
        enemyMoves.remove(enemyMoveSetUsed);
        MoveSet freeMoveToAdd = freeMove.get(0);
        freeMove.remove(freeMoveToAdd);
        freeMove.add(enemyMoveSetUsed);
        enemyMoves.add(freeMoveToAdd);
    }

    public void DisplayPlayerMessage(String headerMessage){
        new DisplayMessage(this, headerMessage, 3f);
    }

    public void DisplayPlayerMessage(String headerMessage, String subMessage){
        new DisplayMessage(this, headerMessage, subMessage, 3f);
    }

    public void EndPlayerTurn(){
        //returning bool so EndGameScreenIfKingsDead can exit method
        //could wrap rest of call in check for enemyKingIsDead?

        //check if enemy team has a king left, then check if king has any health
        if(EndGameScreenIfKingsDead(Team.ENEMY))
            return;


        //reset and shuffle chemicals
        if (this.getStage().getRoot().findActor("MoveConfirmationMenu") != null){
            MoveConfirmation moveConf = this.getStage().getRoot().findActor("MoveConfirmationMenu");
            moveConf.ReturnToMoveSelectCards();
        }

        ShuffleCardsAfterPlayer(this.latestGamePieceCommand.moveSet);
        //update turn info
        this.turnNumber = this.turnNumber + 1;
        this.currentTurn = Team.ENEMY;
        this.movedThisTurn = false;
        this.latestGamePieceCommand = null;

        this.moveSelectCards.setVisible(true);
        this.moveSelectCards.UpdateCardLocations();
        //call AI to make turn
        battleScreen.getHUD().DisableEndTurnButton();
        battleScreen.getHUD().DisableUndoButton();

        enemyAI.MakeMove();
    }

    public void EndEnemyTurn(MoveSet enemyMoveSetUsed){

        ShuffleCardsAfterEnemy(enemyMoveSetUsed);
        this.moveSelectCards.UpdateCardLocations();

        battleScreen.getHUD().UpdateTurn();

        //after AI see if player has any possible moves
        if(!PlayerHasAValidMove()){
            DisplayPlayerMessage("No Valid Moves", "take some damage");
            //select king
            //king takes damage
            //increase move stuck damage
        }

        //check if enemy team has a king left, then check if king has any health
        EndGameScreenIfKingsDead(Team.FRIENDLY);
    }

    public void placeStartingEnemyGamePieces(JsonValue encounter){
        Gdx.app.debug(TAG, "Placing Staring Enemy Game Pieces");
        for (JsonValue gamePiece : encounter.get("gamePieces")){
            GamePiece enemyGamePiece = new GamePiece(
                    board,
                    this,
                    gamePiece.getInt("gamePieceID"),
                    new IntPair(gamePiece.getInt("xVal"),gamePiece.getInt("yVal")),
                    Team.ENEMY,
                    gamePiece.getBoolean("isKing"),
                    Helpers.getPRNGManager().getNextRand(PRNGManager.PRNGType.enemyStatsSeed, gamePiece.getInt("minHealth"), gamePiece.getInt("maxHealth")),
                    Helpers.getPRNGManager().getNextRand(PRNGManager.PRNGType.enemyStatsSeed, gamePiece.getInt("minAttack"), gamePiece.getInt("maxAttack"))
                    );
            enemyGamePieces.add(enemyGamePiece);
            stage.addActor(enemyGamePiece);
        }
    }

    public void placeStartingFriendlyGamePieces(){
        Gdx.app.debug(TAG, "Placing Staring Friendly Game Pieces");
        JsonValue playerGamePieces = new JsonReader().parse(Gdx.files.internal("JSONs/PlayerTeam.json"));
        for (JsonValue jsonGamePiece : playerGamePieces){
            GamePiece playerGamePiece = new GamePiece(
                    board,
                    this,
                    jsonGamePiece.getInt("gamePieceID"),
                    new IntPair(jsonGamePiece.getInt("xVal"),jsonGamePiece.getInt("yVal")),
                    Team.FRIENDLY,
                    jsonGamePiece.getBoolean("isKing"),
                    jsonGamePiece.getInt("health"),
                    jsonGamePiece.getInt("attack")
            );
            Gdx.app.debug(TAG, "player gamePiece starting health: " + jsonGamePiece.getInt("health"));
            Gdx.app.debug(TAG, "player gamePiece starting attack: " + jsonGamePiece.getInt("attack"));
            friendlyGamePieces.add(playerGamePiece);
            stage.addActor(playerGamePiece);
        }
    }
}
