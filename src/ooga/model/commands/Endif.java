package ooga.model.commands;

import java.util.Map;
import ooga.model.grid.ElementInformationBundle;
import ooga.model.player.Avatar;

public class Endif extends ConditionalCommands{

  /**
   * Default constructor
   *
   * @param elementInformationBundle
   * @param parameters
   */
  public Endif(ElementInformationBundle elementInformationBundle,
      Map<String, String> parameters) {
    super(elementInformationBundle, parameters);
  }

  @Override
  public void execute(int ID) {
    Avatar avatar = (Avatar) getElementInformationBundle().getAvatarById(ID);
    avatar.setProgramCounter(avatar.getProgramCounter() + 1);
  }
}
