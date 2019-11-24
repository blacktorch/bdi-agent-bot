package actuators;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.wiringpi.SoftPwm;
import constants.ActuatorConstants;

import static utils.ActuatorUtil.changeDirection;

public class RightMotor implements IMotor{

    private static RightMotor rightMotor;
    private GpioController gpio = GpioFactory.getInstance();
    private GpioPinDigitalOutput motorPinA;
    private GpioPinDigitalOutput motorPinB;
    private PCA9685 motorDriver;

    private int speed;
    private boolean started;

    private RightMotor()throws I2CFactory.UnsupportedBusNumberException {
        //Do not initialize outside..
        motorPinA = gpio.provisionDigitalOutputPin(ActuatorConstants.RIGHT_MOTOR_PIN_A);
        motorPinB = gpio.provisionDigitalOutputPin(ActuatorConstants.RIGHT_MOTOR_PIN_B);
        motorDriver = new PCA9685();
        motorDriver.setPWMFreq(60);
        motorDriver.setPWM(5,0, 100*40);
    }

    public static RightMotor getInstance() {

        if (rightMotor == null) {
            try {
                rightMotor = new RightMotor();
            }
            catch (I2CFactory.UnsupportedBusNumberException e){
                e.printStackTrace();
            }

            return rightMotor;
        } else {
            return rightMotor;
        }

    }

    public int getSpeed(){
        return speed;
    }

    public boolean isStarted(){
        return started;
    }


    @Override
    public void setDirection(int direction) {
        changeDirection(direction, motorPinA, motorPinB);
    }

    @Override
    public void setSpeed(int speed) {
        if (speed >= 0 && speed <= 100) {
            motorDriver.setPWM(5,0, speed*40);
            this.speed = speed;
        } else {
            System.out.println("Invalid speed argument passed, speed should be between 0 - 100");
            motorDriver.setPWM(5,0, 0);
        }
    }

    @Override
    public void start(int initialSpeed, int initialDirection) {
        motorDriver.setPWM(5,0, initialSpeed*40);
        changeDirection(initialDirection, motorPinA, motorPinB);
        this.speed = initialSpeed;
        this.started = true;

    }

    @Override
    public void stop() {
        motorDriver.setPWM(5,0, 0);
        motorPinA.setState(PinState.LOW);
        motorPinB.setState(PinState.LOW);
        gpio.shutdown();
        this.started = false;
    }
}
