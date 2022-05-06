package com.qingyan.traffictool.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

/**
 * ThreadPoolConfig
 *
 * @author xuzhou
 * @version v1.0.0
 * @date 2022/5/5 17:33
 */
@Component
public class ThreadPoolConfig {

    public final ExecutorService threadPool = new ThreadPoolExecutor(4, 10,
            30L, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(1000),
            namedThreadFactory());

    public void stop() {
        threadPool.shutdown();
    }

    private static ThreadFactory namedThreadFactory() {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        return (Runnable r) -> {
            Thread thread = new Thread(r);
            thread.setName("thread-name-" + atomicInteger.getAndIncrement());
            return thread;
        };
    }
}
