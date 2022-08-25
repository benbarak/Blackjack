import javax.swing.*;
import java.awt.*;

public class Main {
  public static void main(String[] args) {
      Game game = Game.getInstance();
      GUI gui = GUI.getInstance();
      game.setGUI(gui);
      gui.setGame(game);
      gui.instructions();
  }
}
