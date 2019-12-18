//package agent;
//
//import enums.Action;
//import jason.architecture.AgArch;
//import jason.asSemantics.ActionExec;
//import jason.asSemantics.Agent;
//import jason.asSemantics.TransitionSystem;
//import jason.asSyntax.Literal;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class AgentBot extends AgArch {
//    private static Logger logger = Logger.getLogger(AgentBot.class.getName());
//    private Brain brain;
//
//    public AgentBot(Brain brain){
//        this.brain = brain;
//        try {
//            Agent ag = new Agent();
//            new TransitionSystem(ag, null, null, this);
//            ag.initAg("asl/reasoning.asl");
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "Init error", e);
//        }
//    }
//
//    public void run() {
//        try {
//            while (isRunning()) {
//                // calls the Jason engine to perform one reasoning cycle
//                logger.fine("Reasoning....");
//                getTS().reasoningCycle();
//                if (getTS().canSleep())
//                    sleep();
//            }
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "Run error", e);
//        }
//    }
//
//    public String getAgName() {
//        return "Robot";
//    }
//
//    // this method just add some perception for the agent
//    @Override
//    public List<Literal> perceive() {
//        List<Literal> l = new ArrayList<>(brain.getPerceptions());
//        //l.add(Literal.parseLiteral("x(10)"));
//        return l;
//    }
//
//    // this method get the agent actions
//    @Override
//    public void act(ActionExec action) {
//        getTS().getLogger().info("Agent " + getAgName() + " is doing: " + action.getActionTerm());
//        // set that the execution was ok
//        brain.updateAction(Action.valueOf(action.getActionTerm().toString().toUpperCase()));
//        action.setResult(true);
//        actionExecuted(action);
//    }
//
//    @Override
//    public boolean canSleep() {
//        return true;
//    }
//
//    @Override
//    public boolean isRunning() {
//        return true;
//    }
//
//    // a very simple implementation of sleep
//    public void sleep() {
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {}
//    }
//
//    // Not used methods
//    // This simple agent does not need messages/control/...
//    @Override
//    public void sendMsg(jason.asSemantics.Message m) throws Exception {
//    }
//
//    @Override
//    public void broadcast(jason.asSemantics.Message m) throws Exception {
//    }
//
//    @Override
//    public void checkMail() {
//    }
//
//}
