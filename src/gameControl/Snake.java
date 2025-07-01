package gameControl;

import java.util.LinkedList;
import java.awt.Point;
import java.io.Serializable;

public class Snake implements Serializable{
    private static final long serialVersionUID = 1L;

    final String[] Direction = {"U", "D", "L", "R"};
    private String selfDirection;
    public LinkedList<Point> body;

    public Snake() {
        this.body = new LinkedList<>();
        this.selfDirection = "R";
    }

    public Snake(int startx, int starty) {
        this.selfDirection = this.Direction[0];
        body = new LinkedList<>();
        body.add(new Point(startx, starty));
        body.add(new Point(startx, starty + 25));
        body.add(new Point(startx, starty + 50));
    }

    public synchronized void copyFrom(Snake other) {
        if(other == null) return;

        this.selfDirection = other.selfDirection;
        this.body = new LinkedList<>();
        for(Point p : other.body) {
            this.body.add(new Point(p.x, p.y));
        }
    }

    public void setDirection(String Direction) {
        this.selfDirection = Direction;
    }

    public String getDirection() {
        return this.selfDirection;
    }
}
