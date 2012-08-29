package com.sqnguyen.toyapp.sqsthreads.tasks;

import java.util.List;

import org.apache.log4j.Logger;

import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;

public class RetrieveMessageTask extends Task{

	private String url;
	private AmazonSQSClient client;
	private static Logger log = Logger.getLogger(RetrieveMessageTask.class);

	public RetrieveMessageTask(AmazonSQSClient client, String url){
		this.client = client;
		this.url = url;
	}

    public void listMessages(String queueName){
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest()
						        	.withQueueUrl(url)
						        	.withMaxNumberOfMessages(10)
						        	.withVisibilityTimeout(TIMEOUT);
        ReceiveMessageResult result;
        while (true){
        	    result = client.receiveMessage(receiveMessageRequest);
            List<Message> messages = result.getMessages();
            if (messages.size() < 1)  break;
        }
    }
	@Override
	public void run() {
        long start,end;
        start = System.currentTimeMillis();
        this.listMessages(QUEUE_NAME);
        end = System.currentTimeMillis();
        log.info("Dequeue: " + (end-start)/1000 + " seconds");
	}
	
	public int getCount(){
		return NUM_OF_MESSAGES_PER_THREAD;
	}

}
