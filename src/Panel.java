import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.ImageIcon;

public class Panel extends JPanel{
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

    public Panel() {
        initSnake();
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

        g.setColor(Color.WHITE);
        g.setFont(new Font("arial", Font.BOLD, 40));
        g.drawString("Press <Space> to Start!", 300, 300);
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
}
