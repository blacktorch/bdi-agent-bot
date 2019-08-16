package connectivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class RobotServer {
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
            while (true){
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    private static class ClientHandler extends Thread{
        private Socket clientSocket;
        private PrintWriter out;
        private byte[] in;

        public ClientHandler(Socket socket){
            this.clientSocket = socket;
            in = new byte[1024];
        }

        public void run() {

            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                while (true) {
                    clientSocket.getInputStream().read(in, 0, 1024);
                    inputLine = new String(in);
                    System.out.println(inputLine.trim());
                    if (".".equals(inputLine.trim())) {
                        out.println("bye");
                        break;
                    }
                    out.println(inputLine.trim());

                }

                out.close();
                clientSocket.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }


}
