
import actions.Move;
import actuators.Servo;
import agent.Brain;
import com.hopding.jrpicam.RPiCamera;
import com.hopding.jrpicam.exceptions.FailedToRunRaspistillException;
import connectivity.RobotServer;
import constants.ActuatorConstants;
import detection.Detector;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws FailedToRunRaspistillException, IOException, InterruptedException {
//        Brain brain = new Brain();
//        brain.start();

//        Detector detector = Detector.create("haarcascade_frontalface_default.xml");
//
//        RPiCamera camera = new RPiCamera();
//
//        camera.setWidth(300);
//        camera.setHeight(300);
//        camera.enableBurst();
//        camera.setTimeout(1);
//        camera.setQuality(50);
//        camera.turnOffPreview();
//
//        JFrame frame = new JFrame("Camera");
//        JPanel panel = new JPanel();
////        JLabel image = new JLabel();
////        ImageIcon icon = new ImageIcon();
//
//        frame.setResizable(false);
//        frame.setSize(300,300);
//        frame.add(panel);
//        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//
//        frame.setVisible(true);
//
//        BufferedImage img;
//        //Servo cam = new Servo(ActuatorConstants.CAMERA_SERVO_CHANNEL);
//
//        ArrayList<Rectangle> faces = new ArrayList<>();
//
////        try {
////            new Thread(() -> {
////                while (true){
////                    cam.travel(cam.getMinAngle(), 6000);
////                    cam.travel(cam.getMaxAngle(), 6000);
////                }
////            }).start();
////        } catch (Exception e){
////            System.out.println("Encountered problem running agent reasoning!");
////        }
//
//        while (true){
//
//            try {
//                img = camera.takeBufferedStill();
//                panel.getGraphics().drawImage(img, 0,0, frame);
//
//                faces.addAll(detector.getFaces(img, 1.2f, 1.1f, 0.1f, 1, true));
//
//                if (faces.size() > 0){
//                    panel.getGraphics().drawRect(faces.get(0).getBounds().x, faces.get(0).getBounds().y, faces.get(0).width, faces.get(0).height);
//                }
//
//                faces.clear();
//
//
//            } catch (IOException | NullPointerException | InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }

        RobotServer server = new RobotServer("192.168.1.99", 3480);
        server.start();


    }
}
