package wsinteg.quartz_xml;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.jobs.ee.jms.JmsHelper;
import org.quartz.jobs.ee.jms.JmsMessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActiveMQJob implements Job {

    private Session session;
    private Connection connection;
	protected Logger logger = LoggerFactory.getLogger(getClass());

    public ActiveMQJob() {
	      try {
              // Create a ConnectionFactory
              ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("karaf", "karaf", "vm://amq-broker");

              // Create a Connection
              connection = connectionFactory.createConnection();
              connection.start();

              // Create a Session
              session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	      }
          catch (Exception e) {
              logger.error("Caught: " + e.getLocalizedMessage());
              e.printStackTrace();
          }
	}
	
	public void connect(String queue, String from, String to) {
		try {
              // Create a message
              String text = String.format("connect %s to %s", from, to);
              TextMessage message = session.createTextMessage(text);

	          // Create a MessageProducer from the Session to the Topic or Queue
	          MessageProducer producer = session.createProducer(session.createQueue(queue));
			  producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
              producer.send(message);
        }
        catch (Exception e) {
            logger.error("Caught: " + e.getMessage());
        }
	}
	
	public void close() {
		try {
              // Clean up
              session.close();
              connection.close();
          }
          catch (Exception e) {
              logger.error("Caught: " + e.getMessage());
          }
	}

	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap dataMap = context.getMergedJobDataMap();
		try {
			MessageProducer producer = session.createProducer(session.createQueue(dataMap.getString("jms.queue")));
			JmsMessageFactory messageFactory = (JmsMessageFactory)
			Class.forName(dataMap.getString(JmsHelper.JMS_MSG_FACTORY_CLASS_NAME)).newInstance();
			Message msg = messageFactory.createMessage(dataMap, session);
			producer.send(msg);
		} catch (JMSException e) {
            logger.error("Caught: " + e.getMessage());
		} catch (InstantiationException e) {
            logger.error("Caught: " + e.getMessage());
		} catch (IllegalAccessException e) {
            logger.error("Caught: " + e.getMessage());
		} catch (ClassNotFoundException e) {
            logger.error("Caught: " + e.getMessage());
		}
	}
}