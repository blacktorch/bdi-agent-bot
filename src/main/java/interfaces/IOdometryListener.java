package interfaces;

public interface IOdometryListener {
    void up(long encoderValue);
    void down(long encoderValue);
}
