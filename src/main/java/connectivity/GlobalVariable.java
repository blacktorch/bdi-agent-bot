package connectivity;

import enums.Pole;

public final class GlobalVariable {
    private GlobalVariable(){
        //do not init..
    }
    public static String[] args;
    public static String currentPosition = "0,0";
    public static String currentAction = "stopRobot";
    public static double ultraSonicData = -1.0;
    public static boolean hasCurrentHeading = false;
    public static Pole pole = Pole.CENTER;
}
