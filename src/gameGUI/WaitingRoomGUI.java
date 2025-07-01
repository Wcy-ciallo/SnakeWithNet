package gameGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import gameControl.*;
import gameNetwork.*;

public class WaitingRoomGUI extends JFrame {
    private StartupGUI startupGUI;
    private GameServer server;
    private JLabel statusLabel;
    private JButton cancelButton;
    private Timer refreshTimer;
    
    public WaitingRoomGUI(StartupGUI startupGUI, GameServer server) {
        super("等待玩家加入");
        this.startupGUI = startupGUI;
        this.server = server;
        
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 30));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("等待玩家加入...", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 1, 5, 10));
        
        JLabel ipLabel = new JLabel("本机IP: " + getLocalIP(), JLabel.CENTER);
        ipLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        infoPanel.add(ipLabel);
        
        JLabel portLabel = new JLabel("端口: " + server.getPort(), JLabel.CENTER);
        portLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        infoPanel.add(portLabel);
        
        statusLabel = new JLabel("等待连接中...", JLabel.CENTER);
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        infoPanel.add(statusLabel);
        
        panel.add(infoPanel, BorderLayout.CENTER);
        
        cancelButton = new JButton("取消");
        cancelButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        cancelButton.addActionListener(e -> {
            server.stopServer();
            dispose();
            startupGUI.setVisible(true);
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(panel);
        
        // 添加窗口关闭事件
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                server.stopServer();
                dispose();
                startupGUI.setVisible(true);
            }
        });
        
        // 创建刷新计时器，每秒检查一次连接状态
        refreshTimer = new Timer(1000, e -> {
            if (server.getState().equals("PLAYING")) {
                // 如果服务器状态为PLAYING，说明有玩家已经连接并准备开始
                refreshTimer.stop();
                startGame();
            } else {
                // 更新状态文本
                statusLabel.setText("等待连接中... " + server.getState());
            }
        });
        refreshTimer.start();
    }
    
    private void startGame() {
        SwingUtilities.invokeLater(() -> {
            dispose();
            GameGUI gameGUI = new GameGUI(startupGUI);
            gameGUI.setTitle("Snake - 房主");
            gameGUI.setVisible(true);
        });
    }
    
    private String getLocalIP() {
        try {
            return java.net.InetAddress.getLocalHost().getHostAddress();
        } catch (java.net.UnknownHostException e) {
            return "未知";
        }
    }
    
    @Override
    public void dispose() {
        if (refreshTimer != null) {
            refreshTimer.stop();
        }
        super.dispose();
    }
}