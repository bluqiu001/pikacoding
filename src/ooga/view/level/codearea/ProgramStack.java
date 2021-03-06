package ooga.view.level.codearea;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import ooga.model.commands.AvailableCommands;

/**
 * Displays the player-created program comprised of command blocks.
 *
 * @author David Li
 */
public class ProgramStack extends VBox {

  private final List<CommandBlockHolder> programBlocks;
  private AvailableCommands availableCommands;
  private AvailableCommands availableCommandsOtherPlayer;
  private final List<ProgramListener> programListeners;

  private int newIndex = 0;

  /**
   * Main constructor
   */
  public ProgramStack() {
    this.setId("program-stack");
    this.setSpacing(5);
    programBlocks = new ArrayList<>();
    programListeners = new ArrayList<>();
  }

  /**
   * Sets the available commands for the program
   * @param availableCommands Available commands
   */
  public void setAvailableCommands(AvailableCommands availableCommands) {
    this.availableCommands = availableCommands;
  }

  /**
   * Sets the available commands of the other player for the program
   * @param availableCommands Available commands
   */
  public void setAvailableCommandsOtherPlayer(AvailableCommands availableCommands) {
    this.availableCommandsOtherPlayer = availableCommands;
  }

  /**
   * Adds a command block to the program stack
   * @param command Type of command
   */
  public void addCommandBlock(String command) {
//    System.out.println(command + " 1");
    System.out.println(command + " " + availableCommands.getCommandNames());
    if (availableCommands.getCommandNames().contains(command)) {
      createAndAddCommandBlock(availableCommands, command, false);
    } else if (availableCommandsOtherPlayer.getCommandNames().contains(command)) {
      createAndAddCommandBlock(availableCommandsOtherPlayer, command, true);
    }
  }

  private void addCommandBlockFromDatabase(String command) {
//    System.out.println(command + " 1");
    if (command.startsWith("end ")) {
      if (availableCommands.getCommandNames().contains(command.substring(4))) {
        createAndAddCommandBlockFromDatabase(availableCommands, command, true, false);
      } else if (availableCommandsOtherPlayer.getCommandNames().contains(command.substring(4))) {
        createAndAddCommandBlockFromDatabase(availableCommandsOtherPlayer, command, true, true);
      }
    } else {
      if (availableCommands.getCommandNames().contains(command)) {
        createAndAddCommandBlockFromDatabase(availableCommands, command, false, false);
      } else if (availableCommandsOtherPlayer.getCommandNames().contains(command)) {
        createAndAddCommandBlockFromDatabase(availableCommandsOtherPlayer, command, false, true);
      }
    }
//    System.out.println(command + " 2");
//    System.out.println(command + " 3");
  }

  public List<CommandBlock> getProgram() {
    List<CommandBlock> program = new ArrayList<>();
    programBlocks.forEach(commandBlockHolder -> program.add(commandBlockHolder.getCommandBlock()));
    return program;
  }

  /**
   * Removes a command block from the progarm stack
   * @param index Index of command to be removed
   */
  public void removeCommandBlock(int index) {
    programBlocks.remove(index - 1);
    this.getChildren().remove(index - 1);
    for (int i = index - 1; i < programBlocks.size(); i++) {
      programBlocks.get(i).setIndex(i + 1);
    }
  }

  /**
   * Called when the player clicks the move button, prepares for a new location to be selected for
   * the command block to be moved to
   * @param commandBlockHolder Command block to be moved
   */
  public void startMove(CommandBlockHolder commandBlockHolder) {
    newIndex = commandBlockHolder.getCommandBlock().getIndex();
    commandBlockHolder.getStyleClass().add("command-block-selected");
    programBlocks.forEach(other -> {
      other.setButtonsDisabled(true);
      other.setOnMouseEntered(e -> {
        other.getStyleClass().add("command-block-hovered");
        newIndex = other.getCommandBlock().getIndex();
      });
      other.setOnMouseExited(e -> {
        other.getStyleClass().remove("command-block-hovered");
      });
      other.setOnMouseClicked(e -> {
        if (canBeMoved(commandBlockHolder, newIndex)) {
          moveCommandBlock(commandBlockHolder.getCommandBlock().getIndex(), newIndex);
          notifyProgramListeners();
        }
        resetMouseActions();
      });
    });
  }

  /**
   * Sets which line each avatar is running
   * @param lineNumbers Map from avatar ids to line numbers
   */
  public void setLineIndicators(Map<Integer, Integer> lineNumbers) {
    List<List<Integer>> indicators = new ArrayList<>();
    programBlocks.forEach(commandBlockHolder -> indicators.add(new ArrayList<>()));
    lineNumbers.forEach((id, lineNumber) -> indicators.get(lineNumber - 1).add(id));
    for (int i = 0; i < programBlocks.size(); i++) {
      programBlocks.get(i).setLineIndicators(indicators.get(i));
    }
  }

  /**
   * Adds a program listener
   * @param programListener Program listener
   */
  public void addProgramListener(ProgramListener programListener) {
    programListeners.add(programListener);
  }

  /**
   * Notifies each of the program listeners that the program has updated
   */
  public void notifyProgramListeners() {
    programListeners.forEach(ProgramListener::onProgramUpdate);
  }

  /**
   * Updates the local program to sync with the database program
   * @param program New program stack
   */
  public void receiveProgramUpdates(List<CommandBlock> program) {
//    System.out.println("program recieved " + program.size());
    Platform.runLater(() -> {
      programBlocks.clear();
      this.getChildren().clear();
    });

    Platform.runLater(() -> {
      for (int i = 0; i < program.size(); i++) {
        CommandBlock commandBlock = program.get(i);
        addCommandBlockFromDatabase(commandBlock.getType());
        if (commandBlock.getParameters() != null) {
          for (String parameter : commandBlock.getParameters().keySet()) {
            String option = commandBlock.getParameters().get(parameter);
            if (programBlocks.get(programBlocks.size() - 1) instanceof NestedEndBlockHolder) {
              int size = programBlocks.size();
              programBlocks.remove(size - 1);
              Platform.runLater(() -> this.getChildren().remove(size - 1));
            }
            programBlocks.get(programBlocks.size() - 1)
                .selectParameter(parameter, option);
          }
        }
      }
    });

//      commandBlock.getParameters().forEach(
//          (parameter, option) -> {
//            System.out.println(parameter + option);
//            programBlocks.get(programBlocks.size() - 1)
//              .selectParameter(parameter, option);});
//    System.out.println("paratmeter");
  }
//    System.out.println(programCopy.size());

  private void createAndAddCommandBlock(AvailableCommands availableCommands, String command,
      boolean isMultiplayer) {
    List<Map<String, List<String>>> parameterOptions = new ArrayList<>();
    if (isMultiplayer) {
      availableCommandsOtherPlayer.getParameters(command).forEach(parameter -> {
        Map<String, List<String>> parameterOptionsMap = new HashMap<>();
        parameterOptionsMap
            .put(parameter, availableCommandsOtherPlayer.getParameterOptions(command, parameter));
        parameterOptions.add(parameterOptionsMap);
      });
    } else {
      availableCommands.getParameters(command).forEach(parameter -> {
        Map<String, List<String>> parameterOptionsMap = new HashMap<>();
        parameterOptionsMap
            .put(parameter, availableCommands.getParameterOptions(command, parameter));
        parameterOptions.add(parameterOptionsMap);
      });
    }
    if (command.equals("jump")) {
      addJumpCommandBlock(command, parameterOptions, isMultiplayer);
    } else if (command.equals("if")) {
      addNestedCommandBlocks(command, parameterOptions, isMultiplayer);
    } else {
      addStandardCommandBlock(command, parameterOptions, isMultiplayer);
    }
  }

  private void createAndAddCommandBlockFromDatabase(AvailableCommands availableCommands,
      String command, boolean isNestedEnd, boolean isMultiplayer) {
    if (isNestedEnd) {
      addNestedEndCommandBlock(command, isMultiplayer);
    } else {
      createAndAddCommandBlock(availableCommands, command, isMultiplayer);
    }
  }

  private void addNestedEndCommandBlock(String command, boolean isMultiplayer) {
    CommandBlockHolder commandBlockHolder = new CommandBlockHolder(programBlocks.size() + 1,
        command, new ArrayList<>(), this);
    if (isMultiplayer) {
      commandBlockHolder.setOtherPlayer();
    }
    programBlocks.add(commandBlockHolder);
    Platform.runLater(() -> this.getChildren().add(commandBlockHolder));
  }

  private void resetMouseActions() {
    programBlocks.forEach(commandBlockHolder -> {
      commandBlockHolder.getStyleClass().remove("command-block-selected");
      commandBlockHolder.getStyleClass().remove("command-block-hovered");
      commandBlockHolder.setButtonsDisabled(false);
      commandBlockHolder.setOnMouseEntered(e -> {
      });
      commandBlockHolder.setOnMouseClicked(e -> {
      });
    });
  }

  private boolean canBeMoved(CommandBlockHolder commandBlockHolder, int newIndex) {
    if (commandBlockHolder instanceof NestedBeginBlockHolder) {
      return newIndex < ((NestedBeginBlockHolder) commandBlockHolder).getEndCommandBlockHolder()
          .getIndex();
    } else if (commandBlockHolder instanceof NestedEndBlockHolder) {
      return newIndex > ((NestedEndBlockHolder) commandBlockHolder).getBeginCommandBlockHolder()
          .getIndex();
    }
    return true;
  }

  private void moveCommandBlock(int oldIndex, int newIndex) {
    if (oldIndex < newIndex) {
      Collections.rotate(programBlocks.subList(oldIndex - 1, newIndex), -1);
    } else if (oldIndex > newIndex) {
      Collections.rotate(programBlocks.subList(newIndex - 1, oldIndex), 1);
    }
    this.getChildren().clear();
    for (int i = 0; i < programBlocks.size(); i++) {
      programBlocks.get(i).setIndex(i + 1);
      this.getChildren().add(programBlocks.get(i));
    }
  }

  private void addStandardCommandBlock(String command,
      List<Map<String, List<String>>> parameterOptions, boolean isMultiplayer) {
    CommandBlockHolder commandBlockHolder = new CommandBlockHolder(programBlocks.size() + 1,
        command, parameterOptions, this);
    if (isMultiplayer) {
      commandBlockHolder.setOtherPlayer();
    }
    programBlocks.add(commandBlockHolder);
    Platform.runLater(() -> this.getChildren().add(commandBlockHolder));
  }

  private void addNestedCommandBlocks(String command,
      List<Map<String, List<String>>> parameterOptions, boolean isMultiplayer) {
    NestedBeginBlockHolder beginCommandBlockHolder = new NestedBeginBlockHolder(
        programBlocks.size() + 1,
        command, parameterOptions, this);
    programBlocks.add(beginCommandBlockHolder);
    NestedEndBlockHolder endCommandBlockHolder = new NestedEndBlockHolder(
        programBlocks.size() + 1,
        command, this);
    programBlocks.add(endCommandBlockHolder);
    beginCommandBlockHolder.attachEndHolder(endCommandBlockHolder);
    endCommandBlockHolder.attachBeginHolder(beginCommandBlockHolder);
    if (isMultiplayer) {
      beginCommandBlockHolder.setOtherPlayer();
      endCommandBlockHolder.setOtherPlayer();
    }
    Platform
        .runLater(() -> this.getChildren().addAll(beginCommandBlockHolder, endCommandBlockHolder));
  }

  private void addJumpCommandBlock(String command,
      List<Map<String, List<String>>> parameterOptions, boolean isMultiplayer) {
    CommandBlockHolder commandBlockHolder = new JumpCommandBlockHolder(programBlocks.size() + 1,
        command, parameterOptions, this);
    if (isMultiplayer) {
      commandBlockHolder.setOtherPlayer();
    }
    programBlocks.add(commandBlockHolder);
    Platform.runLater(() -> this.getChildren().add(commandBlockHolder));
  }

}
