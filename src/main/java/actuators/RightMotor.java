package actuators;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.wiringpi.SoftPwm;
import constants.ActuatorConstants;

import static utils.ActuatorUtil.changeDirection;

public class RightMotor implements IMotor{

    private static RightMotor rightMotor;
    private GpioController gpio = GpioFactory.getInstance();
    private GpioPinDigitalOutput motorPinA;
    private GpioPinDigitalOutput motorPinB;

    private int speed;
    private boolean started;

    private RightMotor() {
        //Do not initialize outside..
        motorPinA = gpio.provisionDigitalOutputPin(ActuatorConstants.RIGHT_MOTOR_PIN_A);
        motorPinB = gpio.provisionDigitalOutputPin(ActuatorConstants.RIGHT_MOTOR_PIN_B);
        SoftPwm.softPwmCreate(ActuatorConstants.RIGHT_MOTOR_ENABLE, 0, 100);
    }

    public static RightMotor getInstance() {

        if (rightMotor == null) {
            rightMotor = new RightMotor();
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
            SoftPwm.softPwmWrite(ActuatorConstants.RIGHT_MOTOR_ENABLE, speed);
            this.speed = speed;
        } else {
            System.out.println("Invalid speed argument passed, speed should be between 0 - 100");
            SoftPwm.softPwmWrite(ActuatorConstants.RIGHT_MOTOR_ENABLE, 0);
        }
    }

    @Override
    public void start(int initialSpeed, int initialDirection) {
        SoftPwm.softPwmWrite(ActuatorConstants.RIGHT_MOTOR_ENABLE, initialSpeed);
        changeDirection(initialDirection, motorPinA, motorPinB);
        this.speed = initialSpeed;
        this.started = true;

    }

    @Override
    public void stop() {
        SoftPwm.softPwmWrite(ActuatorConstants.RIGHT_MOTOR_ENABLE, 0);
        motorPinA.setState(PinState.LOW);
        motorPinB.setState(PinState.LOW);
        gpio.shutdown();
        this.started = false;
    }
}
