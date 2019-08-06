package actions;

import actuators.LeftMotor;
import actuators.RightMotor;
import com.pi4j.wiringpi.Gpio;
import constants.ActuatorConstants;

public final class Move {
    private static LeftMotor leftMotor = LeftMotor.getInstance();
    private static RightMotor rightMotor = RightMotor.getInstance();

    public static void forward(int speed){
        leftMotor.start(speed, ActuatorConstants.LEFT_MOTOR_FORWARD_DIRECTION);
        rightMotor.start(speed, ActuatorConstants.RIGHT_MOTOR_FORWARD_DIRECTION);
    }

    public static void backward(int speed){
        leftMotor.start(speed, ActuatorConstants.LEFT_MOTOR_BACKWARD_DIRECTION);
        rightMotor.start(speed, ActuatorConstants.RIGHT_MOTOR_BACKWARD_DIRECTION);
    }

    public static void turnLeft(float radius, int speed){
        if (!leftMotor.isStarted()){
            leftMotor.start((int)((radius/2) * speed), ActuatorConstants.LEFT_MOTOR_BACKWARD_DIRECTION);
        } else {
            leftMotor.setSpeed((int)((radius/2) * speed));
            leftMotor.setDirection(ActuatorConstants.LEFT_MOTOR_BACKWARD_DIRECTION);
        }
        if (!rightMotor.isStarted()){
            rightMotor.start((int)((radius) * speed), ActuatorConstants.RIGHT_MOTOR_FORWARD_DIRECTION);
        }else {
            rightMotor.setSpeed((int)((radius) * speed));
            rightMotor.setDirection(ActuatorConstants.RIGHT_MOTOR_FORWARD_DIRECTION);
        }
    }

    public static void turnRight(float radius, int speed){
        if (!rightMotor.isStarted()){
            rightMotor.start((int)((radius/2) * speed), ActuatorConstants.RIGHT_MOTOR_BACKWARD_DIRECTION);
        } else {
            rightMotor.setSpeed((int)((radius/2) * speed));
            rightMotor.setDirection(ActuatorConstants.RIGHT_MOTOR_BACKWARD_DIRECTION);
        }
        if (!leftMotor.isStarted()){
            leftMotor.start((int)((radius) * speed), ActuatorConstants.LEFT_MOTOR_FORWARD_DIRECTION);
        }else {
            leftMotor.setSpeed((int)((radius) * speed));
            leftMotor.setDirection(ActuatorConstants.LEFT_MOTOR_FORWARD_DIRECTION);
        }
    }

    public static void applyBreak(int breakDelay){
        int currentSpeed = ((leftMotor.getSpeed() + rightMotor.getSpeed())/2);
        for (int i = currentSpeed; i > 0; i-=5){
            leftMotor.setSpeed(i);
            rightMotor.setSpeed(i);
            System.out.println(i);
            Gpio.delay(breakDelay);
        }
    }

    public static void stop(){
        leftMotor.stop();
        rightMotor.stop();
    }
}
