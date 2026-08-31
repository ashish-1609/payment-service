package com.payments.tests.linkedList;

import java.util.Scanner;

public class DLLOperations {
	Node head;

	final Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		DLLOperations ops = new DLLOperations();
		ops.addNode(1);
		ops.addNode(2);
		ops.addNode(3);
		ops.addNode(4);
		ops.print();
//		int i = 0;
//		while (i <= 2) {
//			ops.print();
//			ops.addAtSpecificPlace();
//			ops.print();
//			i++;
//		}
//
//		System.out.println("_________________________________________");
//		ops.traverseForward();
//		System.out.println("\n_________________________________________");
//		ops.traverseBackward();
	}

	public void addNode(Object data) {
		Node node = new Node(data);
		if (head == null) {
			head = node;
			System.out.printf("Node [%s] added successfully at head %n", data);
			return;
		}
		Node temp = head;
		while (temp.next != null) {
			temp = temp.next;
		}
		node.prev = temp;
		temp.next = node;
		System.out.printf("Node [%s] added successfully at tail %n", data);
	}

	public void removeHead() {
		if (head == null) {
			System.out.println("Doubly linked list is empty");
			return;
		}
		head = head.next;
	}

	public void updateNode() {
		System.out.print("Enter index to update: ");
		int index = sc.nextInt();
		System.out.print("Enter data to update: ");
		Object data = sc.next();
		Node node = new Node(data);
		if (head == null) {
			head = node;
			return;
		}

	}

	public void addAtSpecificPlace() {
		System.out.println("Enter the index: ");
		int index = sc.nextInt();
		System.out.println("Enter the data: ");
		Object data = sc.next();
		Node node = new Node(data);
		if (head == null) {
			head = node;
			return;
		}
		if (index == 0) {
			node.next = head;
			head.prev = node;
			head = node;
			return;
		}
		Node temp = head;
		int size = size();
		int i = 0;
		if (index >= size) {
			while (temp.next != null) {
				temp = temp.next;
			}
			temp.next = node;
			node.prev = temp;
			return;
		}
		while (i < index - 1 && temp.next != null) {
			temp = temp.next;
			i++;
		}
		node.prev = temp;
		node.next = temp.next;
		if (temp.next != null) {
			temp.next.prev = node;
		}
		temp.next = node;
	}

	public void deleteHead() {
		if (head == null) {
			System.out.println("Doubly linked list is empty");
			return;
		}
		System.out.printf("Deleting [%d] node\n", (int) head.data);
		head = head.next;
		if (head != null) {
			head.prev = null;
		}
		print();
	}

	public void deleteTail() {
		if (head == null) {
			return;
		}
		Node temp = head;
		while (temp.next != null) {
			temp =temp.next;
		}
		temp.prev.next = null;
	}

	public void traverseForward() {
		Node temp = head;
		if (temp == null) {
			return;
		}
		while(temp!= null) {
			System.out.print(temp.data+" ");
			temp = temp.next;
		}
	}

	public void traverseBackward() {
		Node temp = head;
		if (temp == null) {
			return;
		}
		while (temp.next != null) {
			temp = temp.next;
		}
		while (temp != null) {
			System.out.print(temp.data + " ");
			temp = temp.prev;
		}
	}

	public void reverseDoublyLinkedList() {
		if (head == null) {
			System.out.println("Doubly linked list is empty");
			return;
		}
	}

	public int size() {
		Node temp = head;
		int count = 0;
		while (temp != null) {
			count++;
			temp = temp.next;
		}
		return count;
	}

	public void print() {
		Node temp = head;
		if (head == null) {
			System.out.println("Doubly linked list is empty");
			return;
		}
		while (temp != null) {
			System.out.println(temp.prev + " | " + temp.data + " | " + temp.next);
			temp = temp.next;
		}
	}
}

class Node {
	Node prev;
	Object data;
	Node next;

	Node(Object data) {
		prev = null;
		this.data = data;
		next = null;
	}
}
