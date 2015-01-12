package kellonge.flightcrawler.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import kellonge.flightcrawler.config.Configuration;

public class ErrorUrlWriter {

	private static PrintWriter printWriter = null;
	private static String filePath = Configuration.ROOT_PATH
			+ "/data/error.txt";

	public static void Print(String url) {
		getPrintWriter().println(url);

	}

	private static PrintWriter getPrintWriter() {
		if (printWriter == null) {
			try {
				printWriter = new PrintWriter(new FileWriter(filePath, true));
				initFlushThread();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return printWriter;
	}

	private static void initFlushThread() {
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				flush();
			}
		}, 10, 10, TimeUnit.SECONDS);
	}

	private static void flush() {
		printWriter.flush();
	}
}
