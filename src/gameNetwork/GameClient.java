package gameNetwork;

import gameControl.*;
import gameGUI.*;

import java.io.*;
import java.net.*;
import javax.swing.*;

public class GameClient {
    private GameSocket serverSocket;
    private GameInfo gameInfo;
    private boolean isConnected;
    private boolean isRunning;
    private enum ClientState { CONNECTING, READY, PLAYING, GAME_OVER }
    private ClientState state;
    private GameGUI gameGUI;
    
    public GameClient(String serverIP, int serverPort) {
        this.gameInfo = GameInfo.getInstance();
        this.isConnected = false;
        this.isRunning = false;
        this.state = ClientState.CONNECTING;
        connect(serverIP, serverPort);
    }
    
    private void connect(String serverIP, int serverPort) {
        Thread connectThread = new Thread(() -> {
            try {
                System.out.println("连接服务器: " + serverIP + ":" + serverPort);
                Socket socket = new Socket(serverIP, serverPort);
                serverSocket = new GameSocket(socket);
                isConnected = true;
                
                String response = serverSocket.receiveMessage();
                if ("CONNECTED".equals(response)) {
                    System.out.println("成功连接到服务器");
                    state = ClientState.READY;
                    
                    // 通知服务器客户端已准备就绪
                    serverSocket.sendMessage("READY");
                    
                    // 开始游戏
                    startGame();
                }
                
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("连接服务器失败: " + e.getMessage());
                isConnected = false;
            }
        });
        
        connectThread.start();
    }
    
    private void startGame() {
        state = ClientState.PLAYING;
        isRunning = true;
        startReceivingGameInfo();
    }
    
    public void setGameGUI(GameGUI gameGUI) {
        this.gameGUI = gameGUI;
    }
    
    private void startReceivingGameInfo() {
        Thread receiveThread = new Thread(() -> {
            int failCount = 0;
            while (isRunning && isConnected && failCount < 3) { // 添加失败计数器
                try {
                    try {
                        GameInfo receivedInfo = serverSocket.receiveGameInfo();
                        failCount = 0;
                        
                        synchronized (gameInfo) {
                            gameInfo.syncFrom(receivedInfo);
                        }
                        
                        if (gameGUI != null && gameGUI.getBoardPanel() != null) {
                            gameGUI.getBoardPanel().repaint();
                        }
                        
                        if (gameInfo.isGameOver()) {
                            state = ClientState.GAME_OVER;
                            showGameOverDialog();
                            break;
                        }
                    } catch (ClassCastException e) {
                        System.err.println("接收客户端数据失败: " + e.getMessage());
                        if (e.getMessage() != null && 
                        (e.getMessage().contains("Connection reset") || 
                            e.getMessage().contains("Socket closed"))) {
                            break; // 连接确实断开才退出
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    failCount++; // 增加失败计数
                    System.err.println("接收服务器数据失败 (尝试 " + failCount + "/3): " + e.getMessage());
                    
                    try {
                        Thread.sleep(1000); // 失败后等待一秒再重试
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                    
                    if (failCount >= 3) {
                        System.err.println("连续多次接收失败，断开连接");
                        isConnected = false;
                        showConnectionErrorDialog();
                    }
                }
            }
            
            System.out.println("接收线程结束");
        });
        
        receiveThread.start();
    }

    private void showConnectionErrorDialog() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(gameGUI, 
                    "与服务器的连接已断开，请重新连接", 
                    "连接错误", 
                    JOptionPane.ERROR_MESSAGE);
            
            // 返回主菜单
            if (gameGUI != null) {
                gameGUI.dispose();
                if (gameGUI.getStartupGUI() != null) {
                    gameGUI.getStartupGUI().setVisible(true);
                }
            }
        });
    }
    
    public void sendKeyEvent(String direction) {
        if (isConnected && state == ClientState.PLAYING) {
            try {
                serverSocket.sendMessage("KEY:" + direction);
            } catch (IOException e) {
                System.err.println("发送按键事件失败: " + e.getMessage());
                isConnected = false;
            }
        }
    }
    
    public void sendPauseRequest() {
        if (isConnected && state == ClientState.PLAYING) {
            try {
                serverSocket.sendMessage("PAUSE");
            } catch (IOException e) {
                System.err.println("发送暂停请求失败: " + e.getMessage());
            }
        }
    }
    
    public void sendQuitRequest() {
        if (isConnected) {
            try {
                serverSocket.sendMessage("QUIT");
                disconnect();
            } catch (IOException e) {
                System.err.println("发送退出请求失败: " + e.getMessage());
            }
        }
    }
    
    public void disconnect() {
        isRunning = false;
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        isConnected = false;
    }
    
    private void showGameOverDialog() {
        SwingUtilities.invokeLater(() -> {
            if (gameGUI != null) {
                int score = gameInfo.getSnake1().body.size() - 3;
                OverGUI overGUI = new OverGUI(score, gameGUI, gameGUI.getStartupGUI());
                overGUI.setVisible(true);
                gameGUI.setVisible(false);
            }
        });
    }
    
    public boolean isConnected() {
        return isConnected;
    }
    
    public String getState() {
        return state.toString();
    }
}