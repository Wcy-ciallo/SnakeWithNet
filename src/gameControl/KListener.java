package gameControl;

import java.awt.event.*;
import gameNetwork.*;

public class KListener implements KeyListener{
    private GameClient gameClient;
    private boolean isNetworkMode;

    public KListener (GameClient gameClient) {
        this.gameClient = gameClient;
        this.isNetworkMode = (gameClient != null);
    }

    public KListener() {
        this.gameClient = null;
        this.isNetworkMode = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        GameInfo gameInfo = GameInfo.getInstance();
        
        if(keyCode == KeyEvent.VK_ESCAPE) {
            gameInfo.setPaused(!gameInfo.isPaused());
            
            if(isNetworkMode && gameClient != null) {
                gameClient.sendPauseRequest();
            }

            System.out.println("游戏" + (gameInfo.isPaused() ? "暂停" : "继续"));
            return;
        }

        if(gameInfo.isPaused() || gameInfo.isGameOver()) {
            return;
        }

        String direction = null;

        if(keyCode == KeyEvent.VK_DOWN)  {
            direction = "D";
        } else if(keyCode == KeyEvent.VK_UP) {
            direction = "U";
        } else if(keyCode == KeyEvent.VK_LEFT) {
            direction = "L";
        } else if(keyCode == KeyEvent.VK_RIGHT) {
            direction = "R";
        }

        if(direction != null) {
            Snake snake1 = gameInfo.getSnake1();
            String dir = snake1.getDirection();

            boolean isValidDirection = 
                (direction.equals("D") && !dir.equals("U")) ||
                (direction.equals("U") && !dir.equals("D")) ||
                (direction.equals("L") && !dir.equals("R")) ||
                (direction.equals("R") && !dir.equals("L"));
            if(isValidDirection) {
                if(isNetworkMode && gameClient != null) {
                    gameClient.sendKeyEvent(direction);
                } else {
                    snake1.setDirection(direction);
                }
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
    public void setGameClient(GameClient gameClient) {
        this.gameClient = gameClient;
        this.isNetworkMode = (gameClient != null);
    }
}
