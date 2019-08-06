package actuators;


import com.pi4j.io.i2c.I2CFactory;
import constants.ActuatorConstants;
import utils.ActuatorUtil;

import static utils.TimeUtil.delay;

public class Servo {

    private int minAngle;
    private int maxAngle;
    private int channel;
    private int currentAngle;

    private PCA9685 servoDriver;

    public Servo(int minAngle, int maxAngle, int channel) {
        this.maxAngle = maxAngle;
        this.minAngle = minAngle;
        this.channel = channel;

        try {
            initServoDriver();
        }
        catch (I2CFactory.UnsupportedBusNumberException ex){
            ex.printStackTrace();
        }

    }

    public Servo(int channel)  {
        this.channel = channel;

        try {
            initServoDriver();
        }
        catch (I2CFactory.UnsupportedBusNumberException ex){
            ex.printStackTrace();
        }

        switch (channel) {
            case ActuatorConstants.BASE_SERVO_CHANNEL:
                this.minAngle = 0;
                this.maxAngle = 180;
                writeAngle(180);
                delay(500);
                this.currentAngle = 180;
                break;
            case ActuatorConstants.ELBOW_SERVO_CHANNEl:
                this.minAngle = 0;
                this.maxAngle = 60;
                writeAngle(30);
                delay(500);
                this.currentAngle = 30;
                break;
            case ActuatorConstants.NECK_SERVO_CHANNEL:
                this.minAngle = 10;
                this.maxAngle = 170;
                writeAngle(80);
                delay(500);
                this.currentAngle = 80;
                break;
            case ActuatorConstants.CLAW_SERVO_CHANNEL:
                this.minAngle = 60;
                this.maxAngle = 145;
                writeAngle(145);
                delay(500);
                this.currentAngle = 145;
                break;
            case ActuatorConstants.CAMERA_SERVO_CHANNEL:
                this.minAngle = 60;
                this.maxAngle = 125;
                writeAngle(this.maxAngle);
                delay(500);

                this.currentAngle = this.maxAngle;
                break;
        }

    }

    private void initServoDriver()throws I2CFactory.UnsupportedBusNumberException{
        servoDriver = new PCA9685();

        servoDriver.setPWMFreq(ActuatorConstants.SERVO_FREQUENCY);
    }

    public boolean writeAngle(int angle) {
        int mappedAngle = (int) ActuatorUtil.map(angle, 0, 180, 150, 600);
        if (angle < minAngle || angle > maxAngle) {
            System.out.println("Angle is out of range");
            return false;
        } else {
            servoDriver.setPWM(channel, 0, mappedAngle);
            currentAngle = mappedAngle;
            return true;
        }
    }

    public void turnOff() {
        servoDriver.setPWM(channel, 0, 0);
    }

    public boolean travel(int currentAngle, int destinationAngle, int speed){
        destinationAngle = (int) ActuatorUtil.map(destinationAngle);
        currentAngle = (int) ActuatorUtil.map(currentAngle);
        //System.out.println(destinationAngle);
        if (destinationAngle > currentAngle){
            if (destinationAngle > (int) ActuatorUtil.map(maxAngle)){
                System.out.println("Destination angle is out of range");
                return false;
            }

            int time = (speed/(destinationAngle - currentAngle));

            System.out.println(time);

            for (int i = currentAngle+1 ; i <= destinationAngle; i++){
                servoDriver.setPWM(channel, 0, i);
                delay(time);
            }

            this.currentAngle = (int)ActuatorUtil.map(destinationAngle, 150, 600, 0, 180);

            return true;

        } else if (destinationAngle < currentAngle){
            if (destinationAngle < (int) ActuatorUtil.map(minAngle)){
                System.out.println("Destination angle is out of range");
                return false;
            }

            int time = (speed/(currentAngle - destinationAngle));
            System.out.println(time);

            for (int i = currentAngle-1 ; i >= destinationAngle; i--){
                servoDriver.setPWM(channel, 0, i);
                delay(time);
            }

            this.currentAngle = (int)ActuatorUtil.map(destinationAngle, 150, 600, 0, 180);

            return true;
        }

        return false;

    }

    public boolean travel(int destinationAngle, int howLong){
        return travel(currentAngle, destinationAngle, howLong);
    }

    public int getMaxAngle() {
        return maxAngle;
    }

    public int getCurrentAngle() {
        return currentAngle;
    }

    public int getChannel() {
        return channel;
    }

    public int getMinAngle() {
        return minAngle;
    }

    public PCA9685 getServoDriver() {
        return servoDriver;
    }
}
