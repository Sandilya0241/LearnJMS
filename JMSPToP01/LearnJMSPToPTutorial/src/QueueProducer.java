import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class QueueProducer {

	public static void main(String[] args) {
		Context context = null; 
		QueueConnection queueConnection = null;
		QueueSession queueSession = null;
		System.out.println("------------------Entering Queue Producer Main()------------------");
		context = QueueConsumer.getInitialContext();
		try {
			QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) context.lookup("ConnectionFactory");
			Queue queue = (Queue)context.lookup("queue/JMS_PtToPt_Tutorial2");
			queueConnection = queueConnectionFactory.createQueueConnection();
			queueSession = queueConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			queueConnection.start();
			QueueProducer queueProducer = new QueueProducer();
			queueProducer.sendMessage("Message #1 from Queue Producer...", queueSession, queue);
			
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
		
		System.out.println("------------------Exiting Queue Producer Main()------------------");
	}
	
	public void sendMessage(String text, QueueSession queueSession, Queue queue) {
		try {
			QueueSender queueSender = queueSession.createSender(queue);
			TextMessage textMessage = queueSession.createTextMessage(text);
			queueSender.send(textMessage);
			queueSender.close();
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
