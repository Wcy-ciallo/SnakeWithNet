package gameControl;

import java.util.Random;
import java.awt.Point;

public class GameInfo {
    private static GameInfo instance;
    Snake snake1, snake2;
    Random random;
    Point food;
    private boolean isPaused = false;
    private boolean isGameOver = false;

    private GameInfo() {
        this.random = new Random();
        this.snake1 = new Snake(25 + 5 * 25, 700 - 25 - 5 * 25);
        this.snake2 = new Snake(900 - 25 - 5 * 25, 700 - 25 - 5 * 25);
        this.food = new Point();
        this.food.x = 25 + 25 * random.nextInt(34);
        this.food.y = 75 + 25 * random.nextInt(24);

    }

    public static synchronized GameInfo getInstance() {
        if(instance == null) {
            instance = new GameInfo();
        }
        return instance;
    }

    public Snake getSnake1() {
        return this.snake1;
    }

    public Snake getSnake2() {
        return this.snake2;
    }

    public Point getFood() {
        return this.food;
    }

    public void updataFood() {
        this.food.x = 25 + 25 * random.nextInt(34);
        this.food.y = 75 + 25 * random.nextInt(24);
    }

    public boolean isPaused() {
        return this.isPaused;
    }
    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }
    public boolean isGameOver() {
        return this.isGameOver;
    }
    public void setGameOver(boolean gameOver) {
        this.isGameOver = gameOver;
    }

    public void resetGame() {
        this.snake1 = new Snake(25 + 5 * 25, 700 - 25 - 5 * 25);
        this.snake2 = new Snake(900 - 25 - 5 * 25, 700 - 25 - 5 * 25);
        this.food.x = 25 + 25 * random.nextInt(34);
        this.food.y = 75 + 25 * random.nextInt(24);
        this.isGameOver = false;
        this.isPaused = false;
    }
}
