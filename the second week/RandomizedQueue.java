
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

	private Item[] items ;
	private int num;
	
	public RandomizedQueue() { // construct an empty randomized queue
		items = (Item[])new Object[16];
		num = 0;
	}

	public boolean isEmpty() { // is the queue empty?
		return num == 0;
	}

	public int size() { // return the number of items on the queue
		return num;
	}

	private void revise(int capacity) {
		Item[] newItems = (Item[])new Object[capacity];
		for(int i = 0; i < num; i ++){
			newItems[i] = items[i];
		}
		items = newItems;
	}
	
	public void enqueue(Item item) { // add the item
		if(item == null)	throw new NullPointerException();
		if(num == items.length)	revise(items.length * 2);
		items[num++] = item;
	}

	public Item dequeue() { // remove and return a random item
		if(isEmpty())	throw new NoSuchElementException();
		if(num <= items.length / 3)	revise(items.length / 2);
		int random = StdRandom.uniform(num);
		Item item = items[random];
		items[random] = items[num - 1];
		items[num - 1] = null;
		num --;
		return item;
	}

	public Item sample() { // return (but do not remove) a random item
		if(isEmpty())	throw new NoSuchElementException();
		int random = StdRandom.uniform(num);
		return items[random];
	}

	private class ListIterator implements Iterator<Item>{

		private boolean[] marked;
		private int currentNum;
		
		public ListIterator() {
			marked = new boolean[items.length];
			currentNum = num;
		}
		
		@Override
		public boolean hasNext() {
			return currentNum != 0;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public Item next() {
			if(currentNum == 0)	throw new NoSuchElementException();
			int random;
			do {
				random = StdRandom.uniform(num);
			}while(marked[random] == true);
			marked[random] = true;
			Item item = items[random];
			currentNum --;
			return item;
		}
		
	
		
	}
	
	public Iterator<Item> iterator() { // return an independent iterator over
										// items in random order
		return new ListIterator();
	}
	


	public static void main(String[] args) { // unit testing
		
		RandomizedQueue<Integer> rq = new RandomizedQueue<>();
		rq.enqueue(3);
		rq.enqueue(4);
		rq.enqueue(5);
		rq.enqueue(7);
		rq.enqueue(9);
		rq.enqueue(1);
		rq.enqueue(0);
		rq.dequeue();
		System.out.println(rq.iterator());
		System.out.println(rq.toString());
		
	}
}
