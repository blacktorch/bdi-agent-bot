import actuators.Servo;
import agent.Brain;
import connectivity.DDSBase;
import connectivity.GlobalVariable;
import connectivity.Publish;
import connectivity.Subscribe;
import constants.ActuatorConstants;
import constants.SensorConstants;
import enums.Pole;
import environments.Point;
import jason.JasonException;
import jason.infra.centralised.RunCentralisedMAS;
import sensors.Odometry;
import sensors.UltrasonicSensor;

import java.io.*;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static com.pi4j.wiringpi.Gpio.delay;

public class Main implements Observer {

    private static Odometry odometry = Odometry.getInstance();
    private static final double RIGHT_TURN_DISTANCE = 911;
    private static final double LEFT_TURN_DISTANCE = 911.7071;
    private static final double SINGLE_CELL_SPACE = 230.2;


    public static void main(String[] args) throws IOException {
        DDSBase.getInstance().setup(args, 10);

        UltrasonicSensor ultrasonicSensor = new UltrasonicSensor(SensorConstants.ULTRASONIC_ECHO_PIN,
                SensorConstants.ULTRASONIC_TRIGGER_PIN, SensorConstants.REJECTION_START,
                SensorConstants.REJECTION_TIME);

        GlobalVariable.args = args;
        Servo wheel = new Servo(ActuatorConstants.STEER_SERVO_CHANNEL);
        wheel.writeAngle(100);
        delay(100);
        Pole pole = Pole.EAST;

        Main m = new Main();

        new Thread(() -> {
            Publish publish = new Publish.Builder().setDataType("Telemetry")
                    .setDomainID(10)
                    .setTopicString("Sensor Data")
                    .build();
            publish.setUp();
            System.out.println("Publisher started");

                while (true) {

                    try {
                        GlobalVariable.ultraSonicData = ultrasonicSensor.getDistance();
                        String[] data = {GlobalVariable.currentPosition, String.valueOf(GlobalVariable.ultraSonicData),
                                String.valueOf(Odometry.getInstance().getTotalDistanceMoved()), GlobalVariable.currentAction};
                        publish.write("agent1", data, 10);

                        Thread.sleep(50);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

        }).start();

        Subscribe subscriber = new Subscribe();
        subscriber.addObserver(m);

        new Thread(() -> {
            try {
                subscriber.start(args);
                System.out.println("Subscriber is started");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();

    }


    @Override
    public void update(Observable o, Object arg) {
        System.out.println("===============UPDATE=====================");
        new Thread(() -> {
            try {

                List<Point> p = (List<Point>) arg;
                Subscribe sub = (Subscribe) o;

                Point[] points = new Point[p.size()];
                for (int i = 0; i < points.length; i++) {
                    points[i] = p.get(i);
                }
                File file = new File("objectState.txt");
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(points);
                objectOutputStream.flush();
                objectOutputStream.close();
                fileOutputStream.close();

                Thread thread=new Thread(new Brain());
                thread.run();

                System.out.println("Stop Reasoning inside main class");

                if (GlobalVariable.hasCurrentHeading){
                    new Thread(() -> {
                        Publish publish = new Publish.Builder().setDataType("MissionPlan")
                                .setDomainID(10)
                                .setTopicString("Mission Plan")
                                .build();
                        publish.setUp();

                        String[] data = {GlobalVariable.currentPosition, points[points.length-1].toString()};
                        publish.write("agent", data, 99);

                    }).start();
                }
                sub.setNewDataReceived(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
