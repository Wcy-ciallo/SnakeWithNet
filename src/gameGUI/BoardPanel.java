package gameGUI;

import gameControl.*;

import java.awt.*;
import java.util.HashMap;
import javax.swing.Timer;
import javax.swing.JPanel;
import javax.swing.ImageIcon;

public class BoardPanel extends JPanel{
    boolean isRemoted;
    GameInfo gameInfo;
    ImageIcon title, body_1, body_2, food;
    ImageIcon[] head = new ImageIcon[4];
    HashMap<String, Integer> dtoh = new HashMap<>();
    Timer timer;
    TimeListener timeListener;

    public BoardPanel (boolean isRemoted) {
        this.isRemoted = isRemoted;
        this.gameInfo = GameInfo.getInstance();
        this.dtoh.put("U", 0);
        this.dtoh.put("D", 1);
        this.dtoh.put("L", 2);
        this.dtoh.put("R", 3);
        this.timeListener = new TimeListener(false);
        this.timeListener.setBoardPanel(this);
        this.timeListener.setRemote(isRemoted);
        this.timer = new Timer(200, this.timeListener);
        this.timer.start();
        this.setFocusable(true);
        this.addKeyListener(new KListener());
        this.requestFocus(); 
        loadImage();
    }

    public void paintComponent(Graphics g) {
        this.gameInfo = GameInfo.getInstance();
        super.paintComponent(g);
        this.setBackground(Color.WHITE);
        title.paintIcon(this, g, 25, 11);

        g.fillRect(25, 75, 25*34, 25*24);
        g.setColor(Color.WHITE);
        Snake snake1 = gameInfo.getSnake1();
        head[dtoh.get(snake1.getDirection())].paintIcon(this, g, snake1.body.get(0).x, snake1.body.get(0).y);
        for(var p : snake1.body) {
            if(p == snake1.body.get(0)) continue;
            body_1.paintIcon(this, g, p.x, p.y);
        }
        if(isRemoted) {
            Snake snake2 = gameInfo.getSnake2();
            head[dtoh.get(snake2.getDirection())].paintIcon(this, g, snake2.body.get(0).x, snake2.body.get(0).y);
            for(var p : snake2.body) {
                if(p == snake2.body.get(0)) continue;
                body_2.paintIcon(this, g, p.x, p.y);
            }
        }
        Point f = gameInfo.getFood();
        this.food.paintIcon(this, g, f.x, f.y);
        
        if(gameInfo.isPaused()) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.WHITE);
            g.setFont(new Font("微软雅黑", Font.BOLD, 36));
            String pauseText = "游戏已暂停";
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(pauseText);
            g.drawString(pauseText, (getWidth() - textWidth) / 2, getHeight() / 2);

            g.setFont(new Font("微软雅黑", Font.BOLD, 18));
            String tipText = "按 ESC 键继续";
            textWidth = g.getFontMetrics().stringWidth(tipText);
            g.drawString(tipText, (getWidth() - textWidth) / 2, getHeight() / 2 + 40);
        }
    }
    
    private void loadImage() {
        this.title = new ImageIcon(getClass().getResource("/resource/img/title.jpg"));
        this.head[0] = new ImageIcon(getClass().getResource("/resource/img/up.png"));
        this.head[1] = new ImageIcon(getClass().getResource("/resource/img/down.png"));
        this.head[2] = new ImageIcon(getClass().getResource("/resource/img/left.png"));
        this.head[3] = new ImageIcon(getClass().getResource("/resource/img/right.png"));
        this.body_1 = new ImageIcon(getClass().getResource("/resource/img/body_1.png"));
        this.body_2 = new ImageIcon(getClass().getResource("/resource/img/body_2.png"));
        this.food = new ImageIcon(getClass().getResource("/resource/img/food.png"));
    }
    public void timerStart() {
        this.timer.start();
    }
    public void timerStop() {
        this.timer.stop();
    }

    public void setNetworkMode(boolean isNetworkMode) {
        this.isRemoted = isNetworkMode;
        
        // 网络模式下停止本地Timer，只接收服务器状态
        if (isNetworkMode && timer != null) {
            timer.stop();
        }
    }
}
