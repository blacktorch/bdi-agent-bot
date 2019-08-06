package actuators;

public interface IMotor {

    void setDirection(int direction);

    void setSpeed(int speed);

    void start(int initialSpeed, int initialDirection);

    void stop();

}
