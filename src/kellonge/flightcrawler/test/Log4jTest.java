package kellonge.flightcrawler.test;

import java.io.Console;
import java.util.BitSet;

import org.apache.log4j.Logger;

public class Log4jTest {

	private static Logger logger = Logger.getLogger(Log4jTest.class);

	public static void main(String[] args) {
		logger.info("this is a info");
		logger.debug("this is a debug");
		logger.error("this is a error");
	}
}
