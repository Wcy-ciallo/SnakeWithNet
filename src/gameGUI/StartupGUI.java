package gameGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import gameControl.BtnListener;

public class StartupGUI extends JFrame{
    private JButton localBtn, createRoomBtn, enterRoomBtn, exitBtn;

    public StartupGUI() {
        super("Snake");
        try{
            BufferedImage backgroundImage = ImageIO.read(getClass().getResource("/resource/img/background.png"));
            this.setLayout(new BorderLayout());
            this.setContentPane(new JLabel(new ImageIcon(backgroundImage)));
            this.setPreferredSize(new Dimension(900, 720));
            this.setResizable(false);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setLocationRelativeTo(null);

            JLabel titleLabel = new JLabel("欢迎来到贪吃蛇游戏!!", SwingConstants.CENTER);
            titleLabel.setFont(new Font("GB18030 Bitmap", Font.BOLD, 36));
            titleLabel.setForeground(Color.DARK_GRAY);
            titleLabel.setBounds(0, 60, 900, 50);

            this.getContentPane().setLayout(null);
            this.getContentPane().add(titleLabel);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(2, 2, 10, 10));
            buttonPanel.setBounds(300, 500, 300, 100);

            this.localBtn = new JButton("本地游戏");
            this.localBtn.setFont(new Font("GB18030 Bitmap", Font.BOLD, 18));
            this.createRoomBtn = new JButton("创建房间");
            this.createRoomBtn.setFont(new Font("GB18030 Bitmap", Font.BOLD, 18));
            this.enterRoomBtn = new JButton("加入房间");
            this.enterRoomBtn.setFont(new Font("GB18030 Bitmap", Font.BOLD, 18));
            this.exitBtn = new JButton("退出游戏");
            this.exitBtn.setFont(new Font("GB18030 Bitmap", Font.BOLD, 18));

            this.localBtn.addActionListener(new BtnListener(this));
            this.localBtn.setActionCommand("Local");
            this.createRoomBtn.addActionListener(new BtnListener(this));
            this.createRoomBtn.setActionCommand("CreateRoom");
            this.enterRoomBtn.addActionListener(new BtnListener(this));
            this.enterRoomBtn.setActionCommand("EnterRoom");
            this.exitBtn.addActionListener(new BtnListener(this));
            this.exitBtn.setActionCommand("Exit");

            buttonPanel.add(localBtn);
            buttonPanel.add(createRoomBtn);
            buttonPanel.add(enterRoomBtn);
            buttonPanel.add(exitBtn);

            this.getContentPane().add(buttonPanel);

        } catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    

}
