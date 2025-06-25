package gameControl;

import java.awt.event.*;

public class LocalKListener implements KeyListener{

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        GameInfo gameInfo = GameInfo.getInstance();
        
        if(keyCode == KeyEvent.VK_ESCAPE) {
            gameInfo.setPaused(!gameInfo.isPaused());
            System.out.println("游戏" + (gameInfo.isPaused() ? "暂停" : "继续"));
            return;
        }
        if(gameInfo.isPaused() || gameInfo.isGameOver()) {
            return;
        }

        Snake snake1 = gameInfo.getSnake1();
        String dir = snake1.getDirection();
        if(keyCode == KeyEvent.VK_DOWN && dir != "U") {
            snake1.setDirection("D");
        } else if(keyCode == KeyEvent.VK_UP && dir != "D") {
            snake1.setDirection("U");
        } else if(keyCode == KeyEvent.VK_LEFT && dir != "R") {
            snake1.setDirection("L");
        } else if(keyCode == KeyEvent.VK_RIGHT && dir != "L") {
            snake1.setDirection("R");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
}
