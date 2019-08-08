package sensors;

import com.pi4j.io.gpio.*;

public class LightDependentResistor {
    private Pin ldrPin;
    private GpioController gpio = GpioFactory.getInstance();
    private GpioPinDigitalInput ldrInput;
    private boolean shut;
    private boolean open;

    public LightDependentResistor(Pin ldrPin){
        this.ldrPin = ldrPin;
        ldrInput = gpio.provisionDigitalInputPin(this.ldrPin, PinPullResistance.OFF);
    }

    public boolean isShut(){
        shut = gpio.getState(ldrInput).isLow();
        return shut;
    }

    public boolean isOpen(){
        open = gpio.getState(ldrInput).isHigh();
        return open;
    }
}
