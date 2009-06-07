package edu.nlt.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import edu.nlt.util.processor.LineProcessor;

/**
 * Delegates LineProcessor to perform actions in parallel
 * 
 * @author shu
 * 
 */
public class LPMultiThreader implements LineProcessor {
	private int linesProcessed = 0;
	private List<Thread> threads;
	private int numOfThreads = 4;
	private LineProcessor myProcessor;

	public LPMultiThreader(int maxNumOfThreads, LineProcessor lineProcessor) {
		super();

		threads = new ArrayList<Thread>(maxNumOfThreads);
		numOfThreads = maxNumOfThreads;
		myProcessor = lineProcessor;

		for (int i = 0; i < numOfThreads; i++) {
			Thread newThread = new Thread(new Worker());
			threads.add(newThread);
			newThread.start();

		}

	}

	private class Worker implements Runnable {

		@Override
		public void run() {

			String next = null;

			while (!isClosed || pending.size() > 0) {
				try {
					synchronized (pending) {
						next = pending.poll(500, TimeUnit.MILLISECONDS);
					}

					if (next != null) {
						linesProcessed++;
						myProcessor.processLine(next);
					}

				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}

			synchronized (pending) {
				pending.notifyAll();
			}
		}
	}

	private BlockingQueue<String> pending = new LinkedBlockingQueue<String>(400);

	private boolean isClosed = false;

	public void close() {
		isClosed = true;

		while (pending.size() > 0) {
			try {
				synchronized (pending) {
					pending.wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// System.out.println(linesProcessed);
		// System.out.println(linesProcessed2);
	}

	static int linesProcessed2 = 0;

	@Override
	public void processLine(String value) {
		if (value != null) {
			linesProcessed2++;
		}
		try {

			if (!pending.offer(value, 1, TimeUnit.DAYS)) {
				throw new RuntimeException();
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
