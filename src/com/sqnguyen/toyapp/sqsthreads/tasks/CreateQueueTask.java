package com.sqnguyen.toyapp.sqsthreads.tasks;

import org.apache.log4j.Logger;

import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;

public class CreateQueueTask extends Task{
	private AmazonSQSClient client;
	private String url;
	private static Logger log = Logger.getLogger(CreateQueueTask.class);
	
	public CreateQueueTask(AmazonSQSClient client){
		this.client = client;
	}
	

	@Override
	public void run() {
        CreateQueueRequest createQueueRequest = new CreateQueueRequest()
        	.withQueueName(QUEUE_NAME);
		CreateQueueResult result = client.createQueue(createQueueRequest);
		url = result.getQueueUrl();
		log.info("URL : " + url);
	}
	
	public String getUrl(){
		return url;
	}
	

}
