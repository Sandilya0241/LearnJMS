import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class QueueConsumer2 implements MessageListener{

	public static void main(String[] args) {
		Context context = null;
		QueueConnection queueConnection = null;
		QueueSession queueSession = null;
		System.out.println("------------------Entering Queue Consumer2 Main()------------------");
		context = QueueConsumer2.getInitialContext();
		try {
			QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) context.lookup("ConnectionFactory");
			Queue queue = (Queue)context.lookup("queue/JMS_PtToPt_Tutorial2");
			queueConnection = queueConnectionFactory.createQueueConnection();
			queueSession = queueConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			QueueReceiver queueReceiver = queueSession.createReceiver(queue);
			queueReceiver.setMessageListener(new QueueConsumer2());
			queueConnection.start();
			try {
				Thread.sleep(10000);
			} catch(InterruptedException ie) {
				ie.printStackTrace();
			}
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException je) {
			je.printStackTrace();
		} finally {
			try {
				if (context != null) context.close();
				if (queueConnection != null) queueConnection.close();
				if (queueSession != null) queueSession.close();
			} catch (NamingException ne) {
				ne.printStackTrace();
			}  catch (JMSException je) {
				je.printStackTrace();
			} 
		}
		System.out.println("------------------Exiting Queue Consumer2 Main()------------------");
	}

	@Override
	public void onMessage(Message message) {
		try {
			System.out.println("Incoming Messages to QueueConsumer2: " + ((TextMessage)message).getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	public static Context getInitialContext() {
		Context context = null;
		Properties props = new Properties();
		props.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
		props.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming");
		props.setProperty("java.naming.provider.url", "localhost:1099");
		try {
			context = new InitialContext(props);
		} catch (NamingException e) {
			e.printStackTrace();
		} 
		return context;
	}

}
