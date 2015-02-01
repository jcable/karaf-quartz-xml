package wsinteg.quartz_xml;

import java.util.Properties;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.jobs.ee.jms.JmsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XMLScheduler {

	private Scheduler scheduler;
	protected Logger logger = LoggerFactory.getLogger(getClass());
		
	public XMLScheduler() {	
		Properties props = new Properties();
		props.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
		props.put("org.quartz.scheduler.skipUpdateCheck", "true");
		props.put("org.quartz.threadPool.threadCount", "5");
		//props.put("org.quartz.scheduler.classLoadHelper.class", "org.quartz.simpl.CascadingClassLoadHelper");
		props.put("org.quartz.plugin.jobInitializer.class", "org.quartz.plugins.xml.XMLSchedulingDataProcessorPlugin");
		props.put("org.quartz.plugin.jobInitializer.fileNames", "jobs.xml");
		props.put("org.quartz.plugin.jobInitializer.scanInterval",  "0");
		props.put("org.quartz.plugin.jobInitializer.wrapInUserTransaction", "false");
		props.put("org.quartz.plugin.jobInitializer.failOnFileNotFound", "false");

        // Grab a Scheduler instance from the Factory
		try {
        	StdSchedulerFactory factory = new StdSchedulerFactory(props);
			scheduler = factory.getScheduler();
         
		} catch (SchedulerException e) {
            logger.error("Caught: " + e.getMessage());
		}
        try {
        	// create a durable job so the xml file only needs triggers
	        JobDetail job = JobBuilder.newJob(ActiveMQJob.class).
	        		withIdentity("connect", "DEFAULT").
            		usingJobData("jms.queue", "path.connect").
            		usingJobData(JmsHelper.JMS_MSG_FACTORY_CLASS_NAME, ConnectMessageFactory.class.getName()).
            		storeDurably().
	        		build();
			scheduler.addJob(job, false);
            // and start it off
			logger.info("starting scheduler");
            scheduler.start();
		} catch (SchedulerException e) {
            logger.error("Caught: " + e.getMessage());
		}
    }

    public void close() {		
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            logger.error("Caught: " + e.getMessage());
        }
    }
}