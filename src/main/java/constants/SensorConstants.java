package constants;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public final class SensorConstants {
    private SensorConstants(){
        //Do not initialize
    }

    public static final int ULTRASONIC_ECHO_PIN = 5;
    public static final int ULTRASONIC_TRIGGER_PIN = 4;
    public static final int REJECTION_TIME = 23529411;
    public static final int REJECTION_START = 1000;
    public static final Pin LEFT_LDR = RaspiPin.GPIO_24;
    public static final Pin MIDDLE_LDR = RaspiPin.GPIO_27;
    public static final Pin RIGHT_LDR = RaspiPin.GPIO_28;
    public static final Pin ODO_LEFT_A = RaspiPin.GPIO_21;
    public static final Pin ODO_LEFT_B = RaspiPin.GPIO_22;
    public static final Pin ODO_RIGHT_A = RaspiPin.GPIO_26;
    public static final Pin ODO_RIGHT_B = RaspiPin.GPIO_27;
}
