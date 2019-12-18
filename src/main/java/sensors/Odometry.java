package sensors;

import actions.Move;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioInterruptCallback;
import constants.SensorConstants;

public class Odometry   {

    private static double DIST_PER_PULSE = 0.11989583;

    private double distanceMoved;
    private static double totalDistanceMoved;

    private GpioPin leftInputA;
    private GpioPin rightInputB;

    private int pulseA;
    private int pulseB;

    private GpioController gpio;
    private static Odometry odometry;

    private Odometry(){

        gpio =  GpioFactory.getInstance();

        this.distanceMoved = 0.0f;
        totalDistanceMoved = 0.0f;
        pulseA = 0;
        pulseB = 0;

        setup();

    }

    private synchronized void setup(){

        if (Gpio.wiringPiSetup() == -1){
            System.out.println("GPIO Setup Failed!");
            return;
        }

        Gpio.pinMode(21, Gpio.INPUT);
        Gpio.pinMode(27, Gpio.INPUT);

        Gpio.pullUpDnControl(21, Gpio.PUD_UP);
        Gpio.pullUpDnControl(27, Gpio.PUD_UP);

        try {

            Gpio.wiringPiISR(21, Gpio.INT_EDGE_BOTH, new GpioInterruptCallback() {
                @Override
                public void callback(int i) {
                    ++pulseA;
                    distanceMoved = (((double)pulseA + (double)pulseB)/2) * (230.2/1000);
                    totalDistanceMoved += distanceMoved;
                    //System.out.println(pulseA);
                }
            });

            Gpio.wiringPiISR(27, Gpio.INT_EDGE_BOTH, new GpioInterruptCallback() {
                @Override
                public void callback(int i) {
                    ++pulseB;
                    distanceMoved = (((double)pulseA + (double)pulseB)/2) * (230.2/1000);
                }
            });

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public synchronized static Odometry getInstance(){
        if (odometry == null){
            odometry = new Odometry();
            return odometry;
        } else {
            return odometry;
        }
    }

    public synchronized void reset(){
        this.distanceMoved = 0.0f;
        pulseA = 0;
        pulseB = 0;
    }

    public synchronized double getDistanceMoved(){
        //System.out.println("Hello!");
        return distanceMoved;
    }

    public synchronized int getPulseA(){
        return pulseA;
    }

    public double getTotalDistanceMoved(){
        return totalDistanceMoved;
    }

}