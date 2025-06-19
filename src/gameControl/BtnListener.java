package gameControl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
            case "Local": break;
            
            case "CreateRoom": 

            case "EnterRoom": 

            case "Exit":
                startupGUI.dispose();
                System.exit(0);
        }
    }
    
}
