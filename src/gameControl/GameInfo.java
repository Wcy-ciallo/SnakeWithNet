package gameControl;

import java.util.Random;
import java.awt.Point;
import java.io.Serializable;

public class GameInfo implements Serializable{
    private static final long serialVersionUID = 1L;

    private static GameInfo instance;
    Snake snake1, snake2;
    transient Random random;
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

    public static synchronized void resetInstance() {
        instance = new GameInfo();
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

    public synchronized void syncFrom(GameInfo other) {
        if (other == null) return;
        
        // 同步蛇1
        if (other.snake1 != null) {
            if (this.snake1 == null) {
                this.snake1 = new Snake();
            }
            this.snake1.copyFrom(other.snake1);
        }
        
        // 同步蛇2
        if (other.snake2 != null) {
            if (this.snake2 == null) {
                this.snake2 = new Snake();
            }
            this.snake2.copyFrom(other.snake2);
        }
        
        // 同步食物位置（创建新实例而不是共享引用）
        if (other.food != null) {
            this.food = new Point(other.food.x, other.food.y);
        }
        
        // 同步游戏状态标志
        this.isPaused = other.isPaused;
        this.isGameOver = other.isGameOver;
    }
}
