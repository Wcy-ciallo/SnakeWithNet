package singleVersion;

import java.awt.Point;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.SwingUtilities;

import gameControl.GameInfo;
import gameControl.Snake;
import gameGUI.*;


public class TimeListener implements ActionListener{
    private GameInfo gameInfo;
    private boolean isRemoted;
    private boolean isStarted;
    private boolean isFailed;
    private BoardPanel boardPanel;

    public TimeListener() {
        this.isStarted = false;
        this.isFailed = false;
        this.isRemoted = false;
    }

    public void setBoardPanel (BoardPanel boardPanel) {
        this.boardPanel = boardPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gameInfo = GameInfo.getInstance();

        if(isFailed || gameInfo.isPaused()) {
            if(boardPanel != null) {
                boardPanel.repaint();
            }
            return;
        }

        Point food = gameInfo.getFood();
        if(isStarted && !isFailed) {
            Snake snake1 = gameInfo.getSnake1();
            int len = snake1.body.size();
            for(int i = len - 1; i > 0; i--) {
                snake1.body.set(i, new Point(snake1.body.get(i-1)));
            }
            updateDir(snake1);
            if(snake1.body.get(0).equals(food)) {
                snake1.body.addLast(new Point(snake1.body.getLast()));
                gameInfo.updataFood();
            }

            if(isRemoted) {
                Snake snake2 = gameInfo.getSnake2();
                len = snake2.body.size();
                for(int i = len - 1; i > 0; i--) {
                    snake2.body.set(i, new Point(snake2.body.get(i - 1)));
                    updateDir(snake2);
                }
                if(snake2.body.get(0).equals(food)) {
                    snake2.body.addLast(new Point(snake2.body.getLast()));
                    gameInfo.updataFood();
                }
            }
            this.isFailed = collision1(snake1);
            if(isRemoted && !isFailed) {
                Snake snake2 = gameInfo.getSnake2();
                this.isFailed = collision1(snake2);
                if(!this.isFailed) this.isFailed = collision2(snake1, snake2);
            }
            this.boardPanel.repaint();
        }
        this.boardPanel.timerStart();
    }
    
    private Snake updateDir(Snake snake) {
        String dir = snake.getDirection();
        int headx = snake.body.get(0).x;
        int heady = snake.body.get(0).y;
        switch(dir) {
            case "R":
                headx += 25;
                if(headx > 850) headx = 25;
                break;
            case "L":
                headx -= 25;
                if(headx < 25) headx = 850;
                break;
            case "U":
                heady -= 25;
                if(heady < 75) heady = 650;
                break;
            case "D":
                heady += 25;
                if(heady > 650) heady = 75;
                break;
        }
        snake.body.set(0, new Point(headx, heady));
        return snake;
    }
    private boolean collision1 (Snake snake) {
        int len = snake.body.size();
        for(int i = 1; i < len; i++) {
            if(snake.body.get(i).equals(snake.body.get(0))) {
                return true;
            }
        }
        return false;
    }
    private boolean collision2(Snake snake1, Snake snake2) {
        int len1 = snake1.body.size(), len2 = snake2.body.size();
        for(int i = 1; i < len2; i++) {
            if(snake2.body.get(i).equals(snake1.body.getFirst())) return true;
        }
        for(int i = 1; i < len1; i++) {
            if(snake1.body.get(i).equals(snake2.body.getFirst())) return true;
        }
        return false;
    }
    public void setRemote (boolean isRemoted) {
        this.isRemoted = isRemoted;
    }

    public void setStarted (boolean isStarted) {
        this.isStarted = isStarted;
    }

    private void checkGameOver(Snake snake1) {
        if(collision1(snake1)) {
            this.isFailed = true;
            GameInfo.getInstance().setGameOver(true);

            int finalScore = snake1.body.size() - 3;

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    JFrame gameFrame = (JFrame) SwingUtilities.getWindowAncestor(boardPanel);
                    if(gameFrame instanceof GameGUI) {
                        GameGUI gameGUI = (GameGUI) gameFrame;
                        OverGUI overGUI = new OverGUI(finalScore, gameGUI, ((GameGUI) gameFrame).getStartupGUI());
                        overGUI.setVisible(true);

                        gameFrame.setVisible(false);
                    }
                }
            });
        }
    }
}
