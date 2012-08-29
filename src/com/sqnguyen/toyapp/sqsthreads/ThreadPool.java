package com.sqnguyen.toyapp.sqsthreads;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.sqnguyen.toyapp.sqsthreads.tasks.DeleteQueueTask;
import com.sqnguyen.toyapp.sqsthreads.tasks.RetrieveMessageTask;
import com.sqnguyen.toyapp.sqsthreads.tasks.SendMessageTask;
import com.sqnguyen.toyapp.sqsthreads.tasks.Task;

public class ThreadPool {

	
	class AtsThread implements Callable<Integer>{
		private Task task;
		public AtsThread(Task task){
			this.task = task;
		}
		@Override
		public Integer call() throws Exception {	
			task.run();
			return 0;
		}
	}
	
	
	private int numThreads;
	private AmazonSQSClient client;
	private String url;
	private static Logger log = Logger.getLogger(ThreadPool.class);

	public ThreadPool (String url, int numThreads) throws IOException{
		this.url = url;
        AWSCredentials credentials = new PropertiesCredentials(
        		Main.class.getResourceAsStream("AwsCredentials.properties"));
        client = new AmazonSQSClient(credentials);
        client.setEndpoint("http://sqs.us-west-2.amazonaws.com");
        this.numThreads = numThreads;
        
    }
	
	public void run() throws Exception{
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);
		int i = 0;
		long start,end;
		start = System.currentTimeMillis();
		List<Future<Integer>> results = new ArrayList<Future<Integer>>();
		while(i++ < numThreads ){
			results.add(executor.submit(new AtsThread(new SendMessageTask(client, url))));
		}
		
		for(Future<Integer> result : results){
			result.get();
		}
		results.clear();
		i = 0;
		executor = Executors.newFixedThreadPool(numThreads);
		while(i++ < numThreads ){
			results.add(executor.submit((new AtsThread(new RetrieveMessageTask(client, url)))));
		}
		for(Future<Integer> result : results){
			result.get();
		}
		end = System.currentTimeMillis();
		log.info("Processed in " + (end-start)/1000 + " seconds.");
		executor.submit(new AtsThread(new DeleteQueueTask(client, url))).get();
	}

	
}
