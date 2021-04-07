

package ooga.controller;
import java.util.List;
import javax.lang.model.util.Elements;
import ooga.model.grid.gridData.BoardState;
import ooga.view.level.codearea.CommandBlock;

/**
 * Facilitates interaction between view and model.
 * @author Ji Yun Hyo
 * Specifically, responsible for receiving unparsed commands and passing them to the model to be parsed and ran.
 */
public interface BackEndExternalAPI {

  /**
   * sets the view controller to set up the line of communication from/to the backend
   * @param viewController FrontEndExternalAPI
   */
  public void setViewController(FrontEndExternalAPI viewController);

  /**
   * Passes in the commands to be parsed
   * @param commandBlocks List of individual command blocks derived from the blocks in the CodeBuilderArea
   */
  public void parseCommands(List<CommandBlock> commandBlocks);

  /**
   * Runs the next command in the command queue
   */
  public void runNextCommand();

  /**
   * Gets the list of changed states in order to update the frontend.
   *
   * @return The list of changed states in the grid
   */
  public List<Elements> getChangedStates();

  /**
   * Sets the position of the sprite
   */
  public void setPosition(double x, double y, int id);

  void setBoard(BoardState board);

  /**
   * All commands have reached the end and no more to be executed
   */
  void declareEndOfAnimation();

  /**
   * initializes the level
   * @param level integer indicating the level
   * @return BoardState object with level information
   */
  BoardState initializeLevel(int level);
}