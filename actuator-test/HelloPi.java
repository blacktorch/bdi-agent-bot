//import com.pi4j.io.gpio.*;
//import com.pi4j.wiringpi.Gpio;
//import com.pi4j.wiringpi.SoftPwm;
//
//public class HelloPi {
//
//    private static final int MOTOR1_PWM = 7;
//    private static final int MOTOR2_PWM = 0;
//    private static final Pin MOTOR1_DIR = RaspiPin.GPIO_15;
//    private static final Pin MOTOR2_DIR = RaspiPin.GPIO_16;
//    private static final Pin LINE_R = RaspiPin.GPIO_27;
//
//    public static void main(String[] args) {
//
////        sensors.UltrasonicSensor sonic =new sensors.UltrasonicSensor(
////                10,//ECO PIN (physical 11)
////                14,//TRIG PIN (pysical 22)
////                1000,//REJECTION_START ; long (nano seconds)
////                23529411 //REJECTION_TIME ; long (nano seconds)
////        );
////        System.out.println("Start");
////        while(true){
////            try {
////                System.out.println("distance "+ sonic.getDistance()+"mm");
////                Thread.sleep(100); //1s
////            } catch (Exception ex){
////                ex.printStackTrace();
////            }
////
////        }
//
//        GpioController gpio = GpioFactory.getInstance();
////
////        SoftPwm.softPwmCreate(MOTOR1_PWM, 100, 100);
////        SoftPwm.softPwmCreate(MOTOR2_PWM, 100, 100);
////        GpioPinDigitalOutput motor1Dir = gpio.provisionDigitalOutputPin(MOTOR1_DIR, "Motor1Dir");
////        GpioPinDigitalOutput motor2Dir = gpio.provisionDigitalOutputPin(MOTOR2_DIR, "Motor2Dir");
//        GpioPinDigitalInput lineA = gpio.provisionDigitalInputPin(LINE_R);
////        System.out.println("forward");
////        motor1Dir.setState(PinState.LOW);
////        motor2Dir.setState(PinState.HIGH);
////        SoftPwm.softPwmWrite(MOTOR1_PWM, 100);
////        SoftPwm.softPwmWrite(MOTOR2_PWM, 100);
//////        Gpio.pinMode(15, Gpio.OUTPUT);
//////        Gpio.pinMode(16, Gpio.OUTPUT);
//////
//////        Gpio.digitalWrite(15, Gpio.HIGH);
//////        Gpio.digitalWrite(16, Gpio.LOW);
////
////        //gpio.setState(PinState.LOW, motor2Dir);
////        //SoftPwm.softPwmWrite(MOTOR1_PWM, 100);
////        //SoftPwm.softPwmWrite(MOTOR2_PWM, 100);
////        Gpio.delay(2000);
////        System.out.println("back");
////        motor1Dir.setState(PinState.HIGH);
////        motor2Dir.setState(PinState.LOW);
////        SoftPwm.softPwmWrite(MOTOR1_PWM, 100);
////        SoftPwm.softPwmWrite(MOTOR2_PWM, 100);
////        Gpio.delay(2000);
////        System.out.println("spin");
////        motor1Dir.setState(PinState.HIGH);
////        motor2Dir.setState(PinState.HIGH);
////        SoftPwm.softPwmWrite(MOTOR1_PWM, 100);
////        SoftPwm.softPwmWrite(MOTOR2_PWM, 100);
////        Gpio.delay(2000);
////        System.out.println("stop");
////        SoftPwm.softPwmWrite(MOTOR1_PWM, 0);
////        SoftPwm.softPwmWrite(MOTOR2_PWM, 0);
////
//
//
//
//        while (true){
//            System.out.println(gpio.getState(lineA).isHigh());
//            Gpio.delay(1000);
//        }
//
//        //gpio.shutdown();
//    }
//}


//public static void main__(String... args) throws I2CFactory.UnsupportedBusNumberException {
//        int freq = 60;
//        if (args.length > 0) {
//        freq = Integer.parseInt(args[0]);
//        }
//        PCA9685 servoBoard = new PCA9685();
//        servoBoard.setPWMFreq(freq); // Set frequency to 60 Hz by default
//        int servoMin = 122; // min value for servos like https://www.adafruit.com/product/169 or https://www.adafruit.com/product/155
//        int servoMax = 615; // max value for servos like https://www.adafruit.com/product/169 or https://www.adafruit.com/product/155
//
//final int CONTINUOUS_SERVO_CHANNEL = 0;
//final int STANDARD_SERVO_CHANNEL   = 1;
//
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//        servoBoard.setPWM(STANDARD_SERVO_CHANNEL, 0, 0); // Stop the standard one
//        servoBoard.setPWM(CONTINUOUS_SERVO_CHANNEL, 0, 0); // Stop the continuous one
//        }));
//
//        for (int i = 0; true && i < 5; i++) {
//        System.out.println("i=" + i);
//        servoBoard.setPWM(STANDARD_SERVO_CHANNEL, 0, servoMin);
//        servoBoard.setPWM(CONTINUOUS_SERVO_CHANNEL, 0, servoMin);
//        delay(1_000);
//        servoBoard.setPWM(STANDARD_SERVO_CHANNEL, 0, servoMax);
//        servoBoard.setPWM(CONTINUOUS_SERVO_CHANNEL, 0, servoMax);
//        delay(1_000);
//        }
//        servoBoard.setPWM(CONTINUOUS_SERVO_CHANNEL, 0, 0); // Stop the continuous one
//        servoBoard.setPWM(STANDARD_SERVO_CHANNEL, 0, 0);   // Stop the standard one
//
//        for (int i = servoMin; i <= servoMax; i++) {
//        System.out.println("i=" + i);
//        servoBoard.setPWM(STANDARD_SERVO_CHANNEL, 0, i);
//        delay(10);
//        }
//        for (int i = servoMax; i >= servoMin; i--) {
//        System.out.println("i=" + i);
//        servoBoard.setPWM(STANDARD_SERVO_CHANNEL, 0, i);
//        delay(10);
//        }
//
//        servoBoard.setPWM(CONTINUOUS_SERVO_CHANNEL, 0, 0); // Stop the continuous one
//        servoBoard.setPWM(STANDARD_SERVO_CHANNEL, 0, 0);   // Stop the standard one
//
//        for (int i = servoMin; i <= servoMax; i++) {
//        System.out.println("i=" + i);
//        servoBoard.setPWM(CONTINUOUS_SERVO_CHANNEL, 0, i);
//        delay(10);
//        }
//        for (int i = servoMax; i >= servoMin; i--) {
//        System.out.println("i=" + i);
//        servoBoard.setPWM(CONTINUOUS_SERVO_CHANNEL, 0, i);
//        delay(10);
//        }
//
//        servoBoard.setPWM(CONTINUOUS_SERVO_CHANNEL, 0, 0); // Stop the continuous one
//        servoBoard.setPWM(STANDARD_SERVO_CHANNEL, 0, 0);   // Stop the standard one
//
//        if (true) {
//        System.out.println("Now, servoPulse");
//        servoBoard.setPWMFreq(250);
//        // The same with setServoPulse
//        for (int i = 0; i < 5; i++) {
//        servoBoard.setServoPulse(STANDARD_SERVO_CHANNEL, 1f);
//        servoBoard.setServoPulse(CONTINUOUS_SERVO_CHANNEL, 1f);
//        delay(1_000);
//        servoBoard.setServoPulse(STANDARD_SERVO_CHANNEL, 2f);
//        servoBoard.setServoPulse(CONTINUOUS_SERVO_CHANNEL, 2f);
//        delay(1_000);
//        }
//        // Stop, Middle
//        servoBoard.setServoPulse(STANDARD_SERVO_CHANNEL, 1.5f);
//        servoBoard.setServoPulse(CONTINUOUS_SERVO_CHANNEL, 1.5f);
//
//        servoBoard.setPWM(CONTINUOUS_SERVO_CHANNEL, 0, 0); // Stop the continuous one
//        }
//
//        System.out.println("Done with the demo.");
//        }

//public static void main___(String... args) throws I2CFactory.UnsupportedBusNumberException {
//        int freq = 60;
//        if (args.length > 0) {
//        freq = Integer.parseInt(args[0]);
//        }
//        PCA9685 servoBoard = new PCA9685();
//        servoBoard.setPWMFreq(freq); // Set frequency to 60 Hz
//        int servoMin = getServoMinValue(freq);   // 130;   // was 150. Min pulse length out of 4096
//        int servoMax = getServoMaxValue(freq);   // was 600. Max pulse length out of 4096
//
//final int CONTINUOUS_SERVO_CHANNEL = 0;
//final int STANDARD_SERVO_CHANNEL = 1;
//
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//        servoBoard.setPWM(STANDARD_SERVO_CHANNEL, 0, 0); // Stop the standard one
//        servoBoard.setPWM(CONTINUOUS_SERVO_CHANNEL, 0, 0); // Stop the continuous one
//        }));
//
//        System.out.println(String.format("min: %d, max: %d", servoMin, servoMax));
//        for (int i = 0; true && i < 5; i++) {
//        System.out.println("i=" + i);
//        servoBoard.setPWM(STANDARD_SERVO_CHANNEL, 0, servoMin);
//        servoBoard.setPWM(CONTINUOUS_SERVO_CHANNEL, 0, servoMin);
//        delay(1_000);
//        servoBoard.setPWM(STANDARD_SERVO_CHANNEL, 0, servoMax);
//        servoBoard.setPWM(CONTINUOUS_SERVO_CHANNEL, 0, servoMax);
//        delay(1_000);
//        }
//        servoBoard.setPWM(CONTINUOUS_SERVO_CHANNEL, 0, 0); // Stop the continuous one
//        servoBoard.setPWM(STANDARD_SERVO_CHANNEL, 0, 0);   // Stop the standard one
//        delay(1_000);
//
//        System.out.println("With hard coded values (Suitable for 60 Hz)");
//        servoMin = 122;
//        servoMax = 615;
//        System.out.println(String.format("min: %d, max: %d", servoMin, servoMax));
//        for (int i = 0; true && i < 5; i++) {
//        System.out.println("i=" + i);
//        servoBoard.setPWM(STANDARD_SERVO_CHANNEL, 0, servoMin);
//        servoBoard.setPWM(CONTINUOUS_SERVO_CHANNEL, 0, servoMin);
//        delay(1_000);
//        servoBoard.setPWM(STANDARD_SERVO_CHANNEL, 0, servoMax);
//        servoBoard.setPWM(CONTINUOUS_SERVO_CHANNEL, 0, servoMax);
//        delay(1_000);
//        }
//
//        servoBoard.setPWM(CONTINUOUS_SERVO_CHANNEL, 0, 0); // Stop the continuous one
//        servoBoard.setPWM(STANDARD_SERVO_CHANNEL, 0, 0);   // Stop the standard one
//        System.out.println("Ouala");
//        }

//Servo camServo = new Servo(ActuatorConstants.CAMERA_SERVO_CHANNEL);
//
//        camServo.writeAngle(180);
//        delay(1000);
//        camServo.turnOff();
//        camServo.travel(60, 4000);
//        camServo.travel(125, 4000);
//        camServo.travel(60, 4000);
//        camServo.travel(125, 4000);
//        camServo.turnOff();