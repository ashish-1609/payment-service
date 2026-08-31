package com.payments.tests.linkedList;

import com.payments.tests.Node;

import java.util.Scanner;

public class LLOperations {

	Node head;

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		LLOperations ll = new LLOperations();
		while (true) {
			System.out.println("Enter the operation you want: " + "\n1). Add " + "\n2). Print" + "\n3). Remove by value"
					+ "\n4). Remove by index " + "\n5). Add at specific index" + "\n6). Size" + "\n8). Detect Cycle" + "\n0). Exit");
			String userInput = scanner.nextLine();
			System.out.println("User entered: " + userInput);
			String data = "";
			int index;
			switch (userInput) {
				case "1" :
					System.out.print("Enter the value to add: ");
					data = scanner.nextLine();
					ll.add(data);
					break;
				case "2" :
					ll.print();
					break;
				case "3" :
					System.out.print("Enter the value to remove: ");
					String dataToRemove = scanner.nextLine();
					ll.removeByValue(dataToRemove);
					break;
				case "4" :
					System.out.print("Enter the index to remove: ");
                    index = scanner.nextInt();
                    System.out.println();
					ll.removeByIndex(index);
					break;
				case "5":
					System.out.print("Enter the index to add: ");
					index = scanner.nextInt();
					System.out.print("Enter the value to add: ");
					data = scanner.next();
					ll.addToIndex(index, data);
					break;
				case "6" :
					System.out.println("Size of the linked list is: " + ll.size());
					break;
				case "7":
					System.out.print("Enter the value to add: ");
					data = scanner.next();
					ll.addToHead(data);
					break;
				case "8":
					System.out.print("Detecting Cycle in a linked list");
					ll.detectCycle();
					break;
				case "0" :
					return;
			}
		}
	}

	private void addToHead(String data) {
		Node node = new Node(data);
        node.next = head;
		head = node;
	}

    private void addToIndex(int index, String data) {
        Node temp = head;
		Node newNode = new Node(data);

		if (index == 0) {
			newNode.next = head;
			head = newNode;
			return;
		}
		int size = size();
		if (index > size) {
			System.out.println("Insertion not possible, out of index");
			return;
		}
		Node prev = temp;
		for (int i = 0; i < index; i++) {
			prev = temp;
			temp = temp.next;
		}
		prev.next = newNode;
		newNode.next = temp;
    }

	void add(Object data) {
		Node node = new Node(data);
		if (head == null) {
			head = node;
			return;
		}
		Node temp = head;
		while (temp.next != null) {
			temp = temp.next;
		}
		temp.next = node;
	}

	void removeByValue(Object data) {
		Node temp = head;
		while (temp.next != null) {
			if (data.equals(temp.next.data)) {
				temp.next = temp.next.next;
				break;
			}
			temp = temp.next;
		}
	}

	void removeByIndex(int index) {
		int size = size();
		if (index >= size) {
			return;
		}
		if (index == 0) {
			head = head.next;
			return;
		}
		Node temp = head;
		int i = 0;
		while (i < index - 1 && temp != null) {
			temp = temp.next;
			i++;
		}
		if (temp == null || temp.next == null) {
			return;
		}
		temp.next = temp.next.next;
	}

	int size() {
		Node temp = head;
		int size = 0;
		while (temp != null) {
			size++;
			temp = temp.next;
		}
		return size;
	}

	void print() {
		Node temp = head;
		while (temp != null) {
			String formattedOutput = "| " + temp.data + " | " + temp.next + " |";
			System.out.println("-".repeat(formattedOutput.length()));
			System.out.print(formattedOutput);
			System.out.println(" -> ");
			System.out.println("-".repeat(formattedOutput.length()));
			temp = temp.next;
		}
	}

	void detectCycle() {
		head = new Node(1);
		head.next = new Node(2);
		head.next.next = new Node(3);
		head.next.next.next = new Node(4);
		head.next.next.next.next = head.next;
		if (head == null) {
			System.out.println("No cycle detected");
		}
		Node temp = head;
		Node slow = head;
		Node fast = head;
		while (slow != null && fast != null && fast.next != null) {
			slow = slow.next;
			fast = fast.next.next;
			if (slow == fast) {
				System.out.println("Cycle identified: " + true);
				break;
			}
		}
		fast = temp;
		while (slow != null && fast.next != null) {
			slow = slow.next;
			fast = fast.next;
			System.out.println("slow: " + slow.data + ", fast: " + fast.data);
			if (slow == fast) {
				System.out.println("Cycle identified at: " + slow.data + ", " + fast.data);
				break;
			}
		}
	}
}
