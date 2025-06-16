import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import javax.sound.sampled.*;


public class Panel extends JPanel implements KeyListener, ActionListener {
    ImageIcon title = new ImageIcon("src/resource/img/title.jpg");
    ImageIcon body = new ImageIcon("src/resource/img/body.png");
    ImageIcon up = new ImageIcon("src/resource/img/up.png");
    ImageIcon down = new ImageIcon("src/resource/img/down.png");
    ImageIcon left = new ImageIcon("src/resource/img/left.png");
    ImageIcon right = new ImageIcon("src/resource/img/right.png");
    ImageIcon food = new ImageIcon("src/resource/img/food.png");
    int len = 3;
    int score = 0;
    int[] snakex = new int[750];
    int[] snakey = new int[750];
    String fx = "R"; //fx: R, L, U, D
    boolean isStarted = false;
    boolean isFailed = false;
    Timer timer = new Timer(200, this);
    int foodx, foody;
    Random random = new Random();
    Clip bgm;

    public Panel() {
        initSnake();
        this.setFocusable(true);
        this.addKeyListener(this);
        timer.start();
        loadBgm();
                System.out.println(getClass().getResource("/resource/sound/bgm.wav") + " good");
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.WHITE);
        title.paintIcon(this, g, 25, 11);

        g.fillRect(25, 75, 25*34, 25*24);
        g.setColor(Color.WHITE);
        g.drawString("Len: " + len, 750, 35);
        g.drawString("Score: " + score, 750, 50);
        if(fx == "R") {
            right.paintIcon(this, g, snakex[0], snakey[0]);
        } else if(fx == "L") {
            left.paintIcon(this, g, snakex[0], snakey[0]);
        } else if(fx == "U") {
            up.paintIcon(this, g, snakex[0], snakey[0]);
        } else if(fx == "D") {
            down.paintIcon(this, g, snakex[0], snakey[0]);
        }
        for(int i = 1; i < len; i++) {
            body.paintIcon(this, g, snakex[i], snakey[i]);
        }
        food.paintIcon(this, g, foodx, foody);

        if(!isStarted && !isFailed) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("arial", Font.BOLD, 40));
            g.drawString("Press <Space> to Start!", 300, 300); 
        } else if(isFailed) {
            g.setColor(Color.RED);
            g.setFont(new Font("arial", Font.BOLD, 40));
            g.drawString("Press <Space> to Restart!", 200, 300);
        }
   }

    public void initSnake() {
        this.len = 3;
        snakex[0] = 100;
        snakey[0] = 100;
        snakex[1] = 75;
        snakey[1] = 100;
        snakex[2] = 50;
        snakey[2] = 100;
        foodx = 25 + 25 * random.nextInt(34);
        foody = 75 + 25 * random.nextInt(24);
        fx = "R";
        score = 0;
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_SPACE) {
            if(isFailed) {
                isFailed = false;
                initSnake();
            } else {
                isStarted = !isStarted;
            }
            if(isStarted) {
                playBgm();
            } else {
                stopBgm();
            }
            repaint();
        } else if(keyCode == KeyEvent.VK_DOWN && fx != "U") {
            fx = "D";
        } else if(keyCode == KeyEvent.VK_UP && fx != "D") {
            fx = "U";
        } else if(keyCode == KeyEvent.VK_LEFT && fx != "R") {
            fx = "L";
        } else if(keyCode == KeyEvent.VK_RIGHT && fx != "L") {
            fx = "R";
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(isStarted && !isFailed) {
             for(int i = len - 1; i > 0; i--) {
                snakex[i] = snakex[i - 1];
                snakey[i] = snakey[i - 1];
            }
            if(fx == "R") {
                snakex[0] += 25;
                if(snakex[0] > 850) snakex[0] = 25;
            } else if(fx == "L") {
                snakex[0] -= 25;
                if(snakex[0] < 25) {
                    snakex[0] = 850;
                }
            } else if(fx == "U") {
                snakey[0] -= 25;
                if(snakey[0] < 75) {
                    snakey[0] = 650;
                }
            } else if(fx == "D") {
                snakey[0] += 25;
                if(snakey[0] > 650) {
                    snakey[0] = 75;
                }
            }

            if(snakex[0] == foodx && snakey[0] == foody) {
                len++;
                score += 10;
                foodx = 25 + 25 * random.nextInt(34);
                foody = 75 + 25 * random.nextInt(24);
            }
            for(int i = 1; i < len; i++) {
                if(snakex[i] == snakex[0] && snakey[i] == snakey[0]) {
                    isFailed = true;
                    break;
                }
            }
            repaint();
        }
        timer.start();
    }

    private void loadBgm() {
        try{
            bgm = AudioSystem.getClip();
            AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResource("/resource/sound/bgm.wav"));
            bgm.open(ais);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void playBgm() {
        bgm.loop(Clip.LOOP_CONTINUOUSLY);
    }

    private void stopBgm() {
        bgm.stop();
    }
}
