package gameControl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;
import gameGUI.GameGUI;
import gameGUI.StartupGUI;

public class BtnListener implements ActionListener{
    private StartupGUI startupGUI;

    public BtnListener(StartupGUI startupGUI) {
        this.startupGUI = startupGUI;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        switch(cmd) {
            case "Local": 
                startupGUI.setVisible(false);
                SwingUtilities.invokeLater(() -> {
                    GameGUI gameGUI = new GameGUI(startupGUI);
                    gameGUI.pack();
                    gameGUI.setVisible(true);
                });
                break;
            
            case "CreateRoom": break;

            case "EnterRoom": break;

            case "Exit":
                startupGUI.dispose();
                System.exit(0);
                break;
        }
    }
    
}
