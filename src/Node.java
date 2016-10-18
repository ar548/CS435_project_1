/**
 * Created by Alex Rosen on 10/6/2016.
 */
public class Node {
	private Node prev;
	private Node next;
	private int val;    // the value of the hex number

	public Node(){
		this.prev = null;
		this.next = null;
		val = 0;
	}

	public Node(Node prev, Node next, int val){
		this.prev = prev;
		this.next = next;
		this.val = val;
	}

	public Node getPrev(){ return this.prev; }
	public Node getNext(){ return this.next; }
	public void setPrev(Node node){this.prev = node; }
	public void setNext(Node node){this.next = node; }
	public int getVal(){ return this.val; }
	public int getData(){ return this.val; }

}
