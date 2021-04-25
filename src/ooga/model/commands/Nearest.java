package ooga.model.commands;

import java.util.Map;
import ooga.model.Direction;
import ooga.model.grid.ElementInformationBundle;
import ooga.model.grid.Tile;
import ooga.model.grid.gridData.BlockData;
import ooga.model.player.Avatar;
import ooga.model.player.Block;

public class Nearest extends AICommands{

  private int X = 0;
  private int Y = 1;
  public Nearest(ElementInformationBundle elementInformationBundle,
      Map<String, String> parameters) {
    super(elementInformationBundle, parameters);
  }

  @Override
  public void execute(int ID) {
    Avatar avatar = (Avatar) getElementInformationBundle().getAvatarById(ID);

    int minDistance = 10000;
    int xAvatar = avatar.getXCoord();
    int yAvatar = avatar.getYCoord();
    BlockData closestBlockData = null;
    for(BlockData blockData : getElementInformationBundle().getBlockData()){
      int xBlock = blockData.getLocation().get(X);
      int yBlock = blockData.getLocation().get(Y);
      int manhattanDistance = Math.abs(xAvatar - xBlock) + Math.abs(yAvatar - yBlock);
      if(manhattanDistance < minDistance){
        minDistance = manhattanDistance;
        closestBlockData = blockData;
      }
    }
    stepTowardsClosestAvailableTile(ID, closestBlockData);
    avatar.setProgramCounter(avatar.getProgramCounter() + 1);
  }

  private void stepTowardsClosestAvailableTile(int ID, BlockData block) {
    Avatar avatar = (Avatar) getElementInformationBundle().getAvatarById(ID);
    int xBlock = block.getLocation().get(X);
    int yBlock = block.getLocation().get(Y);
    int initialManhattanDistance = Math.abs(avatar.getXCoord() - xBlock) + Math.abs(avatar.getYCoord() - yBlock);
    int newX = avatar.getXCoord();
    int newY = avatar.getYCoord();
    Tile prevTile = getCurrTile(ID);
    Tile nextTile = getNextTile(ID, Direction.CURRENT);
    for(Direction direction : Direction.values()){
      int dummyX = avatar.getXCoord() + direction.getXDel();
      int dummyY = avatar.getYCoord() + direction.getYDel();
      int manhattanDistance = Math.abs(dummyX - xBlock) + Math.abs(dummyY - yBlock);
      if(manhattanDistance < initialManhattanDistance && getNextTile(ID, direction).canAddAvatar()){
       initialManhattanDistance = manhattanDistance;
       newX = dummyX;
       newY = dummyY;
       nextTile = getNextTile(ID, direction);
      }
    }
    moveAvatar(avatar, prevTile, nextTile, newX, newY);
    sendAvatarPositionUpdate(avatar);
  }

  private void moveAvatar(Avatar avatar, Tile prevTile, Tile nextTile, int newX, int newY) {
    nextTile.add(avatar);
    prevTile.removeAvatar();
    avatar.setXCoord(newX);
    avatar.setYCoord(newY);
    if (avatar.hasBlock()) {
      avatar.getHeldItem().setXCoord(newX);
      avatar.getHeldItem().setYCoord(newY);
      sendBlockPositionUpdate(avatar.getHeldItem());
    }
  }
}
