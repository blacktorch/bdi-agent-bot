import actions.Move;

import actuators.Servo;
import connectivity.GobalVariable;
import connectivity.Subscribe;
import constants.ActuatorConstants;
import enums.Pole;
import environment.Point;
import sensors.Odometry;
import java.io.IOException;

import static com.pi4j.wiringpi.Gpio.delay;

public class Main {

    private static Odometry odometry = Odometry.getInstance();
    private static final double RIGHT_TURN_DISTANCE = 911;
    private static final double LEFT_TURN_DISTANCE = 911.7071;
    private static final double SINGLE_CELL_SPACE = 230.2;


    public static void main(String[] args) throws IOException {

//        Webcam webcam = Webcam.getDefault();
//        if (webcam != null) {
//            System.out.println("Webcam: " + webcam.getName());
//            webcam.setViewSize(new Dimension(320,240));
//            webcam.open();
//            try {
//                System.out.println(webcam.getViewSize().height);
//                ImageIO.write(webcam.getImage(), "PNG", new File("hello-world.png"));
//            }
//            catch (IOException e){
//                e.printStackTrace();
//            }
//
//        } else {
//            System.out.println("No webcam detected");
//        }
        GobalVariable.args = args;

        Servo wheel = new Servo(ActuatorConstants.STEER_SERVO_CHANNEL);
        wheel.writeAngle(100);
        delay(100);
        Pole pole = Pole.EAST;


        Subscribe subscriber = new Subscribe();

        new Thread(() -> {
            try{
                subscriber.start(args);
                System.out.println("Subscriber is started");

            }catch(Exception e){
                e.printStackTrace();
            }

        }).start();




        while (true){
            if (!subscriber.isNewDataReceived()){
                //System.out.print(".");
            } else {
                Point[] points = new Point[subscriber.getPoints().size()];
                for (int i = 0; i < points.length; i++){
                    points[i] = subscriber.getPoints().get(i);
                }


                if(points[1].getY() != points[0].getY()){
                    if(points[1].getY() == (points[0].getY() -1)){
                        pole=Pole.SOUTH;
                    }
                    else{
                        pole=Pole.NORTH;
                    }
                }
                else if(points[1].getX() != points[0].getX()){
                    if(points[1].getX() == (points[0].getX() -1)){
                        pole=Pole.WEST;
                    }
                    else{
                        pole=Pole.EAST;
                    }
                }


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
                                if (points[i + 1].getY() == (points[i].getY() + 1) && points[i + 1].getX() == points[i].getX()) {
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
                subscriber.setNewDataReceived(false);
            }

        }

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
                System.out.println(odometry.getPulseA());
                odometry.reset();

                break;
            }
        }

        Move.stopEngine();
    }


}
