package com.sqnguyen.toyapp.sqsthreads;


import org.apache.log4j.Logger;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.sqnguyen.toyapp.sqsthreads.tasks.CreateQueueTask;

/**
 * A toy message draining application that spins off threads to speed up the process. 
 * Uses SQS to send, receive, and delete messages. (Probably could just use a MapReduce application,
 * but this is more for fun.
 * 
 * @author snnguyen
 *
 */
public class Main {

	static Logger log = Logger.getLogger(Main.class);
	public static final int NUM_THREADS = 4;
	
    public static void main(String[]args){

        try {
            AWSCredentials credentials = new PropertiesCredentials(
                    Main.class.getResourceAsStream("AwsCredentials.properties"));
            AmazonSQSClient client = new AmazonSQSClient(credentials);
            client.setEndpoint("http://sqs.us-west-2.amazonaws.com");
            
        	CreateQueueTask createTask = new CreateQueueTask(client);
        	createTask.run();
        	String url = createTask.getUrl();
        	ThreadPool pool = new ThreadPool(url,NUM_THREADS);
        	pool.run();
        	
        } catch(Exception ioe){
        	log.error(ioe);
        }
    }
    
}
