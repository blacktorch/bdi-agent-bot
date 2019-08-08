package sensors;

import constants.SensorConstants;
import enums.Position;

public class LineSensor {

    private LightDependentResistor leftResistor;
    private LightDependentResistor rightResistor;
    private LightDependentResistor middleResistor;

    public LineSensor(){

        rightResistor = new LightDependentResistor(SensorConstants.RIGHT_LDR);
        leftResistor = new LightDependentResistor(SensorConstants.LEFT_LDR);
        middleResistor = new LightDependentResistor(SensorConstants.MIDDLE_LDR);

    }

    public Position senseLine(){
        if (rightResistor.isOpen() && middleResistor.isOpen() && leftResistor.isOpen()){
            return Position.LOST;
        } else if (rightResistor.isOpen() && middleResistor.isShut() && leftResistor.isOpen()){
            return Position.MIDDLE;
        } else if (rightResistor.isShut() && middleResistor.isOpen() && leftResistor.isOpen()){
            return Position.RIGHT;
        } else if (rightResistor.isOpen() && middleResistor.isOpen() && leftResistor.isShut()){
            return Position.LEFT;
        } else if (rightResistor.isShut() && middleResistor.isShut() && leftResistor.isOpen()){
            return Position.RIGHT;
        } else if (rightResistor.isOpen() && middleResistor.isShut() && leftResistor.isShut()){
            return Position.LEFT;
        }

        return Position.LOST;
    }
}
