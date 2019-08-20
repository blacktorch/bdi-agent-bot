package utils;

/**
 * A util class for handling all connectivity utilities.
 * This is a final class with a private constructor and
 * those not need to be instantiated since it only contains
 * static members, fields and methods.
 *
 * @author      Chidiebere Onyedinma
 * @version     1.0
 */

import java.nio.charset.StandardCharsets;

public final class ConnectionUtils {

    /**
     * constant to shift and align data to be sent to the left position
     * */
    public static final int LEFT = 0;

    /**
     * constant to shift and align data to be sent to the right position
     * */
    public static final int RIGHT = 1;

    /**
     * default buffer size of the data to be sent
     * */
    public static final int BUFFER_SIZE = 1024;

    /**
     * private constructor, class not to be initialized
     * */
    private ConnectionUtils(){
        //do not init
    }

    /**
     * Method to align the data to be sent
     * @param data the string data/command to be sent
     * @param length the length of the data buffer
     * @param position the position to align the data to
     * @return returns the aligned data in byte array to be sent
     */
    public static byte[] shiftDataToSend(String data, int length, int position){
        int dataLength = data.length();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length-dataLength; i++){
            builder.append(" ");
        }

        switch (position){
            case LEFT:
                builder.insert(0, data);
                break;
            case RIGHT:
                builder.append(data);
                break;
        }

        return builder.toString().getBytes(StandardCharsets.UTF_8);
    }
}
