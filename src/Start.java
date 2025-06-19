import gameGUI.*;
import javax.swing.*;

public class Start extends JFrame {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      System.out.println(Start.class.getResource("/resource/img/background.png"));
      StartupGUI startupGUI = new StartupGUI();
      startupGUI.pack();
      startupGUI.setVisible(true);
    });
  }
}