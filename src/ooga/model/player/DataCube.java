package ooga.model.player;

/**
 * DataCubes are a type of object that avatars can interact with and manipulate. They contain a
 * display number that can be manipulated.
 *
 * @author Harrison Huang
 */
public class DataCube extends Block {

    private int xCoord;
    private int yCoord;
    private final int id;
    private int displayNum;


    /**
     * Default constructor
     */
    public DataCube(int id, int xCoord, int yCoord, int displayNum) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.id = id;
        this.setDisplayNum(displayNum);
    }

    /**
     * Getter for the ID of the element.
     *
     * @return The ID of the element
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Getter for the x-coordinate of the Element.
     *
     * @return The x-coordinate of the Element
     */
    @Override
    public int getXCoord() {
        return xCoord;
    }

    /**
     * Getter for the y-coordinate of the Element.
     *
     * @return The y-coordinate of the Element
     */
    @Override
    public int getYCoord() {
        return yCoord;
    }

    /**
     * Updates the X and Y coordinates of the Element.
     *
     * @param xCoord The new x-coordinate
     * @param yCoord The new y-coordinate
     */
    @Override
    public void setXY(int xCoord, int yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }


    public int getDisplayNum() {
        return displayNum;
    }

    public void setDisplayNum(int displayNum) {
        this.displayNum = displayNum;
    }




}