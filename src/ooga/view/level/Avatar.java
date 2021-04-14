package ooga.view.level;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class Avatar {

  private static final String avatarImage = "PikachuAvatar.gif"; // TODO: put in resource file or get passed
  private int initialXCoordinate;
  private int initialYCoordinate;
  private double width;
  private double height;
  private SpriteLayer spriteLayer;
  private ImageView avatar;
  private int i = 0;
  private int k = 0;

  public Avatar(int x, int y, double w, double h, SpriteLayer root) {
    initialXCoordinate = x;
    initialYCoordinate = y;
    width = w;
    height = h;
    spriteLayer = root;
    makeAvatar();
  }

  private void makeAvatar() {
    avatar = new ImageView(new Image("PikachuAvatar.gif"));
    avatar.getStyleClass().add("avatar");
    avatar.setFitWidth(width);
    avatar.setFitHeight(height);
    reset();
    spriteLayer.getChildren().add(avatar);
  }

  public void moveAvatar(double x, double y) {
    double currentX = avatar.getX();
    double currentY = avatar.getY();
    double nextX = x * width;
    double nextY = y * height;
    avatar.setX(x * width);
    avatar.setY(y * height);

    if(currentX < nextX) {
      int num = ((i / 30) % 6) + 1;
      setAvatarImage("AnimatedPikachuRight" + num + ".gif");
      i++;
    }else if(nextX < currentX){
      int num = ((k/30) % 6) + 1;
      setAvatarImage("AnimatedPikachuLeft" + num + ".gif");
      k++;
    }
  }

  public void reset() {
    avatar.setX(initialXCoordinate * width);
    avatar.setY(initialYCoordinate * height);
  }

  public int getInitialXCoordinate(){
    return (int) (avatar.getX()/width);
  }

  public int getInitialYCoordinate(){
    return (int) (avatar.getY()/height);
  }

  public void setAvatarImage(String image){
    avatar.setImage(new Image(image));
  }
}
