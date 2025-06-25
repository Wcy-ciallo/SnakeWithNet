package gameGUI;

import java.awt.Dimension;
import javax.swing.JFrame;

public class GameGUI extends JFrame{
    private BoardPanel boardPanel;
    private StartupGUI startupGUI;
    public GameGUI(StartupGUI startupGUI) {
        super("Snake-Local");
        this.startupGUI = startupGUI;
        this.setPreferredSize(new Dimension(900, 720));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        boardPanel = new BoardPanel(false);
        this.add(boardPanel);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        boardPanel.requestFocus();
    }

    public StartupGUI getStartupGUI() {
        return this.startupGUI;
    }
    public void requestFocus() {
        if(boardPanel != null) {
            boardPanel.requestFocus();
        }
    }
}
