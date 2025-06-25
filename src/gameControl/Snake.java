package gameControl;

import java.util.LinkedList;
import java.awt.Point;

public class Snake {
    final String[] Direction = {"U", "D", "L", "R"};
    private String selfDirection;
    public LinkedList<Point> body;

    public Snake(int startx, int starty) {
        this.selfDirection = this.Direction[0];
        body = new LinkedList<>();
        body.add(new Point(startx, starty));
        body.add(new Point(startx, starty + 25));
        body.add(new Point(startx, starty + 50));
    }

    public void setDirection(String Direction) {
        this.selfDirection = Direction;
    }

    public String getDirection() {
        return this.selfDirection;
    }
}
