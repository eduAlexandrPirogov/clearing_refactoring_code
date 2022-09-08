import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.Callable;

class Node<T> {

	public Node<T> next, prev;
	protected String nodeStatus;

	public T value;

	public Node() {
		value = null;
		next = null;
		prev = null;
	}

	public Node(T _value) {
		value = _value;
		next = null;
		prev = null;
		nodeStatus = NODE_STATUS_EMPTY;
	}

	public String getStatus() {
		return nodeStatus;
	}
}

class ValueableNode<T> extends Node<T> {

	public ValueableNode(T _value) {
		super(_value);
		this.next = null;
		this.prev = null;
		// TODO Auto-generated constructor stub
	}

}

class EmptyNode<T> extends Node<T> {

	public EmptyNode() {
		super(null);
		this.next = null;
		this.prev = null;
	}



	public EmptyNode(T _value) {
		super(_value);
		// TODO Auto-generated constructor stub
		this.next = null;
		this.prev = null;
	}

}

abstract class NodesOrder<T extends Comparable<T>> implements Comparator<T> {

	public Node<T> head, tail;
	public int count;
	public T max, min;

	@Override
	public int compare(T v1, T v2) {
		// TODO Auto-generated method stub
		return (v1.compareTo(v2));
	}

	public abstract void add(T value);

	public abstract void addInHead(ValueableNode<T> _item);

	public abstract void addInTail(ValueableNode<T> _item);

	public abstract void delete(T val);

	public Node<T> find(T val) {
		if (this.head instanceof EmptyNode || compare(val, max) > 0 || compare(val, min) < 0)
			return new EmptyNode<T>();
		Node<T> current = this.head;
		while (!current.value.equals(val))
			current = current.next;
		return current;
	}

	public void insertAfter(ValueableNode<T> _nodeAfter, ValueableNode<T> _nodeToInsert) {
		Node<T> current = this.head;
		while (!(current instanceof ValueableNode)) {
			if (_nodeAfter.value == this.tail.value) {
				this.tail.next = _nodeToInsert;
				_nodeToInsert.prev = this.tail;
				this.tail = _nodeToInsert;
				_nodeToInsert.next = null;
				return;
			}
			if (current.equals(_nodeAfter)) {
				_nodeToInsert.next = current.next;
				current.next.prev = _nodeToInsert;
				current.next = _nodeToInsert;
				_nodeToInsert.prev = current;
				return;
			}
			current = current.next;
		}
	}

	public void clear(boolean asc) {
		Node<T> current = this.head;
		while (current instanceof ValueableNode) {
			this.head = current.next;
			current = current.next;
		}
		this.tail = new EmptyNode<T>();
		count = 0;
	}

	public int count() {
		return count;
	}

	public abstract ArrayList<Node<T>> getAll();

}

class DescNodesOrder<T extends Comparable<T>> extends NodesOrder<T> {

	DescNodesOrder(int count) {
		count = 0;
		head = new EmptyNode<T>();
		tail = new EmptyNode<T>();
	}

	public void add(T value) {
		ValueableNode<T> nodeToAdd = new ValueableNode<T>(value);
		count++;
		// Ternary operator would be usefull here
		if (this.head instanceof EmptyNode) {
			addFirstHead(nodeToAdd);
			return;
		}
		addAfterHead(nodeToAdd);

	}

	private void addFirstHead(ValueableNode<T> nodeToAdd) {
		addInTail(nodeToAdd);
		max = this.head.value;
		min = this.head.value;
	}

	private void addAfterHead(ValueableNode<T> nodeToAdd) {
		Node<T> current = this.head;
		
		//ternary operator would be great here
		if((compare(nodeToAdd.value, min) < 0 || compare(nodeToAdd.value, max) > 0))
		{
			if (compare(nodeToAdd.value, this.head.value) > 0)
				addInHead(nodeToAdd);
			else
				addInTail(nodeToAdd);
		} else {
			current = recDescSearch(nodeToAdd.value, current, nodeToAdd);
			insertAfter((ValueableNode) current, nodeToAdd);

		}

		
		min = this.head.value;
		max = this.tail.value;
	}
	

	private Node<T> recDescSearch(T value, Node<T> current, Node<T> nodeToAdd) {
		if (current.next instanceof EmptyNode
				|| (compare(nodeToAdd.value, current.value) > 0 && compare(nodeToAdd.value, current.next.value) < 0)
				|| (compare(current.value, nodeToAdd.value) == 0))
			return current;
		current = current.next;
		return recDescSearch(value, current, nodeToAdd);

	}

	@Override
	public void addInHead(ValueableNode<T> _item) {
		this.head.prev = _item;
		_item.next = head;
		this.head = _item;
		max = this.head.value;
	}

	public void addInTail(ValueableNode<T> _item) {
		if (head instanceof EmptyNode) {
			this.head = _item;
			this.head.next = null;
			this.head.prev = null;

		} else {
			this.tail.next = _item;
			_item.prev = tail;
		}
		this.tail = _item;
		min = this.tail.value;
	}

	public Node<T> find(T val) {
		if (this.head instanceof EmptyNode || compare(val, max) > 0 || compare(val, min) < 0)
			return new EmptyNode<T>();
		Node<T> current = this.head;
		while (!current.value.equals(val))
			current = current.next;
		return current;
	}

	public void delete(T val) {
		if (this.head instanceof EmptyNode)
			return;
		if (this.head.equals(this.tail)) {
			this.head = new EmptyNode<T>();
			this.tail = new EmptyNode<T>();
			count--;
			return;
		}
		Node<T> current = this.head;
		while (current instanceof ValueableNode) {
			if (current.value.equals(val)) {

				if (current.equals(this.head)) {
					this.head = head.next;
					this.head.prev = new EmptyNode<T>();
					count--;
					max = this.head.value;
					return;
				}
				if (current.equals(this.tail)) {
					this.tail = this.tail.prev;
					this.tail.next = new EmptyNode<T>();
					count--;
					min = this.tail.value;
					return;
				}
				current.prev.next = current.next;
				current.next.prev = current.prev;
				count--;
				return;
			}

			current = current.next;
		}
		count--;
	}

	public void clear(boolean asc) {
		if (this.head instanceof EmptyNode)
			return;
		Node<T> current = this.head;
		while (current instanceof ValueableNode) {
			this.head = current.next;
			current = current.next;
		}
		this.tail = new EmptyNode<T>();
		count = 0;
	}

	public int count() {
		return count;
	}

	public ArrayList<Node<T>> getAll() {
		ArrayList<Node<T>> r = new ArrayList<Node<T>>();
		Node<T> node = head;
		while (node instanceof ValueableNode) {
			r.add(node);
			node = node.next;
		}
		return r;
	}
}

class AscNodesOrder<T extends Comparable<T>> extends NodesOrder<T> {

	AscNodesOrder(int count) {
		count = 0;
		head = new EmptyNode<T>();
		tail = new EmptyNode<T>();
	}

	public int compare(T v1, T v2) {
		return (v1.compareTo(v2));
	}

	// complex 13
	public void add(T value) {
		ValueableNode<T> nodeToAdd = new ValueableNode<T>(value);
		Node<T> current = this.head;
		// check for empty list
		if (this.head instanceof EmptyNode) {
			addInTail(nodeToAdd);
			max = this.head.value;
			min = this.head.value;
			count++;
			return;
		}

		// check for
		if (compare(nodeToAdd.value, min) < 0 || compare(nodeToAdd.value, max) > 0) {
			if (compare(nodeToAdd.value, this.head.value) > 0)
				addInTail(nodeToAdd);
			count++;
			return;
		}

		current = recAscSearch(value, current, nodeToAdd);
		insertAfter((ValueableNode) current, nodeToAdd);
		count++;

		min = this.head.value;
		max = this.tail.value;
	}

	private Node<T> recAscSearch(T value, Node<T> current, Node<T> nodeToAdd) {
		if (current.next instanceof EmptyNode
				|| (compare(nodeToAdd.value, current.value) > 0 && compare(nodeToAdd.value, current.next.value) < 0)
				|| (compare(current.value, nodeToAdd.value) == 0))
			return current;
		current = current.next;
		return recAscSearch(value, current, nodeToAdd);

	}


	public void addInHead(ValueableNode<T> _item) {
		this.head.prev = _item;
		_item.next = head;
		this.head = _item;
		min = this.head.value;
	}

	public void addInTail(ValueableNode<T> _item) {
		ValueableNode<T> _itemToAdd = new ValueableNode<T>(_item.value);
		if (head instanceof EmptyNode) {
			this.head = _itemToAdd;

		} else {
			this.tail.next = _itemToAdd;
			_itemToAdd.prev = tail;
		}
		this.tail = _itemToAdd;
		max = this.tail.value;
	}

	public Node<T> find(T val) {
		if (this.head instanceof EmptyNode || compare(val, max) > 0 || compare(val, min) < 0)
			return new EmptyNode();
		Node<T> current = this.head;
		while (!current.value.equals(val))
			current = current.next;
		return current;
	}

	public void delete(T val) {
		if (this.head instanceof EmptyNode || compare(val, max) > 0 || compare(val, min) < 0)
			return;
		if (this.head.equals(this.tail)) {
			this.head = new EmptyNode<T>();
			this.tail = new EmptyNode<T>();
			count--;
			return;
		}
		Node<T> current = this.head;
		while (current instanceof ValueableNode) {
			if (current.value.equals(val)) {

				if (current.equals(this.head)) {
					this.head = head.next;
					this.head.prev = new EmptyNode<T>();
					count--;
					min = this.head.value;
					return;
				}
				if (current.equals(this.tail)) {
					this.tail = this.tail.prev;
					this.tail.next = new EmptyNode<T>(this.tail.next);
					count--;
					max = this.tail.value;
					return;
				}
				current.prev.next = current.next;
				current.next.prev = current.prev;
				count--;
				return;
			}

			current = current.next;
		}
		count--;
	}

	public void clear(boolean asc) {
		if (this.head instanceof EmptyNode)
			return;
		Node<T> current = this.head;
		while (!(current instanceof ValueableNode)) {
			this.head = current.next;
			current = current.next;
		}
		this.tail = new EmptyNode<T>();
		count = 0;
	}

	public int count() {
		return count;
	}

	public ArrayList<Node<T>> getAll() {
		ArrayList<Node<T>> r = new ArrayList<Node<T>>();
		Node<T> node = head;
		while (node instanceof ValueableNode) {
			r.add(node);
			node = node.next;
		}
		return r;
	}

}

public class OrderedList<T extends Comparable<T>> implements Comparator<T> {

	NodesOrder<T> order;

	public OrderedList(boolean asc) {
		order = asc ? new AscNodesOrder<T>(0) : new DescNodesOrder<T>(0);

	}

	boolean getAscenging() {
		return order instanceof AscNodesOrder;
	}

	public int compare(T v1, T v2) {
		return (v1.compareTo(v2));
	}

	// complex 13
	public void add(T value) {
		order.add(value);
	}

	public void insertAfter(ValueableNode<T> _nodeAfter, ValueableNode<T> _nodeToInsert) {
		order.insertAfter(_nodeAfter, _nodeToInsert);
	}

	public void addInHead(ValueableNode<T> _item) {
		order.addInHead(_item);
	}

	public void addInTail(ValueableNode<T> _item) {
		order.addInTail(_item);
	}

	public Node<T> find(T val) {
		return order.find(val);
	}

	public void delete(T val) {
		order.delete(val);
	}

	public void clear(boolean asc) {
		order.clear(asc);
	}

	public int count() {
		return order.count();
	}

	ArrayList<Node<T>> getAll() {
		return order.getAll();
	}

	public Node<T> getHead() {
		return order.head;
	}

	public Node<T> getTail() {
		return order.tail;
	}
}
