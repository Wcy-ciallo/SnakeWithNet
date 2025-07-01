package gameControl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import gameGUI.GameGUI;
import gameGUI.StartupGUI;
import gameGUI.WaitingRoomGUI;
import gameNetwork.*;

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
            
            case "CreateRoom": 
                try {
                    int port = 8888;
                    GameServer server = new GameServer(port);
                    server.start();

                    startupGUI.setVisible(false);
                    SwingUtilities.invokeLater(() -> {
                        WaitingRoomGUI waitingRoom = new WaitingRoomGUI(startupGUI, server);
                        waitingRoom.setVisible(true);
                    });
                    System.out.println("创建房间成功！");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(startupGUI, "创建房间失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
                break;

            case "EnterRoom": 
                String serverIP = JOptionPane.showInputDialog(startupGUI, "请输入服务器IP：", "127.0.0.1");
                String portStr = JOptionPane.showInputDialog(startupGUI, "请输入端口号：", "8888");

                if(serverIP != null && portStr != null) {
                    try {
                        int port = Integer.parseInt(portStr);
                        GameClient client = new GameClient(serverIP, port);

                        GameGUI gameGUI = new GameGUI(startupGUI);
                        gameGUI.setNetworkMode(client);
                        gameGUI.setVisible(true);
                        startupGUI.setVisible(false);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(startupGUI, "无效的端口号", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;

            case "Exit":
                startupGUI.dispose();
                System.exit(0);
                break;
        }
    }
    
}
