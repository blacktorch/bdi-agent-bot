
import actions.Move;
import connectivity.Message;
import connectivity.RobotServer;
import utils.ConnectionUtils;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class Main implements Observer {

    public static void main(String[] args) throws  IOException {


        RobotServer server = new RobotServer("192.168.1.99", 3480);
        Main main = new Main();
        server.addObserver(main);
        server.start();


    }

    @Override
    public void update(Observable o, Object arg) {
        Message message = (Message)arg;
        try {
            message.getOut().write(ConnectionUtils.shiftDataToSend("ack", ConnectionUtils.BUFFER_SIZE, ConnectionUtils.LEFT), 0, ConnectionUtils.BUFFER_SIZE);
            message.getOut().flush();
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
