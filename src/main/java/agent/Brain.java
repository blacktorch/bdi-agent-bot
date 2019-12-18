package agent;

import actions.Move;
import com.pi4j.wiringpi.Gpio;
import connectivity.GlobalVariable;
import constants.ActuatorConstants;
import enums.Action;
import enums.Pole;
import environments.Point;
import jason.architecture.AgArch;
import jason.asSemantics.ActionExec;
import jason.asSemantics.Agent;
import jason.asSemantics.TransitionSystem;
import jason.asSyntax.Literal;
import sensors.LineSensor;
import sensors.Odometry;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Brain extends AgArch implements Runnable {
    private static Logger logger = Logger.getLogger(Brain.class.getName());
    List<Literal> perceptionList = new ArrayList<>();
    private Boolean running = true;
    Point[] points;
    static int pointCounter=1;
    Odometry odometry = Odometry.getInstance();


    @SuppressWarnings("deprecation")

    public Brain() {

        try {
            System.out.println("Inside brain constructor");
            try {
                LogManager.getLogManager().readConfiguration(new FileInputStream("logging.properties"));
            } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            Agent agent1 = new Agent();
            new TransitionSystem(agent1, null, null, this);
            agent1.initAg("asl/agent2.asl");

            /*
             *Deserializing the object
             */
            File file=new File("objectState.txt");
            System.out.println(running);
            FileInputStream fileInputStream=new FileInputStream(file);
            ObjectInputStream objectInputStream=new ObjectInputStream(fileInputStream);

            points=(Point[])objectInputStream.readObject();
            System.out.println("Point length is ");
            System.out.println(points.length);

            for(int i=0;i<points.length;i++){
                System.out.println(points[i].getX()+","+points[i].getY());
            }

            //Assuming that normalized waypoints are get once from DDS
            //start the agent
            //Add the initial position (0,0) in agent mental belief
            //Compute the initial orientation and add it inside agent mental belief
            //add the target location in agent mental belief
            perceptionList.add(Literal.parseLiteral("start"));

            if (GlobalVariable.hasCurrentHeading){
                perceptionList.add(Literal.parseLiteral("orientation("+GlobalVariable.pole.name().toLowerCase()+")"));
                System.out.println("I HAVE NEW POINTS YEAH!!!!!!!!!");
                Thread.sleep(5000);
            } else {
                if(points[1].getX() != points[0].getX()) {
                    if(points[1].getX() == (points[0].getX() - 1)) {
                        perceptionList.add(Literal.parseLiteral("orientation(west)"));
                    }
                    else {
                        perceptionList.add(Literal.parseLiteral("orientation(east)"));
                    }
                }
                else {
                    if(points[1].getY() == (points[0].getY() - 1)) {
                        perceptionList.add(Literal.parseLiteral("orientation(south)"));
                    }
                    else {
                        perceptionList.add(Literal.parseLiteral("orientation(north)"));
                    }
                }
            }

            //Add the targetLocation
            perceptionList.add(Literal.parseLiteral("targetLoc("+points[points.length-1].getX()+","
                    +points[points.length-1].getY()+")"));


        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Init error", ex);
        }
    }

    public void run() {
        try {

            while (isRunning()) {
                //System.out.println("Inside isRunning");
                getTS().reasoningCycle();
                if (getTS().canSleep())
                    sleep();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Run error", e);
        }
    }

    public String getAgName() {
        return "Robot";
    }

    @Override
    public List<Literal> perceive() {
        //System.out.println("Inside perceive method");
        System.out.println(perceptionList.isEmpty());
        return perceptionList;
    }

    @Override
    public void act(ActionExec action) {
        GlobalVariable.currentAction = action.getActionTerm().getFunctor();

        logger.info("executing: " + action + ", but not implemented!");
        int angle;
        double distance ;

        if (action.getActionTerm().getFunctor().equals("forward")) {
            System.out.println("inside forward");
            angle = 0;
            distance = 230.2;
            performMoveAction(distance, angle);
            updatePercept();
        } else if (action.getActionTerm().getFunctor().equals("right")) {
            System.out.println("inside right");
            angle = 30;
            distance = 900;
            performMoveAction(distance, angle);
            updatePercept();
        } else if (action.getActionTerm().getFunctor().equals("left")) {
            System.out.println("Inside left");
            angle = -30;
            distance = 911.7071;
            performMoveAction(distance, angle);
            updatePercept();
        } else if(action.getActionTerm().getFunctor().equals("targetReach")){
            angle = 0;
            distance = 0;
            performMoveAction(distance, angle);
            Move.steer(0);
            pointCounter = 1;
            GlobalVariable.hasCurrentHeading = false;
            running=false;
        } else if(action.getActionTerm().getFunctor().equals("brake")){
            GlobalVariable.hasCurrentHeading = true;
            running=false;
        } else if(action.getActionTerm().getFunctor().equals("started")){
            //perceptionList.add(Literal.parseLiteral(curre))
            updatePercept();
        }

        GlobalVariable.pole = getCurrentHeading();

        System.out.println("Current Heading: "+ GlobalVariable.pole.name());

        // set that the execution was ok
        action.setResult(true);
        actionExecuted(action);

    }

    @Override
    public boolean canSleep() {
        return true;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    // a very simple implementation of sleep
    public void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
    }

    private void updatePercept() {
        perceptionList.clear();
        System.out.println("Inside update percept method");
        if(pointCounter < points.length ) {
            GlobalVariable.currentPosition = points[pointCounter - 1].toString();
            perceptionList.add(Literal.parseLiteral("nextLoc("+points[pointCounter].getX()+","+points[pointCounter].getY()+")"));
            perceptionList.add(Literal.parseLiteral("obstacle("+ GlobalVariable.ultraSonicData+")"));
            pointCounter++;
        }

    }

    private void performMoveAction(double distance, int angle) {
        if (!(GlobalVariable.ultraSonicData >= 0.0 && GlobalVariable.ultraSonicData <= 30.0)) {
            Move.steer(angle);
            odometry.reset();
            if (!Move.getIsMoving()) {
                Move.forward(30);
            }

            while (true) {
                if (odometry.getDistanceMoved() >= distance || (GlobalVariable.ultraSonicData >= 0.0 && GlobalVariable.ultraSonicData <= 30.0)) {
                    System.out.println("Action Complete");
                    System.out.println(odometry.getPulseA());
                    odometry.reset();
                    break;
                }
            }
        }

        Move.stopEngine();
    }

    private Pole getCurrentHeading(){
        Iterator<Literal> iterator=getTS().getAg().getBB().iterator();
        while(iterator.hasNext()) {
            Literal l=iterator.next();
            if(l.getFunctor().contains("orientation")) {
                if (l.toString().contains("east")){
                    return Pole.EAST;
                } else if (l.toString().contains("west")){
                    return Pole.WEST;
                } else if (l.toString().contains("north")){
                    return Pole.NORTH;
                } else if (l.toString().contains("south")){
                    return Pole.SOUTH;
                }
            }
        }
        return Pole.CENTER;
    }


}
