
import agent.AgentBot;

public class Main {
    public static void main(String[] args) {
        try {
            new Thread(() -> {
                AgentBot agent = new AgentBot();
                agent.run();
            }).start();
        } catch (Exception e){
            System.out.println("Encountered problem running agent reasoning!");
        }

    }
}
