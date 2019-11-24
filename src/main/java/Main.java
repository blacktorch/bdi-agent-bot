
import actions.Move;
import actuators.LeftMotor;
import actuators.Servo;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import connectivity.Message;
import connectivity.RobotServer;
import constants.ActuatorConstants;
import constants.SensorConstants;
import sensors.Odometry;
import sensors.UltrasonicSensor;
import utils.ActuatorUtil;
import utils.ConnectionUtils;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import static utils.TimeUtil.delay;

public class Main implements Observer {


    //static Servo tire = new Servo(ActuatorConstants.TILT_SERVO_CHANNEL);
    public static void main(String[] args) throws IOException {

        Odometry odometry = new Odometry(1920);
        odometry.setup();


        Move.forward(100);
        delay(8000);
        Move.stopEngine();

//        while(true){
//
//        }


//        //tire.travel(120, 1000);
//        Move.startEngine();
//        Move.forward(50);
//        delay(2000);
//        Move.stopEngine();

//        sensors.UltrasonicSensor sonic =new sensors.UltrasonicSensor(
//                5,//ECO PIN (physical 16)
//                4,//TRIG PIN (physical 18)
//                1000,//REJECTION_START ; long (nano seconds)
//                23529411 //REJECTION_TIME ; long (nano seconds)
//        );
//        System.out.println("Start");
//        while(true){
//            try {
//                System.out.println("distance "+ sonic.getDistance()+"mm");
//                Thread.sleep(100); //1s
//            } catch (Exception ex){
//                ex.printStackTrace();
//            }
//
//        }
//


        //tire.writeAngle(140);


    }

    private void reply(String msg, Message message) {
        try {
            message.getOut().write(ConnectionUtils.shiftDataToSend(msg, ConnectionUtils.BUFFER_SIZE, ConnectionUtils.LEFT), 0, ConnectionUtils.BUFFER_SIZE);
            message.getOut().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        Message message = (Message) arg;
        String[] data = message.getMessage().split(" ");
        if (data.length > 0) {
            switch (data[0]) {
                case "drive":
                    if (data[1].startsWith("-")) {
                        Move.backward(Integer.parseInt(data[1].replace("-", "")));
                    } else {
                        Move.forward(Integer.parseInt(data[1]));
                    }
                    reply("drive successful", message);
                    break;
                case "brake":
                    Move.stopEngine();
                    reply("brake successful", message);
                    break;
                case "steer":
                    if (data[1].startsWith("-")) {
                        Move.turnLeft(Integer.parseInt(data[1].replace("-", "")), 100);
                    } else {
                        Move.turnRight(Integer.parseInt(data[1]), 100);
                    }
                    reply("steer successful", message);
                    break;
                case "tilt":
                    if (data[1].startsWith("-")) {
                        //camServo.writeAngle(Integer.parseInt(data[1].replace("-", "")));
                    } else {
                        //camServo.writeAngle(Integer.parseInt(data[1]));
                    }
                    reply("tilt successful", message);
                    break;
                case "detect":
                    if (data[1].equals("distance")) {


                    }
            }
        }

    }
}
