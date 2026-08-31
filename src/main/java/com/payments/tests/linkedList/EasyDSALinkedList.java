package com.payments.tests.linkedList;

import com.payments.tests.Node;

public class EasyDSALinkedList {

    static Node head;

    static {
        Node node_1 = new Node(2);
        Node node_2 = new Node(1);
        Node node_3 = new Node(3);
        Node node_4 = new Node(5);
        Node node_5 = new Node(4);

        head = node_1;
        node_1.next = node_2;
        node_2.next = node_3;
        node_3.next = node_4;
        node_4.next = node_5;
        node_5.next = node_1;
    }

    public static void main(String[] args) {
        EasyDSALinkedList list = new EasyDSALinkedList();
        Node head1 = new Node(1);
        head1.next = new Node(3);
        head1.next.next = new Node(5);
        head1.next.next.next = new Node(7);
        head1.next.next.next.next = new Node(9);

        Node head2 = new Node(2);
        head2.next = new Node(4);
        head2.next.next = new Node(6);
        head2.next.next.next = new Node(8);
        head2.next.next.next.next = new Node(10);

        list.print(list.mergeTwoLinkedList(head1, head2));
    }

    void reverseLinkedList() {
        Node temp = head;
        Node prev = null;
        while (temp != null) {
            Node next = temp.next;
            temp.next = prev;
            prev = temp;
            temp = next;
        }
        head = prev;
    }

    void findMiddleOfALinkedList() {
        Node slow = head;
        Node fast = head;
        while (fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        System.out.println("Mid: " + slow.data);
    }

    void checkCycle() {
        if (head == null) {
            System.out.println("Empty Linked List");
            return;
        }
        Node temp = head.next;
        while (temp != null && temp != head) {
            temp = temp.next;
        }
        if (temp == head) {
            System.out.println("Linked List has a cycle");
        } else {
            System.out.println("Linked List does not have a cycle");
        }
    }

    Node mergeTwoLinkedList(Node l1, Node l2) {
        Node temp = new Node(0);
        if (l1 == null && l2 == null) {
            return temp.next;
        }
        Node curr = temp;
        return curr;
    }

    void print(Node head) {
        Node temp = head;
        while (temp != null) {
            System.out.print("| " + temp.data + " |");
            if (temp.next != null) {
                System.out.print(" -> ");
            }
            temp = temp.next;
        }
    }
}