package ooga.model.animation;

import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import ooga.controller.BackEndExternalAPI;
import ooga.model.Direction;
import ooga.model.player.Avatar;
import ooga.model.player.Element;

public class AnimationPane {

  private BackEndExternalAPI modelController;
  private Deque<Double> commandsToBeExecuted;
  private Deque<String> typeToBeUpdated;
  private Map<Integer, Element> allElementInformation;


  private static final String DEFAULT_RESOURCES =
      AnimationPane.class.getPackageName() + ".resources.";
  private static final String UPDATE_NEXT_RESOURCE =
      DEFAULT_RESOURCES + "UpdateNextReflectionActions";
  private int INCREMENT_FACTOR = 10;
  private int currentID = 1;
  private static final String PANE_BOX_ID = "AvatarView";



  public AnimationPane(BackEndExternalAPI modelController){
    this.modelController = modelController;
    commandsToBeExecuted = new ArrayDeque<>();
    allElementInformation = new HashMap<>();
  }

  public void updateCommandQueue(String commandType, List<Double> commandValues) {
    typeToBeUpdated.add(commandType);
    commandsToBeExecuted.addAll(commandValues);
  }

  private void clearQueue() {
    commandsToBeExecuted.clear();
    typeToBeUpdated.clear();
  }

  public void updateAvatarStates() {
    String key;
    ResourceBundle updateNextActionResources = ResourceBundle.getBundle(UPDATE_NEXT_RESOURCE);
    if (!typeToBeUpdated.isEmpty()) {
      key = typeToBeUpdated.removeFirst();
      try {
        String methodName = updateNextActionResources.getString(key);
        Method m = AnimationPane.this.getClass().getDeclaredMethod(methodName);
        m.invoke(AnimationPane.this);
      } catch (Exception e) {
        new Alert(Alert.AlertType.ERROR);
      }
    }
  }

  /**
   * This is when we actually change the x, y coordinates of the sprite
   */
  private void updatePosition() {
    double nextX = commandsToBeExecuted.pop();
    double nextY = commandsToBeExecuted.pop();

//    if (nextY < 0 || nextX < 0 || nextY > cols - TURTLE_HEIGHT || nextX > rows - TURTLE_WIDTH) {
//      Alert error = new Alert(AlertType.ERROR);
//      error.setContentText(errorLanguageResource.getString("TurtleOutOfBounds"));
//      nextX = centerX;
//      nextY = centerY;
//      error.show();
//      viewController.processUserCommandInput(cS);
//    }
//
//    if (allTurtleInformation.get(currentID).getPenState() == 1) {
//      createLine(nextX, nextY, penColor);
//    }
//    allTurtleInformation.get(currentID).getTurtle().setX(nextX);
//    allTurtleInformation.get(currentID).getTurtle().setY(nextY);

    System.out.println("updatePosition called");
  }

  /**
   * This is the method in which we add the incremented x,y positions into the queue for update later
   * @param x
   * @param y
   */
  public void setPosition(double x, double y) {
    int increment = 10;

  }

  private void setID() {
    currentID = (int) Math.round(commandsToBeExecuted.pop());
  }

  //
  public void setActiveAvatar(int avatarID) {

    currentID = avatarID;
    commandsToBeExecuted.add((double) avatarID);
    typeToBeUpdated.add("SetID");
  }

  public void createAvatar(int id, Element element){
    allElementInformation.put(id, element);
   // System.out.println(allElementInformation);
  }

  public Map<Integer, Element> getAllElementInformation(){
    return allElementInformation;
  }


  public void moveAvatar(Avatar dummy, Direction direction) {

    int xPrev = dummy.getXCoord();
    int yPrev = dummy.getYCoord();
    dummy.setXCoord(dummy.getXCoord() + direction.getXDel());
    dummy.setYCoord(dummy.getYCoord() + direction.getYDel());


    System.out.printf("Moving avatar %d from (%d, %d) in the direction %s to new location (%d, %d)\n", dummy.getId(), xPrev, yPrev, direction, dummy.getXCoord(), dummy.getYCoord());

  }



}