package agent;

import actions.Move;
import com.pi4j.wiringpi.Gpio;
import constants.ActuatorConstants;
import enums.Action;
import jason.asSyntax.Literal;
import sensors.LineSensor;

import java.util.ArrayList;
import java.util.List;

public class Brain extends Thread {

    private List<Literal> perceptions;
    private Action action;
    private boolean isNewAction;
    private LineSensor lineSensor;

    public Brain(){
        perceptions = new ArrayList<>();
        action = Action.STOP;
        lineSensor = new LineSensor();
        isNewAction = true;
    }

    public void run(){
        startAgentReasoning();
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

        for (;;){

            performAction();
            if (isNewAction){
                perceptions.clear();
                perceptions.add(Literal.parseLiteral(lineSensor.senseLine().name().toLowerCase()));
            }

        }


    }

    private void startAgentReasoning(){
        try {
            new Thread(() -> {
                AgentBot agent = new AgentBot(Brain.this);
                agent.run();
            }).start();
        } catch (Exception e){
            System.out.println("Encountered problem running agent reasoning!");
        }
    }

    List<Literal> getPerceptions(){
        return perceptions;
    }

    void updateAction(Action ac){
        this.action = ac;
        isNewAction = true;
    }

    private void performAction(){
        switch (action){
            case STOP:
                Move.stopEngine();
                break;
            case TURN_LEFT:
                Move.turnLeft(ActuatorConstants.MOTOR_RADIUS, 100);
                isNewAction = false;
                break;
            case GO_FORWARD:
                Move.forward(40);
                isNewAction = false;
                break;
            case TURN_RIGHT:
                Move.turnRight(ActuatorConstants.MOTOR_RADIUS, 100);
                isNewAction = false;
                break;
            case GO_BACKWARD:
                Move.backward(40);
                isNewAction = false;
                break;
        }

        Gpio.delay(1);

    }


}
