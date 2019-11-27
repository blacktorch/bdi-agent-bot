package actions;

import actuators.LeftMotor;
import actuators.RightMotor;
import actuators.Servo;
import com.pi4j.wiringpi.Gpio;
import constants.ActuatorConstants;
import interfaces.IMoveCompletedListener;
import utils.ActuatorUtil;

public final class Move {
    private static LeftMotor leftMotor = LeftMotor.getInstance();
    private static RightMotor rightMotor = RightMotor.getInstance();
    private static boolean isMoving = false;
    private static int currentAngle = 0;

//    private static IMoveCompletedListener moveCompletedListener;

    public static void forward(int speed){
        leftMotor.start(speed, ActuatorConstants.LEFT_MOTOR_FORWARD_DIRECTION);
        rightMotor.start(speed, ActuatorConstants.RIGHT_MOTOR_FORWARD_DIRECTION);
        isMoving = true;
    }

//    public static void setMoveCompletedListener(IMoveCompletedListener iMoveCompletedListener){
//        moveCompletedListener=iMoveCompletedListener;
//    }

    public static void backward(int speed){
        leftMotor.start(speed, ActuatorConstants.LEFT_MOTOR_BACKWARD_DIRECTION);
        rightMotor.start(speed, ActuatorConstants.RIGHT_MOTOR_BACKWARD_DIRECTION);
        isMoving = true;
    }

    public static void steer(int angle){

        if (angle != currentAngle){

            Servo steering = new Servo(ActuatorConstants.STEER_SERVO_CHANNEL);
            double mappedAngle = ActuatorUtil.valueMap(angle, -30, 30, steering.getMinAngle(), steering.getMaxAngle());
            steering.writeAngle((int)mappedAngle);
            currentAngle = angle;
        }

    }

    public static void turnLeft(float radius, int speed){
        if (!leftMotor.isStarted()){
            leftMotor.start((int)((radius) * speed), ActuatorConstants.LEFT_MOTOR_BACKWARD_DIRECTION);
        } else {
            leftMotor.setSpeed((int)((radius) * speed));
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
            rightMotor.start((int)((radius) * speed), ActuatorConstants.RIGHT_MOTOR_BACKWARD_DIRECTION);
        } else {
            rightMotor.setSpeed((int)((radius) * speed));
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

    public static void stopEngine(){
        leftMotor.stop();
        rightMotor.stop();
        isMoving = false;
    }

    public static void startEngine(){
        leftMotor.start(0, ActuatorConstants.LEFT_MOTOR_FORWARD_DIRECTION);
        rightMotor.start(0, ActuatorConstants.RIGHT_MOTOR_FORWARD_DIRECTION);
    }

    public static boolean getIsMoving(){
        return isMoving;
    }
}
