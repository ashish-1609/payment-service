package com.payments.tests;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class JavaConcurrentThreads {

    public static void main(String[] args) {
        try (ExecutorService service = Executors.newFixedThreadPool(10)) {
            service.submit(() -> handleRequest("A"));
            service.submit(() -> handleRequest("B"));
            service.submit(() -> handleRequest("C"));
            service.submit(() -> handleRequest("D"));
            service.submit(() -> handleRequest("E"));
            service.submit(() -> handleRequest("F"));
            service.submit(() -> handleRequest("G"));
            service.submit(() -> handleRequest("H"));
            service.submit(() -> handleRequest("I"));
            service.submit(() -> handleRequest("J"));
            service.submit(() -> handleRequest("K"));
        }

    }

    public static void handleRequest(String user) {
        try {
            Thread.sleep(10000);
            System.out.println(user + " requested for the thread: "+ Thread.currentThread().getName());
        } catch (InterruptedException e) {
            System.err.println("Error occurred while sleeping");
        }
    }
}
