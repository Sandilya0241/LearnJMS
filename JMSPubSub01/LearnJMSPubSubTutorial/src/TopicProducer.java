import java.util.Properties;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TopicProducer {
	public static void main(String[] args) {
		Context context = null;
		TopicConnection topicConnection = null;
		System.out.println("------------------Entering JMS Example Topic Producer------------------------");
		try {
			Properties props	= new Properties();
			props.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
			props.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming");
			props.setProperty("java.naming.provider.url", "localhost:1099");
			context = new InitialContext(props);
			TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) context.lookup("ConnectionFactory");
			Topic topic = (Topic) context.lookup("topic/JMS_PubSub_Tutorial1");
			topicConnection = topicConnectionFactory.createTopicConnection();
			TopicSession topicSession = topicConnection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
			topicConnection.start();
			TopicProducer topicProducer = new TopicProducer();
			topicProducer.sendMessage("Message1 from Topic Producer...", topicSession, topic);
			topicProducer.sendMessage("Message2 from Topic Producer...", topicSession, topic);
			topicProducer.sendMessage("Message3 from Topic Producer...", topicSession, topic);
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
		System.out.println("------------------Exiting JMS Example Topic Producer------------------------");
	}
	
	public void sendMessage(String text, TopicSession topicSession, Topic topic) {
		try {
			TopicPublisher topicPublisher = topicSession.createPublisher(topic);
			TextMessage textMessage = topicSession.createTextMessage(text);
			topicPublisher.publish(textMessage);
			topicPublisher.close();
		} catch (JMSException e) {
			e.printStackTrace();
		} 
	}
}
