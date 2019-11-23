package utils;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import constants.ActuatorConstants;

public final class ActuatorUtil {

    private ActuatorUtil(){
        //
    }

    public static void changeDirection(int direction, GpioPinDigitalOutput pinA, GpioPinDigitalOutput pinB){
        switch (direction) {
            case ActuatorConstants.LEFT_MOTOR_FORWARD_DIRECTION:
                pinA.setState(PinState.HIGH);
                pinB.setState(PinState.LOW);
                break;
            case ActuatorConstants.LEFT_MOTOR_BACKWARD_DIRECTION:
                pinA.setState(PinState.LOW);
                pinB.setState(PinState.HIGH);
                break;
        }
    }

    public static double map(int value){
        return map(value, 0, 180, 150, 600);
    }

    public static double map(int value, int min, int max, int desiredMin, int desiredMax){
        int originalValueDifference;
        int desiredValueDifference;
        float scalingFactor;
        double mappedValue;
        boolean scaleForward;

        if (max > min){
            originalValueDifference = max - min;
        } else {
            originalValueDifference = min - max;
        }

        if (desiredMax > desiredMin){
            desiredValueDifference = desiredMax - desiredMin;
        } else {
            desiredValueDifference = desiredMin - desiredMax;
        }

        if (desiredValueDifference > originalValueDifference){
            scalingFactor = ((float)desiredValueDifference / (float)originalValueDifference);

        } else {
            scalingFactor = ((float)originalValueDifference/(float)desiredValueDifference);
        }

        if (desiredMax > max){
            scaleForward = true;
        }else {
            scaleForward =false;
        }


        if (scaleForward){
            double scale = (value * scalingFactor);
            mappedValue = scale + desiredMin;
        } else {
            double scale = value - min;
            mappedValue = (scale/scalingFactor);
        }

        return mappedValue;
    }

    public static double valueMap(int value, int min, int max, int desiredMin, int desiredMax){

        double mappedValue = 0.0;

        mappedValue = (value - min)*((desiredMax - desiredMin)/(max - min)) + desiredMin;

        return mappedValue;

    }
}
