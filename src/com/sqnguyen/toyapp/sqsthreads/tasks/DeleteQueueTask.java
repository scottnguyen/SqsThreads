package com.sqnguyen.toyapp.sqsthreads.tasks;

import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;

public class DeleteQueueTask extends Task{
    private AmazonSQSClient client;
	private String url;
	
	public DeleteQueueTask(AmazonSQSClient client, String url) {
    	    this.client = client;
    	    this.url = url;
	}
	
	@Override
	public void run() {
		DeleteQueueRequest request = new DeleteQueueRequest()
            .withQueueUrl(url);
        client.deleteQueue(request);
    }

}
