package gameNetwork;

import gameControl.*;

import java.io.*;
import java.net.*;
import java.awt.Point;
import javax.swing.Timer;

public class GameServer {
    private ServerSocket serverSocket;
    private GameSocket clientSocket;
    private GameInfo gameInfo;
    private boolean isRunning;
    private enum ServerState { WAITING, CONNECTED, PLAYING, GAME_OVER }
    private ServerState state;
    private Timer sendTimer;
    
    public GameServer(int port) throws IOException {
        this.state = ServerState.WAITING;
        this.serverSocket = new ServerSocket(port);
        this.serverSocket.setSoTimeout(500); 
        this.isRunning = true;
        this.gameInfo = GameInfo.getInstance();
    }
    
    public void start() {
        Thread serverThread = new Thread(() -> {
            System.out.println("服务器已启动，等待客户端连接...");
            
            while (isRunning && state == ServerState.WAITING) {
                try {
                    Socket clientSocketRaw = null;
                    try {
                        clientSocketRaw = serverSocket.accept();
                    } catch (SocketTimeoutException e) {
                        // 超时继续循环
                        continue;
                    }
                    
                    // 客户端连接成功
                    System.out.println("客户端已连接: " + clientSocketRaw.getInetAddress());
                    clientSocket = new GameSocket(clientSocketRaw);
                    state = ServerState.CONNECTED;
                    
                    // 向客户端发送连接确认
                    clientSocket.sendMessage("CONNECTED");
                    
                    // 等待客户端准备就绪
                    waitForClientReady();
                    
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("服务器错误: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        
        serverThread.start();
    }
    
    private void waitForClientReady() throws IOException, ClassNotFoundException {
        String message = clientSocket.receiveMessage();
        if ("READY".equals(message)) {
            System.out.println("客户端已准备就绪，游戏开始");
            startGame();
        }
    }
    
    private void startGame() {
        state = ServerState.PLAYING;
        
        gameInfo.resetGame();
        
        startSendingGameInfo();
        
        startReceivingClientInput();
    }
    
    
    private void startSendingGameInfo() {
        sendTimer = new Timer(50, e -> {
            if (state == ServerState.PLAYING) {
                try {
                    // 添加游戏逻辑更新
                    synchronized(gameInfo) {
                        updateGameLogic();  // 新增：更新游戏状态
                        clientSocket.sendGameInfo(gameInfo);
                    }
                } catch (IOException ex) {
                    System.err.println("发送游戏状态失败: " + ex.getMessage());
                    if (ex.getMessage() != null && 
                        (ex.getMessage().contains("Broken pipe") || 
                        ex.getMessage().contains("Connection reset"))) {
                        state = ServerState.GAME_OVER;
                        ((Timer)e.getSource()).stop();
                    }
                }
            } else if (state == ServerState.GAME_OVER) {
                try {
                    clientSocket.sendMessage("GAME_OVER");
                    ((Timer)e.getSource()).stop();
                } catch (IOException ex) {
                    ((Timer)e.getSource()).stop();
                }
            }
        });
        sendTimer.start();
    }

    // 新增：游戏逻辑更新方法
    private void updateGameLogic() {
        if (!gameInfo.isPaused() && !gameInfo.isGameOver()) {
            // 移动蛇1（服务器端玩家）
            Snake snake1 = gameInfo.getSnake1();
            if (snake1 != null) {
                moveSnake(snake1);
                checkFood(snake1);
            }
            
            // 移动蛇2（客户端玩家）
            Snake snake2 = gameInfo.getSnake2();
            if (snake2 != null) {
                moveSnake(snake2);
                checkFood(snake2);
            }
            
            // 检查碰撞
            checkCollisions();
        }
    }

    // 移动蛇的逻辑
    private void moveSnake(Snake snake) {
        if (snake.body.isEmpty()) return;
        
        Point head = new Point(snake.body.getFirst());
        String direction = snake.getDirection();
        
        switch (direction) {
            case "U": head.y -= 25; break;
            case "D": head.y += 25; break;
            case "L": head.x -= 25; break;
            case "R": head.x += 25; break;
        }
        
        snake.body.addFirst(head);
        snake.body.removeLast(); // 移除尾部（除非吃到食物）
    }

    // 检查是否吃到食物
    private void checkFood(Snake snake) {
        Point head = snake.body.getFirst();
        Point food = gameInfo.getFood();
        
        if (head.equals(food)) {
            // 蛇长大（不移除尾部）
            snake.body.addLast(new Point(snake.body.getLast()));
            // 生成新食物
            gameInfo.updataFood();
        }
    }

    // 检查碰撞
    private void checkCollisions() {
        Snake snake1 = gameInfo.getSnake1();
        Snake snake2 = gameInfo.getSnake2();
        
        // 检查蛇1自身碰撞和边界碰撞
        if (checkSelfCollision(snake1) || checkBoundaryCollision(snake1)) {
            gameInfo.setGameOver(true);
            return;
        }
        
        // 检查蛇2自身碰撞和边界碰撞
        if (checkSelfCollision(snake2) || checkBoundaryCollision(snake2)) {
            gameInfo.setGameOver(true);
            return;
        }
        
        // 检查两蛇相撞
        if (checkSnakeCollision(snake1, snake2)) {
            gameInfo.setGameOver(true);
        }
    }

    private boolean checkSelfCollision(Snake snake) {
        if (snake.body.size() < 2) return false;
        Point head = snake.body.getFirst();
        for (int i = 1; i < snake.body.size(); i++) {
            if (head.equals(snake.body.get(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean checkBoundaryCollision(Snake snake) {
        Point head = snake.body.getFirst();
        return head.x < 25 || head.x >= 25 + 25*34 || 
            head.y < 75 || head.y >= 75 + 25*24;
    }

    private boolean checkSnakeCollision(Snake snake1, Snake snake2) {
        Point head1 = snake1.body.getFirst();
        Point head2 = snake2.body.getFirst();
        
        // 检查头部相撞
        if (head1.equals(head2)) return true;
        
        // 检查蛇1头部撞到蛇2身体
        for (Point p : snake2.body) {
            if (head1.equals(p)) return true;
        }
        
        // 检查蛇2头部撞到蛇1身体
        for (Point p : snake1.body) {
            if (head2.equals(p)) return true;
        }
        
        return false;
    }
    
    
    private void startReceivingClientInput() {
        Thread receiveThread = new Thread(() -> {
            while (isRunning && state == ServerState.PLAYING) {
                try {
                    String input = clientSocket.receiveMessage();

                    if (input.startsWith("KEY:")) {
                        String direction = input.substring(4);
                        Snake snake2 = gameInfo.getSnake2();
                        if (snake2 != null) {
                            snake2.setDirection(direction);
                        }
                    } else if ("PAUSE".equals(input)) {
                        gameInfo.setPaused(!gameInfo.isPaused());
                    } else if ("QUIT".equals(input)) {
                        state = ServerState.GAME_OVER;
                    }
                    
                    if (gameInfo.isGameOver()) {
                        state = ServerState.GAME_OVER;
                    }
                    
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("接收客户端数据失败: " + e.getMessage());
                    break;
                }
            }
        });
        
        receiveThread.start();
    }
    
    
    public void stopServer() {
        isRunning = false;
        if (sendTimer != null) {
            sendTimer.stop();
        }
        
        try {
            if (clientSocket != null) {
                clientSocket.close();
            }
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public int getPort() {
        return serverSocket.getLocalPort();
    }
    
    
    public String getState() {
        return state.toString();
    }
}