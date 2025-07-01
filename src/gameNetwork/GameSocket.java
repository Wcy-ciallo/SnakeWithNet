package gameNetwork;

import gameControl.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class GameSocket {
    private Socket socket;
    private ObjectOutputStream objectOutputStream = null;
    private ObjectInputStream objectInputStream = null;

    GameSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
        this.objectInputStream = new ObjectInputStream(this.socket.getInputStream());
    }

    String receiveMessage() throws IOException, ClassNotFoundException {
        return (String)this.objectInputStream.readObject();
    }

    public GameInfo receiveGameInfo() throws IOException, ClassNotFoundException {
        String message = (String)this.objectInputStream.readObject();
        if (message == null) {
            throw new IOException("接收到的消息为null");
        }
        
        try {
            byte[] data = Base64.getDecoder().decode(message);
            ObjectInputStream objInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
            GameInfo gameInfo = (GameInfo)objInputStream.readObject();
            objInputStream.close();
            return gameInfo;
        } catch (Exception e) {
            throw new IOException("反序列化GameInfo失败: " + e.getMessage(), e);
        }
    }

    synchronized public void sendMessage(String message) throws IOException {
        this.objectOutputStream.writeObject(message);
        this.objectOutputStream.flush();
    }

    synchronized public void sendGameInfo(GameInfo gameInfo) throws IOException {
        if (gameInfo == null) {
            throw new IOException("GameInfo对象为null");
        }
        
        String message;
        synchronized (gameInfo) {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objOutputStream.writeObject(gameInfo);
                objOutputStream.close();
                message = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
            } catch (Exception e) {
                throw new IOException("序列化GameInfo失败: " + e.getMessage(), e);
            }
        }
        
        // 检查连接是否已关闭
        if (socket.isClosed()) {
            throw new IOException("Socket已关闭");
        }
        
        try {
            this.objectOutputStream.writeObject(message);
            this.objectOutputStream.flush();
        } catch (Exception e) {
            throw new IOException("发送数据失败: " + e.getMessage(), e);
        }
    }

    synchronized public void close() throws IOException {
        try {
            if (this.objectOutputStream != null) {
                this.objectOutputStream.flush();
                this.objectOutputStream.close();
            }
        } catch (IOException e) {
            // 记录错误但继续关闭其他资源
        }
        
        try {
            if (this.objectInputStream != null) {
                this.objectInputStream.close();
            }
        } catch (IOException e) {
            // 记录错误但继续关闭
        }
        
        if (this.socket != null && !this.socket.isClosed()) {
            this.socket.close();
        }
    }

}
