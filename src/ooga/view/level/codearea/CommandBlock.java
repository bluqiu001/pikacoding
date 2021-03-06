package ooga.view.level.codearea;

import java.util.Map;

/**
 * Contains all of the information for the command of this block.
 *
 * @author David Li
 */
public class CommandBlock {

  private int index;
  private final String type;
  private final Map<String, String> parameters;

  /**
   * Main constructor
   * @param index Index of the command
   * @param type The command type
   * @param parameters Map from parameter name to parameter option
   */
  public CommandBlock(int index, String type, Map<String, String> parameters) {
    this.index = index;
    this.type = type;
    this.parameters = parameters;
  }

  public int getIndex() {
    return index;
  }

  public String getType() {
    return type;
  }

  public Map<String, String> getParameters() {
    return parameters;
  }

  public void setParameter(String parameter, String value) {
    parameters.put(parameter, value);
  }

  void setIndex(int index) {
    this.index = index;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof CommandBlock commandBlock) {
      if (parameters == null) {
        return (this.index == commandBlock.index &&
            this.type.equals(commandBlock.type) &&
            (null == (commandBlock.parameters)) || commandBlock.parameters.size() == 0);
      }
      return (this.index == commandBlock.index &&
          this.type.equals(commandBlock.type) &&
          this.parameters.equals(commandBlock.parameters));
    }
    return false;
  }
}
