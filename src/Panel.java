import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.ImageIcon;

public class Panel extends JPanel{
    ImageIcon title = new ImageIcon("resource/img/title.jpg");
    public Panel() {

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.WHITE);
        title.paintIcon(this, g, 25, 11);
    }
}
