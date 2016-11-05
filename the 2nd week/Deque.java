
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

	private Node first;
	private Node last;
	private int num;

	private class Node {
		Item item;
		Node front, next;

		public Node(Item item, Node front, Node next) {
			this.item = item;
			this.front = front;
			this.next = next;
		}
	}

	public Deque() { // construct an empty deque
		first = last = null;
		num = 0;
	}

	public boolean isEmpty() { // is the deque empty?
		return first == null; // first == last的判断是错误的。因为只有1个元素时也是这个表达式。
	}

	public int size() { // return the number of items on the deque
		return num;
	}

	public void addFirst(Item item) { // add the item to the front
		if (item == null)
			throw new NullPointerException();
		Node oldFirst = first;
		first = new Node(item, null, oldFirst); // 它的前边已经不指向任何[实体]了，只有一个句柄first指着它。设为null吧。
		if (size() == 0)
			last = first; // last原先是[谁也不指向]的
		else {
			first.next.front = first; // 更改下一个节点的front.
		}
		num++;
	}

	public void addLast(Item item) { // add the item to the end
		if (item == null)
			throw new NullPointerException();
		Node oldLast = last;
		/*last.next*/last = new Node(item, oldLast, null);
		if (size() == 0)
			first = last;
		else{
			last.front.next = last;
		}
		num++;
	}

	public Item removeFirst() { // remove and return the item from the front
		if (isEmpty())
			throw new NoSuchElementException(); // 没有元素的情况
		Item item = first.item; // 不要建立新的oldFirst变量。。要不first移动之后oldFirst还指向那个item。。。垃圾没法回收。。。
		if (first == last) {
			first = last = null; // 只有一个元素的情况
		} else {
			first = first.next; // 其他情况
			first.front.next = null; // 下边两句方便回收
			first.front = null;
		}
		num--;
		return item;
	}

	public Item removeLast() { // remove and return the item from the end
		if (isEmpty())
			throw new NoSuchElementException(); // 没有元素的情况
		Item item = last.item;
		if (first == last)
			first = last = null;
		else {
			last = last.front;
			last.next.front = null; // 方便回收。
			last.next = null;
		}
		num--;
		return item;
	}

	private class ListIterator implements Iterator<Item> {

		private Node currentFirst = first;
		private Node currentLast = last;

		@Override
		public boolean hasNext() {
			//个人觉得最好不要调用isEmpty().因为调用的是外边的isEmpty()，实现方法是first==null，可能会和内部currentFirst==null结果不一样。
			if (currentFirst != null) {
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
			if (currentFirst == null) {
				throw new NoSuchElementException();
			}
			Item item = currentFirst.item;
			currentFirst = currentFirst.next;
			return item;
		}

/*		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			while (this.hasNext()) {
				sb.append(this.next()).append(" ");
			}
			sb.append("\n");
			// this.remove();
			return sb.toString();
		}
*/
	}

	public Iterator<Item> iterator() { // return an iterator over items in order
										// from front to end
		return new ListIterator();
	}

/*	@Override
	public String toString() { // 测试工具。打印出deque的所有item，以及下一行是每个节点的front.item，最后一行是每个节点的next.item.
		StringBuilder sb = new StringBuilder();
		Node temp1 = first;
		while (temp1 != null) {
			sb.append(temp1.item).append(" ");
			temp1 = temp1.next;
		}
		sb.append("\n");
		Node temp2 = first;
		while (temp2 != null) {
			if (temp2.front != null)
				sb.append(temp2.front.item).append(" ");
			else
				sb.append("N").append(" ");
			temp2 = temp2.next;
		}
		sb.append("\n");
		Node temp3 = first;
		while (temp3 != null) {
			if (temp3.next != null)
				sb.append(temp3.next.item).append(" ");
			else
				sb.append("N").append(" ");
			temp3 = temp3.next;
		}
		sb.append("\n");
		return sb.toString();
	}
*/
	public static void main(String[] args) { // unit testing

		Deque<Integer> deque = new Deque<>();
		deque.addFirst(3);
		deque.addFirst(2);
		deque.addFirst(1);
		deque.addLast(7);
		deque.addLast(8);
		deque.addLast(9);
		deque.addFirst(3);
		deque.addFirst(2);
		deque.addFirst(1);
		deque.removeFirst();
		deque.removeLast();
		System.out.println(deque);
		System.out.println(deque.size());

		Iterator<Integer> it = deque.iterator();
		System.out.println(it);

	}

}