
import actions.Move;
import actuators.Servo;
import connectivity.Message;
import connectivity.RobotServer;
import constants.ActuatorConstants;
import constants.SensorConstants;
import sensors.UltrasonicSensor;
import utils.ActuatorUtil;
import utils.ConnectionUtils;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import static utils.TimeUtil.delay;

public class Main implements Observer {

     //static Servo camServo = new Servo(ActuatorConstants.CAMERA_SERVO_CHANNEL);
    UltrasonicSensor ultrasonicSensor = new UltrasonicSensor(SensorConstants.ULTRASONIC_ECHO_PIN,
            SensorConstants.ULTRASONIC_TRIGGER_PIN, SensorConstants.REJECTION_START,
            SensorConstants.REJECTION_TIME);
    static Servo base = new Servo(ActuatorConstants.BASE_SERVO_CHANNEL);
    static Servo elbow = new Servo(ActuatorConstants.ELBOW_SERVO_CHANNEl);
    static Servo cam = new Servo(ActuatorConstants.CAMERA_SERVO_CHANNEL);


    public static void main(String[] args) throws  IOException {
       // elbow.writeAngle(40);
        Move.stopEngine();
//        for (;;){
//            armDance();
//        }
//        base.turnOff();
//        elbow.turnOff();

    }

    private void reply(String msg, Message message){
        try {
            message.getOut().write(ConnectionUtils.shiftDataToSend(msg, ConnectionUtils.BUFFER_SIZE, ConnectionUtils.LEFT), 0, ConnectionUtils.BUFFER_SIZE);
            message.getOut().flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void armDance(){
        delay(120);
        cam.writeAngle(65);
        Move.turnRight(0.5f, 50);
        base.travel(145, 600);
        elbow.travel(50,600);
        Move.turnLeft(0.5f, 50);
        cam.writeAngle(120);
        elbow.travel(10, 400);
        base.travel(180, 400);
        //elbow.writeAngle(30);
        //delay(1000);
        //base.writeAngle(180);
        //elbow.writeAngle(10);
    }

    @Override
    public void update(Observable o, Object arg) {
        Message message = (Message)arg;
        String[] data = message.getMessage().split(" ");
        if (data.length > 0){
            switch (data[0]){
                case "drive":
                    if (data[1].startsWith("-")){
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
                    if (data[1].startsWith("-")){
                        Move.turnLeft(Integer.parseInt(data[1].replace("-", "")), 100);
                    } else {
                        Move.turnRight(Integer.parseInt(data[1]),100);
                    }
                    reply("steer successful", message);
                    break;
                case "tilt":
                    if (data[1].startsWith("-")){
                        //camServo.writeAngle(Integer.parseInt(data[1].replace("-", "")));
                    } else {
                        //camServo.writeAngle(Integer.parseInt(data[1]));
                    }
                    reply("tilt successful", message);
                    break;
                case "detect":
                    if (data[1].equals("distance")){
                        try {
                            System.out.println(ultrasonicSensor.getDistance());
                            reply(String.valueOf(ultrasonicSensor.getDistance()), message);
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                    }
            }
        }

    }
}
