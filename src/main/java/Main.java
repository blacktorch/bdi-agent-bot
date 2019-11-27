
import actions.Move;

import actuators.Servo;
import constants.ActuatorConstants;
import enums.Pole;
import environment.Point;
import sensors.Odometry;

import static com.pi4j.wiringpi.Gpio.delay;

public class Main {

    private static Odometry odometry = Odometry.getInstance();
    private static final double RIGHT_TURN_DISTANCE = 540.6694;
    private static final double LEFT_TURN_DISTANCE = 381.2087;
    private static final double SINGLE_CELL_SPACE = 230.2;

    public static void main(String[] args) {

        Servo wheel = new Servo(ActuatorConstants.STEER_SERVO_CHANNEL);
        wheel.writeAngle(100);
        delay(100);

        Point[] points = {new Point(), new Point(1, 0), new Point(2, 0), new Point(3, 0),
                new Point(4, 0), new Point(4, -1), new Point(4, -2), new Point(4, -3),
                new Point(4, -4), new Point(3, -4), new Point(2, -4),new Point(2, -3),
                new Point(2, -2)};

        Pole pole = Pole.EAST;


        for (int i = 0; i < points.length; i++) {
            if ((i + 1) != points.length){
                System.out.println(pole.name());
                switch (pole){
                    case EAST:
                        if (points[i + 1].getY() == (points[i].getY() + 1) && points[i + 1].getX() == points[i].getX() ) {
                            performMoveAction(LEFT_TURN_DISTANCE, -30);
                            Move.steer(0);
                            pole = Pole.NORTH;
                        } else if (points[i + 1].getY() == (points[i].getY() - 1) && points[i + 1].getX() == points[i].getX()) {
                            performMoveAction(RIGHT_TURN_DISTANCE, 30);
                            Move.steer(0);
                            pole = Pole.SOUTH;
                        } else if (points[i + 1].getX() == (points[i].getX() + 1)&& points[i + 1].getY() == points[i].getY()) {
                            performMoveAction(SINGLE_CELL_SPACE, 0);
                        }
                        break;
                    case SOUTH:
                        if (points[i + 1].getY() == (points[i].getY() - 1) && points[i + 1].getX() == points[i].getX()) {
                            performMoveAction(SINGLE_CELL_SPACE, 0);
                        } else if (points[i + 1].getX() == (points[i].getX() + 1)&& points[i + 1].getY() == points[i].getY()) {
                            performMoveAction(LEFT_TURN_DISTANCE, -30);
                            Move.steer(0);
                            pole = Pole.EAST;
                        } else if (points[i + 1].getX() == (points[i].getX() - 1)&& points[i + 1].getY() == points[i].getY()) {
                            performMoveAction(RIGHT_TURN_DISTANCE, 30);
                            Move.steer(0);
                            pole = Pole.WEST;
                        }
                        break;
                    case WEST:
                        if (points[i + 1].getY() == (points[i].getY() - 1) && points[i + 1].getX() == points[i].getX()) {
                            performMoveAction(LEFT_TURN_DISTANCE, -30);
                            Move.steer(0);
                            pole = Pole.SOUTH;
                        } else if (points[i + 1].getX() == (points[i].getX() - 1)&& points[i + 1].getY() == points[i].getY()) {
                            performMoveAction(SINGLE_CELL_SPACE, 0);
                        } else if (points[i + 1].getY() == (points[i].getY() + 1) && points[i + 1].getX() == points[i].getX()) {
                            performMoveAction(RIGHT_TURN_DISTANCE, 30);
                            Move.steer(0);
                            pole = Pole.NORTH;
                        }
                        break;
                    case NORTH:
                        if (points[i + 1].getY() == (points[i].getY() - 1) && points[i + 1].getX() == points[i].getX()) {
                            performMoveAction(SINGLE_CELL_SPACE, 0);
                        } else if (points[i + 1].getX() == (points[i].getX() - 1)&& points[i + 1].getY() == points[i].getY()) {
                            performMoveAction(LEFT_TURN_DISTANCE, -30);
                            Move.steer(0);
                            pole = Pole.WEST;
                        } else if (points[i + 1].getX() == (points[i].getX() + 1) && points[i + 1].getY() == points[i].getY()) {
                            performMoveAction(RIGHT_TURN_DISTANCE, 30);
                            Move.steer(0);
                            pole = Pole.EAST;
                        }
                        break;

                }
            }

        }

        System.out.println("Goal Reached");
        System.exit(0);

//        Move.stopEngine();


    }

    private static void performMoveAction(double distance, int angle) {
        Move.steer(angle);
        odometry.reset();
        if (!Move.getIsMoving()){
            Move.forward(30);
        }

        while (true) {
            if (odometry.getDistanceMoved() >= distance) {
                System.out.println("Action Complete");
                System.out.println(odometry.getDistanceMoved());
                odometry.reset();

                break;
            }
        }

        Move.stopEngine();
    }


}
