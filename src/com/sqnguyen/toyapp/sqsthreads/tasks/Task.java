package com.sqnguyen.toyapp.sqsthreads.tasks;

import com.sqnguyen.toyapp.sqsthreads.Main;

public abstract class Task {
	protected static final int NUM_OF_MESSAGES = 20000;
	protected static final int NUM_OF_MESSAGES_PER_THREAD = NUM_OF_MESSAGES/Main.NUM_THREADS;
	protected static final String QUEUE_NAME = "test234";
    protected static final int TIMEOUT = NUM_OF_MESSAGES;
    
	public abstract void run();
}
