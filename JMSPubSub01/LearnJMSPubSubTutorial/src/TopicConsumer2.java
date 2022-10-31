import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TopicConsumer2 implements MessageListener{
	
	public static void main(String[] args) {
		System.out.println("------------------Entering JMS Example Topic Consumer2------------------------");
		Context context = null;
		TopicConnection topicConnection = null;
		try {
			Properties props	= new Properties();
			props.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
			props.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming");
			props.setProperty("java.naming.provider.url", "localhost:1099");
			context = new InitialContext(props);
			TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory)context.lookup("ConnectionFactory");
			Topic topic = (Topic)context.lookup("topic/JMS_PubSub_Tutorial1");
			topicConnection = topicConnectionFactory.createTopicConnection();
			TopicSession topicSession = topicConnection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
			topicSession.createSubscriber(topic).setMessageListener(new TopicConsumer2());
			topicConnection.start();
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		} finally {
			try {
				if(topicConnection != null) topicConnection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
			try {
				if(context != null) context.close();
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		System.out.println("------------------Exiting JMS Example Topic Consumer2------------------------");
	}

	@Override
	public void onMessage(Message message) {
		try {
			System.out.println("Incoming Messages : " + ((TextMessage)message).getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
}
