package connectivity;

import interfaces.IMessageReceivedListener;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;

public class RobotServer extends Observable implements IMessageReceivedListener {

    ServerSocket serverSocket;
    private String host;
    private int port;
    private InetAddress address;

    public RobotServer(String host, int port) throws UnknownHostException {

        this.host = host;
        this.port = port;
        this.address = InetAddress.getByName(host);
    }

    public void start(){
        try {
            serverSocket = new ServerSocket(port, 50, address);
            ClientHandler clientHandler;
            while (true){
               clientHandler = new ClientHandler(serverSocket.accept());
               clientHandler.setMessageReceivedListener(this);
               clientHandler.start();
            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onReceiveMessage(Message message) {
        setChanged();
        notifyObservers(message);
    }

    private static class ClientHandler extends Thread{
        private Socket clientSocket;
        private OutputStream out;
        private byte[] in;

        private IMessageReceivedListener messageReceivedListener;

        ClientHandler(Socket socket){
            this.clientSocket = socket;
            in = new byte[1024];
        }

        public void setMessageReceivedListener(IMessageReceivedListener listener){
            this.messageReceivedListener = listener;
        }

        public void run() {

            try {
                out = clientSocket.getOutputStream();

                String inputLine;
                while (true) {
                    if (!clientSocket.isConnected()){
                        out.close();
                        clientSocket.close();
                        break;
                    }
                    if (clientSocket.getInputStream().available() > 0){
                        clientSocket.getInputStream().read(in, 0, 1024);
                        inputLine = new String(in);
                        System.out.println(inputLine.trim());
                        messageReceivedListener.onReceiveMessage(new Message(inputLine.trim(), out));
                    }

                }

            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }



}
