package environments;

import com.github.sarxos.webcam.*;

public class DetectMotion  implements WebcamMotionListener {

    public DetectMotion(){
        WebcamMotionDetector detector = new WebcamMotionDetector(Webcam.getDefault());
        detector.setInterval(500); // one check per 500 ms
        detector.addMotionListener(this);
        detector.start();
        System.out.println("Started");
    }

    @Override
    public void motionDetected(WebcamMotionEvent wme) {
       //
    }

}
