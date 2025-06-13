import java.awt.Color;
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

    public Panel() {

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.WHITE);
        title.paintIcon(this, g, 25, 11);

        g.fillRect(25, 75, 25*34, 25*24);
        right.paintIcon(this, g, 100, 100);
        body.paintIcon(this, g, 75, 100);
        body.paintIcon(this, g, 50, 100);
    }
}
