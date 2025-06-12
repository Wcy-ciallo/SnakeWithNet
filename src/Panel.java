import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class Panel extends JPanel{
    
    public Panel() {

    }

    public void paintComponent(Graphics g) {
        super.paint(g);
        this.setBackground(Color.GREEN);
    }
}
