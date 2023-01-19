package com.fastcampus.projectboard.repository;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

public class ThreadPoolExecutorTest {

	@Test
	void threadPoolExecutorTest() throws InterruptedException {

		int numTasks = 60;
		BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>();
		CountDownLatch countDownLatch = new CountDownLatch(numTasks);

		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 50, 10,
				TimeUnit.SECONDS, blockingQueue);

		for(int i = 0; i<120; i++) {
			threadPoolExecutor.submit(() -> {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {

				}
				countDownLatch.countDown();
			});
		}

		for(int i=0; i< 120; i++) {
			Thread.sleep(500);

			System.out.println("Active: " + threadPoolExecutor.getActiveCount());

			System.out.println("Queue: " + blockingQueue.size());
		}

		threadPoolExecutor.shutdown();
	}
}
