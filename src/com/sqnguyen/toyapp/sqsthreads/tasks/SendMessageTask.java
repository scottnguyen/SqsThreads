package com.sqnguyen.toyapp.sqsthreads.tasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class SendMessageTask extends Task{
	
    private AmazonSQSClient client;
	private String url;
	private static Logger log = Logger.getLogger(SendMessageTask.class);
	
    public SendMessageTask(AmazonSQSClient client, String url) {
    	this.client = client;
    	this.url = url;
	}

	public void sendMessage(String message) throws InterruptedException{
        
        SendMessageRequest request = new SendMessageRequest()
                .withMessageBody(message)
                .withQueueUrl(url);
        client.sendMessage(request);
        
    }

	private void sendMessages(List<SendMessageBatchRequestEntry> batch) {
		SendMessageBatchRequest request = new SendMessageBatchRequest()
												.withEntries(batch)
												.withQueueUrl(url);
		client.sendMessageBatch(request);
	}
	
	@Override
	public void run() {
		long start, end;
        System.out.println();
        List<SendMessageBatchRequestEntry> batch = 
        		new ArrayList<SendMessageBatchRequestEntry>();
        start = System.currentTimeMillis();
        
        for (int i = 0 ; i < NUM_OF_MESSAGES_PER_THREAD/10 ; i++){
        	for (int k = 0; k < 10; k++)
        		batch.add(new SendMessageBatchRequestEntry()
        						.withId(Integer.toString(i*10+k))
        						.withMessageBody("hello worlds"));
        	this.sendMessages(batch);
        	batch.clear();
        }
        end = System.currentTimeMillis();
        log.info("Enqueue" + 
        		(end - start)/1000 + "seconds/"+ NUM_OF_MESSAGES_PER_THREAD + "messages");
        
	}

}
