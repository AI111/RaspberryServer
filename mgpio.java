import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalMultipurpose;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/*
 *
 * @author Jens Deters
 */
public class mgpio {

    public static void main(String args[]) throws InterruptedException {
        System.out.println("<--Pi4J--> GPIO Listen Example ...");

        final GpioController gpio = GpioFactory.getInstance();

        PinMode pinMode = PinMode.DIGITAL_INPUT;
        GpioPinDigitalMultipurpose pin0 = (GpioPinDigitalMultipurpose) gpio.
                provisionDigitalMultipurposePin(RaspiPin.GPIO_00, pinMode, PinPullResistance.PULL_UP);
        GpioPinDigitalMultipurpose pin1 = (GpioPinDigitalMultipurpose) gpio.
                provisionDigitalMultipurposePin(RaspiPin.GPIO_01, pinMode, PinPullResistance.PULL_DOWN);
        GpioPinDigitalMultipurpose pin2 = (GpioPinDigitalMultipurpose) gpio.
                provisionDigitalMultipurposePin(RaspiPin.GPIO_02, pinMode, PinPullResistance.PULL_DOWN);
        GpioPinDigitalMultipurpose pin3 = (GpioPinDigitalMultipurpose) gpio.
                provisionDigitalMultipurposePin(RaspiPin.GPIO_03, pinMode, PinPullResistance.PULL_UP);
        GpioPinDigitalMultipurpose pin4 = (GpioPinDigitalMultipurpose) gpio.
                provisionDigitalMultipurposePin(RaspiPin.GPIO_04, pinMode, PinPullResistance.PULL_DOWN);
        GpioPinDigitalMultipurpose pin5 = (GpioPinDigitalMultipurpose) gpio.
                provisionDigitalMultipurposePin(RaspiPin.GPIO_05, pinMode, PinPullResistance.PULL_DOWN);
        GpioPinDigitalMultipurpose pin6 = (GpioPinDigitalMultipurpose) gpio.
                provisionDigitalMultipurposePin(RaspiPin.GPIO_06, pinMode, PinPullResistance.PULL_DOWN);
        GpioPinDigitalMultipurpose pin7 = (GpioPinDigitalMultipurpose) gpio.
                provisionDigitalMultipurposePin(RaspiPin.GPIO_07, pinMode, PinPullResistance.PULL_DOWN);

        addPinStateChangeListener(pin0);

        addPinStateChangeListener(pin1);

        addPinStateChangeListener(pin2);

        addPinStateChangeListener(pin3);

        addPinStateChangeListener(pin4);

        addPinStateChangeListener(pin5);

        addPinStateChangeListener(pin6);
        
        pin3.setMode(PinMode.DIGITAL_OUTPUT);
		pin3.low();
        System.out.print("started!");
        System.out.println("Waiting for InputEvents...");
        pin3.setMode(PinMode.DIGITAL_INPUT);

        for (;;) {
            Thread.sleep(500);
        }

    }

    private static void addPinStateChangeListener(GpioPinDigitalMultipurpose pin) {
        pin.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
            }
        });
    }
}
