package sensors;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import constants.SensorConstants;

public class Odometry   {

    private int pulsePerRev;
    private float circumference;
    private static double DIST_PER_PULSE = 0.11989583;

    private double distanceMoved;

    GpioPin leftInputA;
    GpioPin rightInputB;

    private int pulseA;
    private int pulseB;

    GpioController gpio;

    public Odometry(int pulsePerRev){

        gpio =  GpioFactory.getInstance();
        leftInputA = gpio.provisionDigitalInputPin(SensorConstants.ODO_LEFT_A, "PinA", PinPullResistance.PULL_UP);
        rightInputB = gpio.provisionDigitalInputPin(SensorConstants.ODO_LEFT_B, "PinB", PinPullResistance.PULL_UP);

        this.pulsePerRev = pulsePerRev;
        this.distanceMoved = 0.0f;
        pulseA = 0;
        pulseB = 0;

    }

    public void setup(){

        try {
            leftInputA.addListener(new GpioPinListenerDigital() {
                int lastA;

                @Override
                public synchronized void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent arg0) {

                    if (arg0.getState().getValue() != lastA){
                        lastA = arg0.getState().getValue();
                        ++pulseA;

                        distanceMoved = (((double) pulseA + (double) pulseB)/2) * DIST_PER_PULSE;
                        System.out.println(distanceMoved);

                    }

                }
            });

            rightInputB.addListener(new GpioPinListenerDigital() {
                int lastB;

                @Override
                public synchronized void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent arg0) {

                    if (arg0.getState().getValue() != lastB){
                        lastB = arg0.getState().getValue();
                        ++pulseB;
                    }

                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void reset(){
        this.distanceMoved = 0.0f;
        pulseA = 0;
        pulseB = 0;
    }

    public double getDistanceMoved(){
        return distanceMoved;
    }

}
