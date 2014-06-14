import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
 
public class PiBot {
 
 /**
  * @param args
  */
  static GpioController gpio = GpioFactory.getInstance();
 
  //range finder pins
 
  static GpioPinDigitalOutput rangefindertrigger = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_11, "Range Finder Trigger", PinState.LOW);
 
  static GpioPinDigitalInput rangefinderresult = gpio.provisionDigitalInputPin(RaspiPin.GPIO_10, "Range Pulse Result", PinPullResistance.PULL_DOWN);
 public static void main(String[] args) {
 
  // Setup GPIO Pins 
  
 
  // Create the range finder
  
 
  do {
  // Get the range
  double distance = getRange();
 
  System.out.println("RangeFinder result ="+distance +"mm");
  
  
    try {
   // fire the trigger pulse 
  
    
   Thread.sleep(500);
  } catch (InterruptedException e) {
    
   e.printStackTrace();
   System.out.println("Exception triggering range finder");
  }
  } while (false!=true);
   
   
 
 }
  public static double getRange() {
	  double result = 0;
  System.out.println("Range Finder Triggered");
  try {
   // fire the trigger pulse 
   rangefindertrigger.high();
    
   Thread.sleep(20);
  } catch (InterruptedException e) {
    
   e.printStackTrace();
   System.out.println("Exception triggering range finder");
  }
  rangefindertrigger.low();
 
  // wait for the result
   
  double startTime = System.currentTimeMillis();
  double stopTime = 0;
  do {
    
   stopTime = System.currentTimeMillis();
   if ((System.currentTimeMillis() - startTime) >= 40) {
    break;
   }
  } while (rangefinderresult.getState() != PinState.HIGH);
 
  // calculate the range. If the loop stopped after 38 ms set the result
  // to -1 to show it timed out.
 
  if ((stopTime - startTime) <= 38) {
   result = (stopTime - startTime) * 165.7;
   System.out.println(result+" "+(stopTime-startTime) );
  } else {
   System.out.println("Timed out");
   result = -1;
  }
 //
  return result;
 
 }
}
