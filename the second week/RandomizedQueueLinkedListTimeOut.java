package com.algorithm.week2;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueueLinkedListTimeOut<Item> implements Iterable<Item> {

	// 链表超时。按照网上大神一样，变成用数组实现了.技巧：每次dequeue时，把n-1换到最前边去。
	// 搞一个循环+双向队列，用一个通用指针指向中间。random是几就向random%2方向挪random格。random%2==0向左，==1向右。就这么定了2333

	private Node first; // 去掉了last。
	private int num;

	private class Node {
		private Item item;
		private Node front, next;
		private boolean visited;

		public Node(Item item, Node front, Node next) {
			this.item = item;
			this.front = front;
			this.next = next;
			this.visited = false;
		}
	}

	public RandomizedQueueLinkedListTimeOut() { // construct an empty randomized queue
		first = null;
		num = 0;
	}

	public boolean isEmpty() { // is the queue empty?
		return first == null;
	}

	public int size() { // return the number of items on the queue
		return num;
	}

	public void enqueue(Item item) { // add the item //设定还是规矩地从first插入吧。
		if (item == null)
			throw new NullPointerException();
		if (isEmpty()) {
			first = new Node(item, null, null);
			first.front = first; // 一个前后结点全指向自己的Node......
			first.next = first;
		} else if (size() == 1) {
			first = new Node(item, first.front, first); // 在first前边初始化node.
			first.front.next = first; // first的front也把next赋值成first
			first.front.front = first; // mdzz.....要绕蒙了......必须多出来一句。因为size()==1时，仅有的那个结点front和next全指向自己。。所以要改一下。
		} else {
			first = new Node(item, first.front, first);
			first.front.next = first;
			first.next.front = first; // 注意和上一个else if的这一句有本质区别。
		}
		num++;
	}

	private Node random() {
		int random = StdRandom.uniform(num) + 1; // 想规避线性检查用num/2。。然而如果只有一个元素，好像只能删掉那个元素啊。
													// 如果设num/2会发现问题。。删的越多越不随机。因为越删，到最后就random随着num--就会趋近于1.
		// System.out.println("最后这句话要去掉★random = "+random+" ！！");
		Node temp = first;
		if (random % 2 == 0) {
			while (random-- > 0) {
				temp = temp.front;
			}
		} else {
			while (random-- > 0) {
				temp = temp.next;
			}
		}
		return temp;
	}

	public Item dequeue() { // remove and return a random item

		// 这里也有陷阱。。。如果把first指向的东西删了怎么办。。。链表变成悬挂的了。。。太坑了！
		if (isEmpty())
			throw new NoSuchElementException();
		if (size() == 1) {
			Item item = first.item;
			first = null;
			num--;
			return item;
		}
		Node temp = random();
		if (size() == 2) {
			Item item = temp.item;
			temp.front.next = temp.front.front = temp.front; // 自抱模式 只有一个元素的时候
																// 它的前后都返回自己。
			if (temp == first) {
				// 如果误删了自己 把first删了
				first = first.next; // first先往右串一位
			}
			temp.front = null; // 回收
			temp.next = null; // 回收
			temp = null; // 回收
			num--;
			return item;
		} else {
			Item item = temp.item;
			temp.front.next = temp.next;
			temp.next.front = temp.front;
			if (temp == first) {
				// 如果误删了自己 把first删了
				first = first.next; // first先往右串一位
			}
			temp.front = null; // 同上，三步回收。
			temp.next = null;
			temp = null;
			num--;
			return item;
		}

	}

	public Item sample() { // return (but do not remove) a random item
		if (isEmpty())
			throw new NoSuchElementException();
		return random().item;
	}

	private class ListIterator implements Iterator<Item> {

		// 但是并不安全。如果外边生成了一个ListIterator，再在外部调用一个enqueue，这个ListIterator就作废了。
		private Node currentFirst = first;
		private int currentNum = num;

		@Override
		public boolean hasNext() {
			if (currentNum > 0) { // currentFirst!=null比num>0还低一个层次，索性用num了。因为迭代器里边设置每有一个node标成false，num--以备next()使用。
				return true;
			}
			return false;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Item next() {
			if (currentNum <= 0) {
				throw new NoSuchElementException();
			} else /*( /*num / currentNum <= 10  currentNum > 0)*/ { // 设置currentNum的大小和num的比值低于1/4就禁用随机，改成直接遍历。要不如果标成访问过的远大于没访问的，随机数可能一辈子也找不着了。。
				do {	//事实证明单凭借随机数是可以完全遍历的。。。。醉了。。。
					int random = StdRandom.uniform(currentNum) + 1;
					// System.out.print(random+"★");
					if (random % 2 == 0) { // 向左找
						while (random-- > 0) {
							currentFirst = currentFirst.front;
						}
					} else {
						while (random-- > 0) { // 向右找。
							currentFirst = currentFirst.next;
						}
					}
				} while (currentFirst.visited == true);
			} 
//			else {
//				while (currentFirst.visited == true) { // 不再随机，改成轮询遍历了。
//					currentFirst = currentFirst.next;
//				}
//			}
			currentFirst.visited = true; // 标成被访问过。
			currentNum--;
			return currentFirst.item; // else if中是一样的随机策略。
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("\n");
			while (this.hasNext()) {
				sb.append(this.next()).append("\n");
			}
			sb.append("\n");
			// this.remove();
//			for(int i = 0; i < num; i ++){
//				if(currentFirst.visited == true){ 
//					System.out.println(i);
//				}
//			}
//			return null;
			return sb.toString();
		}

	}

	public Iterator<Item> iterator() { // return an independent iterator over
										// items in random order
		return new ListIterator();
	}

	@Override
	public String toString() { // 测试工具。打印出deque的所有item，以及下一行是每个节点的front.item，最后一行是每个节点的next.item.
		StringBuilder sb = new StringBuilder();
		int ret = num;
		Node temp1 = first;
		while (ret-- > 0) {
			sb.append(temp1.item).append(" ");
			temp1 = temp1.next;
		}
		sb.append("\n");
		ret = num;
		Node temp2 = first;
		while (ret-- > 0) {
			if (temp2.front != null)
				sb.append(temp2.front.item).append(" ");
			else
				sb.append("N").append(" ");
			temp2 = temp2.next;
		}
		sb.append("\n");
		ret = num;
		Node temp3 = first;
		while (ret-- > 0) {
			if (temp3.next != null)
				sb.append(temp3.next.item).append(" ");
			else
				sb.append("N").append(" ");
			temp3 = temp3.next;
		}
		sb.append("\n");
		return sb.toString();
	}

	public static void main(String[] args) { // unit testing

		RandomizedQueueLinkedListTimeOut<Integer> rq = new RandomizedQueueLinkedListTimeOut<>();
		// rq.enqueue(3);
		// rq.enqueue(1);
		// rq.enqueue(2);
		// rq.enqueue(7);
		// rq.enqueue(8);
		// rq.enqueue(9);
		for (int i = 0; i < 10; i++) {
			rq.enqueue(i);
		}
		// System.out.println(rq.sample());
		// System.out.println(rq.toString());
		// System.out.println(rq.dequeue());
		// System.out.println(rq.toString());
		// System.out.println(rq.dequeue());
		// System.out.println(rq.toString());
		
		System.out.println(rq.iterator().toString());
		
	}
}