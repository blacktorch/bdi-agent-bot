package constants;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public final class SensorConstants {
    private SensorConstants(){
        //Do not initialize
    }

    public static final int ULTRASONIC_ECHO_PIN = 10;
    public static final int ULTRASONIC_TRIGGER_PIN = 14;
    public static final Pin LEFT_LDR = RaspiPin.GPIO_24;
    public static final Pin MIDDLE_LDR = RaspiPin.GPIO_27;
    public static final Pin RIGHT_LDR = RaspiPin.GPIO_28;
}
