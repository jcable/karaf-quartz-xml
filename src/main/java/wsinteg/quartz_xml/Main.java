package wsinteg.quartz_xml;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		XMLScheduler s = new XMLScheduler();
		Thread.sleep(3600000);
		s.close();
	}
}
