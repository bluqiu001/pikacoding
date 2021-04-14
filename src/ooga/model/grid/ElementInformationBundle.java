package ooga.model.grid;

import com.google.api.Backend;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ooga.controller.BackEndExternalAPI;
import ooga.model.Direction;
import ooga.model.InformationBundle;
import ooga.model.grid.gridData.BlockData;
import ooga.model.grid.gridData.TileData;
import ooga.model.player.Avatar;
import ooga.model.player.AvatarData;
import ooga.model.player.DataCube;
import ooga.model.player.Element;
import ooga.model.player.Block;
import ooga.model.player.Player;

/**
 * The GameGrid contains all the elements for the grid of the game.
 */
public class ElementInformationBundle implements InformationBundle {

  private Tile[][] grid;
  private final List<Player> avatarList;
  private final List<Block> dataCubeList;
  private Map<Integer, Integer> lineUpdates;
  private AvatarData newUpdate;
  private BackEndExternalAPI modelController;

  public ElementInformationBundle() {
    avatarList = new ArrayList<>();
    dataCubeList = new ArrayList<>();
    lineUpdates = new HashMap<>();
    newUpdate = new AvatarData();
  }

  public List<Player> getAvatarList() {
    return Collections.unmodifiableList(avatarList);
  }

  public void setModelController(BackEndExternalAPI modelController){
    this.modelController = modelController;
  }

  public BackEndExternalAPI getModelController(){
    return modelController;
  }

  public List<BlockData> getBlockData() {
    List<BlockData> ret = new ArrayList<>();
    for (Block dataCube : dataCubeList) {
      ret.add(new BlockData(dataCube));
    }
    return ret;
  }


  public void setDimensions(int x, int y) {
    grid = new Tile[x][y];
    for (int i=0; i<x; i++) {
      for (int j=0; j<y; j++) {
        grid[i][j] = new Tile();
      }
    }
  }

  public Structure getStructure(int x, int y) {
    return grid[x][y].getStructure();
  }

  public void setStructure(int x, int y, Structure structure) {
    grid[x][y].setStructure(structure);
  }


  public void addGameElement(Element gameElement) {
    int xPos = gameElement.getXCoord();
    int yPos = gameElement.getYCoord();
    grid[xPos][yPos].add(gameElement);
    if (gameElement instanceof Avatar) {
      avatarList.add((Avatar) gameElement);
    }
    if (gameElement instanceof DataCube) {
      dataCubeList.add((DataCube) gameElement);
    }
  }

  //TODO: Remove
  /**
   * Moves the avatar in a cardinal direction.
   *
   * @param direction The direction to be moved
   */
  public void step(int avatarId, Direction direction) {
    Element avatar = getAvatarById(avatarId);
    assert avatar != null;
    int currX = avatar.getXCoord();
    int currY = avatar.getYCoord();
    int newX = currX + direction.getXDel();
    int newY = currY + direction.getYDel();
    if (grid[newX][newY].canAddAvatar()) {
      grid[newX][newY].add(avatar);
      grid[currX][currY].removeAvatar();
      avatar.step(direction);
    } else {
      //TODO: throw error to handler?
      System.out.println("The avatar cannot step here!");
    }

  }

  //TODO: Remove
  public void pickUp(int avatarId, Direction direction) {
    Player avatar = getAvatarById(avatarId);
    assert avatar != null;
    int currX = avatar.getXCoord();
    int currY = avatar.getYCoord();
    int newX = currX + direction.getXDel();
    int newY = currY + direction.getYDel();
    if (grid[newX][newY].hasBlock()) {
      avatar.pickUp(grid[newX][newY].getBlock());
      grid[currX][currY].removeBlock();
    } else {
      //TODO: throw error to handler
      System.out.println("There is no block to be picked up!");
    }
  }


  //TODO: Remove
  /**
   * Directs the avatar to drop the block it is holding.
   *
   * @param avatarId The id of the avatar
   */
  public void drop(int avatarId) {
    Player avatar = getAvatarById(avatarId);
    assert avatar != null;
    int currX = avatar.getXCoord();
    int currY = avatar.getYCoord();
    if (grid[currX][currY].canAddBlock()) {
      Block block = avatar.drop();
      if (block == null) {
        //TODO: throw error to handler
        System.out.println("You are not holding a block!");
      }
      grid[currX][currY].add(block);
    } else {
      //TODO: throw error to handler
      System.out.println("You cannot drop here!");

    }

  }

  public Player getAvatarById(int id) {
    for (Player avatar : avatarList) {
      if (avatar.getId() == id) {
        return avatar;
      }
    }
    return null; // should never happen
  }

  /**
   * Returns a collection of the IDs of all the current avatars.
   *
   * @return A collection of integers containing IDs
   */
  public Collection<Integer> getAvatarIds() {
    List<Integer> ids = new ArrayList<>();
    for (Element avatar : avatarList) {
      ids.add(avatar.getId());
    }
    Collections.sort(ids);
    return ids;
  }

  /**
   * Retrieves the information of a queried Tile as a TileData object.
   *
   * @param x The x-coordinate of the tile
   * @param y The y-coordinate of the tile
   * @return A TileData object containing information about the tile
   */
  public TileData getTileData(int x, int y) {
    return new TileData(grid[x][y]);
  }

  public Tile getTile(int x, int y) {
    return grid[x][y];
  }
}