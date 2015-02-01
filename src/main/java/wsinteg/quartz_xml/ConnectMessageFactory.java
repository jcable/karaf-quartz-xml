package wsinteg.quartz_xml;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.quartz.JobDataMap;
import org.quartz.jobs.ee.jms.JmsMessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectMessageFactory implements JmsMessageFactory {
    protected Logger logger = LoggerFactory.getLogger(getClass());

	public Message createMessage(JobDataMap map, Session session) {
        String text = String.format("connect %s to %s", map.getString("source"), map.getString("destination"));
        try {
			return session.createTextMessage(text);
		} catch (JMSException e) {
        	logger.error("Caught: " + e.getLocalizedMessage());
			return null;
		}
	}
}
