package sensors; /**
 * 	HC-SR04 Ultrasonic sensor (return in mm)
 **/

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.*;

public class UltrasonicSensor {

    private int pinEcho, pinTrigger;
    private long rejectionStart;
    private long rejectionTime;

    private GpioController gpio;
    private GpioPinDigitalOutput gpioPinTrigger;
    private GpioPinDigitalInput gpioPinEcho;

    public UltrasonicSensor (int echo, int trigger, long rejectionStart,long rejectionTime) {

        this.pinEcho = echo;
        this.pinTrigger = trigger;
        this.rejectionStart =rejectionStart;
        this.rejectionTime =rejectionTime;

        gpio = GpioFactory.getInstance();

        gpioPinTrigger =gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(pinTrigger), "gpioPinTrigger", PinState.HIGH);//pin,tag,initial-state
        gpioPinTrigger.setShutdownOptions(true, PinState.LOW);

        gpioPinEcho =gpio.provisionDigitalInputPin(RaspiPin.getPinByAddress(pinEcho),PinPullResistance.PULL_DOWN);//pin,tag,initial-state

    }

    public int getDistance() throws Exception {
        int distance;
        long startTime, endTime, rejection_start = 0, rejection_time = 0;

        /**Start ranging- trig should be in high state for 10us to generate ultrasonic signal
         * this will generate 8 cycle sonic burst.
         * produced signal would looks like, _|-----|
         *
         *  **/
        gpioPinTrigger.low(); busyWaitMicros(2);
        gpioPinTrigger.high(); busyWaitMicros(10);
        gpioPinTrigger.low();

        //echo pin high time is propotional to the distance _|----|
        //distance calculation

        while(gpioPinEcho.isLow()){ //wait until echo get high
            busyWaitNanos(1); //wait 1ns
            rejection_start++;
            if(rejection_start== rejectionStart) return -1; //something wrong
        }
        startTime=System.nanoTime();

        while(gpioPinEcho.isHigh()){ //wait until echo get low
            busyWaitNanos(1); //wait 1ns
            rejection_time++;
            if(rejection_time== rejectionTime) return -1; //infinity
        }
        endTime=System.nanoTime();

        distance=(int)((endTime-startTime)/5882.35294118); //distance in mm

        return distance;
    }

    public static void busyWaitMicros(long micros){
        long waitUntil = System.nanoTime() + (micros * 1_000);
        while(waitUntil > System.nanoTime()){

        }
    }

    public static void busyWaitNanos(long nanos){
        long waitUntil = System.nanoTime() + nanos;
        while(waitUntil > System.nanoTime()){

        }
    }

}