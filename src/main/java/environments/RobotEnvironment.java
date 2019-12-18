//package environments;
//
//import actions.Move;
//import connectivity.GlobalVariable;
//import jason.asSyntax.Literal;
//import jason.asSyntax.Structure;
//import jason.environment.Environment;
//import sensors.Odometry;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.ObjectInputStream;
//import java.util.logging.LogManager;
//import java.util.logging.Logger;
//
//public class RobotEnvironment extends Environment {
//    private Logger logger = Logger.getLogger("bdiRobot." + RobotEnvironment.class.getName());
//    Odometry odometry = Odometry.getInstance();
//
//    Point[] points;
//    static int pointCounter = 1;
//
//    /**
//     * Called before the MAS execution with the args informed in .mas2j
//     */
//    @Override
//    public void init(String[] args) {
//        try {
//            LogManager.getLogManager().readConfiguration(new FileInputStream("logging.properties"));
//        } catch (Exception e) {
//            System.out.println("Exception in logging file");
//        }
//
//        //Assuming that normalized waypoints are get once from DDS
//        try {
//            File file = new File("objectState.txt");
//            FileInputStream fileInputStream = new FileInputStream(file);
//            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
//            points = (Point[]) objectInputStream.readObject();
//            objectInputStream.close();
//            fileInputStream.close();
//            System.out.println(points.length);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //start the agent
//        //Add the initial position (0,0) in agent mental belief
//        //Compute the initial orientation and add it inside agent mental belief
//        //add the target location in agent mental belief
//        addPercept(Literal.parseLiteral("start"));
//
//        if (points[1].getX() != points[0].getX()) {
//            if (points[1].getX() == (points[0].getX() - 1)) {
//                addPercept(Literal.parseLiteral("orientation(west)"));
//            } else {
//                addPercept(Literal.parseLiteral("orientation(east)"));
//            }
//        } else {
//            if (points[1].getY() == (points[0].getY() - 1)) {
//                addPercept(Literal.parseLiteral("orientation(south)"));
//            } else {
//                addPercept(Literal.parseLiteral("orientation(north)"));
//            }
//        }
//
//        //Add the targetLocation
//        addPercept(Literal.parseLiteral("targetLoc(" + points[points.length - 1].getX() + ","
//                + points[points.length - 1].getY() + ")"));
//
//    }
//
//    private void updatePercept(String agName) {
//        clearAllPercepts();
//        clearPercepts(agName);
//
//        if (agName.equals("agent1")) {
//            if (pointCounter < points.length) {
//                GlobalVariable.currentPosition = points[pointCounter - 1].toString();
//                addPercept(Literal.parseLiteral("nextLoc(" + points[pointCounter].getX() + "," + points[pointCounter].getY() + ")"));
//                pointCounter++;
//            }
//        }
//    }
//
//    private void performMoveAction(double distance, int angle) {
//        if (!(GlobalVariable.ultraSonicData >= 0.0 && GlobalVariable.ultraSonicData <= 30.0)) {
//            Move.steer(angle);
//            odometry.reset();
//            if (!Move.getIsMoving()) {
//                Move.forward(30);
//            }
//
//            while (true) {
//                if (odometry.getDistanceMoved() >= distance || (GlobalVariable.ultraSonicData >= 0.0 && GlobalVariable.ultraSonicData <= 30.0)) {
//                    System.out.println("Action Complete");
//                    System.out.println(odometry.getPulseA());
//                    odometry.reset();
//                    break;
//                }
//            }
//        }
//
//        Move.stopEngine();
//    }
//
//    /*
//     * Change this method according to your robot send the acknowledgement when agent completes the action
//     */
//    @Override
//    public boolean executeAction(String agName, Structure action) {
//
//        GlobalVariable.currentAction = action.getFunctor();
//
//        logger.info("executing: " + action + ", but not implemented!");
//        int angle = 0;
//        double distance = 0.0;
//
//        if (action.getFunctor().equals("forward")) {
//            System.out.println("inside forward");
//            angle = 0;
//            distance = 230.2;
//        } else if (action.getFunctor().equals("right")) {
//            System.out.println("inside right");
//            angle = 30;
//            distance = 900;
//        } else if (action.getFunctor().equals("left")) {
//            System.out.println("Inside left");
//            angle = -30;
//            distance = 911.7071;
//        }
//        performMoveAction(distance, angle);
//
//        if (true) { // you may improve this condition
//            informAgsEnvironmentChanged();
//        }
//        if (!action.getFunctor().equals("stopRobot")) {
//            updatePercept(agName);
//        } else {
//            Move.steer(0);
//            pointCounter = 1;
//        }
//
//        return true; // the action was executed with success
//    }
//
//    /**
//     * Called before the end of MAS execution
//     */
//    @Override
//    public void stop() {
//        // pointCounter=1;
//        System.out.println("Inside stop MAS");
//
//        super.stop();
//    }
//}
