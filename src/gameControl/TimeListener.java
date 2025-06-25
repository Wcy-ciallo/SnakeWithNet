package gameControl;

import java.awt.Point;
import java.awt.event.*;
import javax.swing.*;

import gameGUI.*;

public class TimeListener implements ActionListener {
    private GameInfo gameInfo;
    private BoardPanel boardPanel;
    private boolean isRemoted;

    public TimeListener(boolean isRemoted) {
        this.isRemoted = isRemoted;
    }

    public void setBoardPanel(BoardPanel boardPanel) {
        this.boardPanel = boardPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gameInfo = GameInfo.getInstance();
        if(gameInfo.isGameOver()) {
            Snake snake1 = gameInfo.getSnake1();
            showGameOverDialog(snake1);
            this.boardPanel.timerStop();
            return;
        }

        Point food = gameInfo.getFood();
        if (!gameInfo.isPaused()) {
            Snake snake1 = gameInfo.getSnake1();
            updateSnakeBody(snake1);
            
            if (snake1.body.getFirst().equals(food)) {
                snake1.body.addLast(new Point(snake1.body.getLast()));
                gameInfo.updataFood();
            }
            
            if (isRemoted) {
                Snake snake2 = gameInfo.getSnake2();
                updateSnakeBody(snake2);
                
                if (snake2.body.getFirst().equals(food)) {
                    snake2.body.addLast(new Point(snake2.body.getLast()));
                    gameInfo.updataFood();
                }
            }
            
            checkCollisions();
        }
        this.boardPanel.repaint();
    }
    
    private void updateSnakeBody(Snake snake) {
        int len = snake.body.size();
        
        // 从尾部开始，每个节点继承前一个节点的位置
        for (int i = len - 1; i > 0; i--) {
            snake.body.set(i, new Point(snake.body.get(i-1)));
        }
        
        snake = updateDir(snake);
    }
    
    private Snake updateDir(Snake snake) {
        String dir = snake.getDirection();
        Point head = snake.body.getFirst();
        int headx = head.x;
        int heady = head.y;
        
        switch (dir) {
            case "R":
                headx += 25;
                if (headx > 850) headx = 25;
                break;
            case "L":
                headx -= 25;
                if (headx < 25) headx = 850;
                break;
            case "U":
                heady -= 25;
                if (heady < 75) heady = 650;
                break;
            case "D":
                heady += 25;
                if (heady > 650) heady = 75;
                break;
        }
        
        snake.body.set(0, new Point(headx, heady));
        return snake;
    }
    
    private void checkCollisions() {
        Snake snake1 = gameInfo.getSnake1();
        
        boolean collision = collision1(snake1);
        
        if (isRemoted && !collision) {
            Snake snake2 = gameInfo.getSnake2();
            
            collision = collision1(snake2);
            
            if (!collision) {
                collision = collision2(snake1, snake2);
            }
        }
        
        if (collision) {
            GameInfo.getInstance().setGameOver(true);
        }
    }
    
    private boolean collision1(Snake snake) {
        Point head = snake.body.getFirst();
        
        for (int i = 1; i < snake.body.size(); i++) {
            if (snake.body.get(i).equals(head)) {
                return true;
            }
        }
        return false;
    }
    
    // 检查两条蛇之间的碰撞
    private boolean collision2(Snake snake1, Snake snake2) {
        Point head1 = snake1.body.getFirst();
        Point head2 = snake2.body.getFirst();
        
        for (int i = 0; i < snake2.body.size(); i++) {
            if (snake2.body.get(i).equals(head1)) {
                return true;
            }
        }
        
        for (int i = 0; i < snake1.body.size(); i++) {
            if (snake1.body.get(i).equals(head2)) {
                return true;
            }
        }
        
        if (head1.equals(head2)) {
            return true;
        }
        
        return false;
    }
    
    private void showGameOverDialog(Snake snake) {
        int finalScore = snake.body.size() - 3; // 减去初始长度
        
        SwingUtilities.invokeLater(() -> {
            JFrame gameFrame = (JFrame) SwingUtilities.getWindowAncestor(boardPanel);
            if (gameFrame instanceof GameGUI) {
                GameGUI gameGUI = (GameGUI) gameFrame;
                StartupGUI startupGUI = gameGUI.getStartupGUI();
                
                if (startupGUI == null) {
                    System.out.println("警告: startupGUI 为 null，将使用默认处理");
                }
                
                OverGUI overGUI = new OverGUI(finalScore, gameGUI, startupGUI);
                overGUI.setVisible(true);
                gameFrame.setVisible(false);
            }
        });
    }
    
    public void setRemote(boolean isRemoted) {
        this.isRemoted = isRemoted;
    }
}