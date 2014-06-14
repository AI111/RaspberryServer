import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalMultipurpose;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class ReciverAMQP {
	
	private static final String EXCHANGE_NAME = "topic_logs";
	private static final String[] topics = {
			"raspberry.gpio.out",
			"raspberry.gpio.in",
			"android.gpio.out",
			"android.gpio.in",
			"raspberry.sennsore.out",
			"raspberry.sennsore.in"			
		};
		private static final String[] sensors={
			"HC_SR501",
			"DHT11"};

	public static Channel channel = null,chanel1=null;
	public static void main(String[] argv) {
	  GpioController gpio = GpioFactory.getInstance();
	  GpioPinDigitalMultipurpose pin[];
    Connection connection = null;
	Sonar sonar = new Sonar();
   sonar.setup();
    try {
      ConnectionFactory factory = new ConnectionFactory();
    //  if(argv)
      factory.setHost(argv[0]);
      
		PinMode pinMode = PinMode.DIGITAL_OUTPUT;

		pin = new GpioPinDigitalMultipurpose[]{ 
			(GpioPinDigitalMultipurpose) gpio.provisionDigitalMultipurposePin(RaspiPin.GPIO_00, pinMode, PinPullResistance.PULL_DOWN),
			(GpioPinDigitalMultipurpose) gpio.provisionDigitalMultipurposePin(RaspiPin.GPIO_01, pinMode, PinPullResistance.PULL_DOWN),
			(GpioPinDigitalMultipurpose) gpio.provisionDigitalMultipurposePin(RaspiPin.GPIO_02, pinMode, PinPullResistance.PULL_DOWN),
			(GpioPinDigitalMultipurpose) gpio.provisionDigitalMultipurposePin(RaspiPin.GPIO_03, pinMode, PinPullResistance.PULL_DOWN),
			(GpioPinDigitalMultipurpose) gpio.provisionDigitalMultipurposePin(RaspiPin.GPIO_04, pinMode, PinPullResistance.PULL_DOWN),
			(GpioPinDigitalMultipurpose) gpio.provisionDigitalMultipurposePin(RaspiPin.GPIO_05, pinMode, PinPullResistance.PULL_DOWN)

          };
          for(GpioPinDigitalMultipurpose p:pin){
			  addPinStateChangeListener(p);
			  }
			  
			 
      connection = factory.newConnection();
      channel = connection.createChannel();
		chanel1=connection.createChannel();
      channel.exchangeDeclare(EXCHANGE_NAME, "topic");
      String queueName = channel.queueDeclare().getQueue();

		
		for(String bindingKey : topics){    
        channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
     }
    
      System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

      QueueingConsumer consumer = new QueueingConsumer(channel);
      channel.basicConsume(queueName, true, consumer);
		int num;
		
      while (true) {
        QueueingConsumer.Delivery delivery = consumer.nextDelivery();
        String message = new String(delivery.getBody());
        String routingKey = delivery.getEnvelope().getRoutingKey();
       /// if(routingKey.equals("raspberry/gpio")){}
		
		System.out.println(" [x] Received '" + routingKey + "':'" + message + "'");  
		if(routingKey.equals(topics[0]))
		{
			String[] arr = message.split(" ");
			num=Integer.parseInt(arr[0]);
			if(arr[1].equals("1")&&pin[num].isMode(PinMode.DIGITAL_OUTPUT))
				{
				pin[num].high();
				System.out.println("ON "+Integer.parseInt(arr[0]));  
				}
			else
				{
				if(pin[num].isMode(PinMode.DIGITAL_INPUT))
				{
					pin[num].setMode(PinMode.DIGITAL_OUTPUT);
				}
				pin[num].low();
				System.out.println("OFF "+Integer.parseInt(arr[0]));  
				}
		
		}
		else if(routingKey.equals(topics[1]))
		{	 
			String[] arr = message.split(" ");	
			
			num=Integer.parseInt(arr[0]);
			if(arr[1].equals("0")&&pin[num].isMode(PinMode.DIGITAL_OUTPUT))
			{
				pin[num].setMode(PinMode.DIGITAL_INPUT);
				System.out.println("INPUT "+num );  
			}
				
			
		}else if(routingKey.equals(topics[4]))
			{ int d =sonar.getDistance();
				System.out.println("Sensore " +message+" "+d); 
				String[] arr = message.split(" ");	
				if(arr[0].equals(sensors[0]))
				{
					chanel1.basicPublish(EXCHANGE_NAME, topics[5], null, new String(arr[1]+" "+d).getBytes());
					System.out.println("INPUT "+d ); 
					//System.out.println(" --> send: " + (event.getPin().getPin().getAddress()) + " = " + event.getState());
				}
			/*	try 
				{
					chanel1.basicPublish(EXCHANGE_NAME, topics[3], null, new String()
					.getBytes());
					System.out.println(" --> send: " + (event.getPin().getPin().getAddress()) + " = " + event.getState());
				}
				catch  (Exception e) {
					e.printStackTrace();
				}
				*/
			}
      }
      
    }
    catch  (Exception e) {
      e.printStackTrace();
    }
    finally {
      if (connection != null) {
        try {
          connection.close();
        }
        catch (Exception ignore) {}
      }
    }
  }
  private static void addPinStateChangeListener(GpioPinDigitalMultipurpose pin) {
        pin.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                System.out.println(" --> GPIO PIN STATE CHANGE: " + (event.getPin().getPin().getAddress()) + " = " + event.getState());
                if(event.getPin().isMode(PinMode.DIGITAL_INPUT))
					{
						try 
						{
						chanel1.basicPublish(EXCHANGE_NAME, topics[3], null, 
						((event.getPin().getPin().getAddress())+" "+(event.getState().isLow()?0:1)).getBytes());
						System.out.println(" --> send: " + (event.getPin().getPin().getAddress()) + " = " + event.getState());
						}
						catch  (Exception e) {
						e.printStackTrace();
						}
					}
                
            }
        });
    }
    
}
