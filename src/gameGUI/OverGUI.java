package gameGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import gameControl.*;

public class OverGUI extends JFrame{
    private int score;
    private GameGUI gameGUI;
    private StartupGUI startupGUI;
    
    public OverGUI(int score, GameGUI gameGUI, StartupGUI startupGUI) {
        super("Game Over!");
        this.score = score;
        this.gameGUI = gameGUI;
        this.startupGUI = startupGUI;

        this.setSize(400, 300);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("游戏结束", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 22));
        titlePanel.add(titleLabel);

        JPanel scorePanel = new JPanel();
        JLabel scoreLabel = new JLabel("你的得分：" + this.score, JLabel.CENTER);
        scoreLabel.setFont(new Font("微软雅黑", Font.PLAIN, 22));;
        scorePanel.add(scoreLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton restartButton = new JButton("重新开始");
        restartButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GameInfo.getInstance().resetGame();
                dispose();
                if (gameGUI != null) {
                    gameGUI.dispose(); // 释放旧的窗口
                }
                // 创建新的 GameGUI 并显示
                SwingUtilities.invokeLater(() -> {
                    GameGUI newGameGUI = new GameGUI(startupGUI);
                    newGameGUI.pack();
                    newGameGUI.setVisible(true);
                    newGameGUI.requestFocus();
                });
            }
        });

        JButton menuButton = new JButton("返回主菜单");
        menuButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        menuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                if (gameGUI != null) {
                    gameGUI.dispose();
                }
                GameInfo gameInfo = GameInfo.getInstance();
                gameInfo.resetGame();
                SwingUtilities.invokeLater(() -> {
                    StartupGUI newStartupGUI = new StartupGUI();
                    newStartupGUI.pack();
                    newStartupGUI.setVisible(true);
                });
            }
        });

        JButton exitButton = new JButton("退出游戏");
        exitButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonPanel.add(restartButton);
        buttonPanel.add(menuButton);
        buttonPanel.add(exitButton);

        this.add(titlePanel, BorderLayout.NORTH);
        this.add(scorePanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }
}
