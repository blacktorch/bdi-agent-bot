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
