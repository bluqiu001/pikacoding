package ooga.model;

import java.util.*;
import javafx.scene.web.HTMLEditorSkin.Command;
import ooga.controller.BackEndExternalAPI;
import ooga.model.grid.GameGrid;
import ooga.model.grid.gridData.BoardState;
import ooga.view.level.Board;
import ooga.view.level.codearea.CommandBlock;

/**
 * 
 */
public class CommandExecutor {

    private List<CommandBlock> commandBlocks;
    private Map<Integer, CommandBlock> mapOfCommandBlocks;
    private int programCounter;
    private BackEndExternalAPI modelController;
    private GameGrid gameGrid;
    private BoardState initialState;
    /**
     * Default constructor
     */
    public CommandExecutor(List<CommandBlock> commandBlocks, BackEndExternalAPI modelController, BoardState initialState) {
        this.initialState = initialState;
        programCounter = 1;
        gameGrid = new GameGrid(modelController);
        this.modelController = modelController;
        mapOfCommandBlocks = new HashMap<>();
        System.out.println("Command block received from frontend");
        System.out.println("Initial Positions: " + initialState.getAllAvatarLocations());
        System.out.println("Initial State of the Board: " + initialState.getAllBlockData());

        // build a map of all commands to be executed
        for(CommandBlock commandBlock : commandBlocks){
            mapOfCommandBlocks.put(commandBlock.getIndex(), commandBlock);
        }
        this.commandBlocks = commandBlocks;
    }

    public void runNextCommand() {
        CommandBlock currentCommand = mapOfCommandBlocks.get(programCounter);
        if(programCounter < mapOfCommandBlocks.size() + 1){
            System.out.println();
            System.out.println("Command currently running: " + currentCommand.getType() + " with parameter " + currentCommand.getParameters());
        }else{
            modelController.declareEndOfAnimation();
        }


        programCounter++;
    }
}