package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecuteTask<T extends Runnable> {

    public ExecuteTask(T task) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    public ExecuteTask(T task1, T task2, int threads) {
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        executor.execute(task1);
        executor.execute(task2);
    }
}
