package com.payments.tests.linkedList;

import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.Queue;

public class RoundRobin {

	public static void main(String[] args) {
		int time = 2;
		int[] processes = {10, 7, 3, 1, 6, 7};
		roundRobinProcess(processes, time);
	}

	private static void roundRobinProcess(int[] processes, int time) {
		System.out.printf("Starting Processing for %d processes where each process will take %d ms time%n",
				processes.length, time);

		Queue<Integer> queue = new ArrayDeque<>();
		Queue<Integer> ids = new ArrayDeque<>();

		for (int i = 0; i < processes.length; i++) {
			queue.add(processes[i]);
			ids.add(i);
		}

		while (!queue.isEmpty()) {
			int remaining = queue.remove();
			int id = ids.remove();

			if (remaining > time) {
				remaining -= time;
				queue.add(remaining);
				ids.add(id);
			} else {
				System.out.println("Process P" + id + " completed");
			}
		}
	}
}
