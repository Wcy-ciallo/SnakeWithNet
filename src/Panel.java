import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.ImageIcon;


public class Panel extends JPanel implements KeyListener{
    ImageIcon title = new ImageIcon("resource/img/title.jpg");
    ImageIcon body = new ImageIcon("resource/img/body.png");
    ImageIcon up = new ImageIcon("resource/img/up.png");
    ImageIcon down = new ImageIcon("resource/img/down.png");
    ImageIcon left = new ImageIcon("resource/img/left.png");
    ImageIcon right = new ImageIcon("resource/img/right.png");
    ImageIcon food = new ImageIcon("resource/img/food.png");
    int len = 3;
    int[] snakex = new int[750];
    int[] snakey = new int[750];
    String fx = "R"; //fx: R, L, U, D
    boolean isStarted = false;

    public Panel() {
        initSnake();
        this.setFocusable(true);
        this.addKeyListener(this);

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.WHITE);
        title.paintIcon(this, g, 25, 11);

        g.fillRect(25, 75, 25*34, 25*24);
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

        if(!isStarted) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("arial", Font.BOLD, 40));
            g.drawString("Press <Space> to Start!", 300, 300); 
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
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_SPACE) {
            isStarted = !isStarted;
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
