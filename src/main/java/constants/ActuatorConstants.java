package constants;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public final class ActuatorConstants {

    private ActuatorConstants(){
        //Do not initialize..
    }

    public static final int LEFT_MOTOR_ENABLE = 7;
    public static final int RIGHT_MOTOR_ENABLE = 15;
    public static final Pin LEFT_MOTOR_PIN_A = RaspiPin.GPIO_00;
    public static final Pin LEFT_MOTOR_PIN_B = RaspiPin.GPIO_01;
    public static final Pin RIGHT_MOTOR_PIN_A = RaspiPin.GPIO_03;
    public static final Pin RIGHT_MOTOR_PIN_B = RaspiPin.GPIO_02;

    public static final int LEFT_MOTOR_FORWARD_DIRECTION = 0xF1;
    public static final int LEFT_MOTOR_BACKWARD_DIRECTION = 0xF0;

    public static final int RIGHT_MOTOR_FORWARD_DIRECTION = 0xF0;
    public static final int RIGHT_MOTOR_BACKWARD_DIRECTION = 0xF1;

    public static final float MOTOR_RADIUS = 0.6f;

    public static final int CAMERA_SERVO_CHANNEL = 11;
    public static final int BASE_SERVO_CHANNEL = 12;
    public static final int ELBOW_SERVO_CHANNEl = 13;
    public static final int PAN_SERVO_CHANNEL = 14;
    public static final int TILT_SERVO_CHANNEL = 15;
    public static final int STEER_SERVO_CHANNEL = 0;
    public static final int SERVO_FREQUENCY = 60;


}
